package uk.co.timesheets24.app.timesheetssandbox

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import uk.co.timesheets24.app.timesheetssandbox.LocalDataBase.LocalUserDatabase
import uk.co.timesheets24.app.timesheetssandbox.Models.AuthResponse
import uk.co.timesheets24.app.timesheetssandbox.Models.EditingTimeSheet

import uk.co.timesheets24.app.timesheetssandbox.Models.LocalData.LiveJobLocal
import uk.co.timesheets24.app.timesheetssandbox.Models.ReceivedTimeSheet

import uk.co.timesheets24.app.timesheetssandbox.Models.RemoteData.LiveJobRemote
import uk.co.timesheets24.app.timesheetssandbox.Models.RemoteData.RecentEntryRemote
import uk.co.timesheets24.app.timesheetssandbox.Models.TimeSheetEntry
import uk.co.timesheets24.app.timesheetssandbox.Models.TimeSheetEntryTable
import uk.co.timesheets24.app.timesheetssandbox.Models.TimeSheetTable
import uk.co.timesheets24.app.timesheetssandbox.Models.UserDetails
import java.time.LocalDateTime
import java.util.UUID
import kotlin.String
import kotlin.jvm.java
import kotlin.text.contains
import kotlin.toString

class ConnectService(context: Context, workerParams: WorkerParameters) : CoroutineWorker(context, workerParams) {

    val context : Context = context


    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.AUTH_BASE_URL)
        .client(if(BuildConfig.AUTH_BASE_URL.contains("10.0.2.2")) GlobalLookUp.getUnsafeOkHttpClient(context) else GlobalLookUp.getSafeOkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService = retrofit.create(ApiService::class.java)

    interface ApiService {

        @GET("auth")
        suspend fun getAuth() : String

        @FormUrlEncoded
        @POST("auth")
        suspend fun authentication(@Field("email") email: String,
                                   @Field("password") password: String) : AuthResponse

    }

    val retrofitDetails : Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.PROFILE_URL)
        .client(if(BuildConfig.AUTH_BASE_URL.contains("10.0.2.2")) GlobalLookUp.getUnsafeOkHttpClient(context) else GlobalLookUp.getSafeOkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    val apiDetailsService = retrofitDetails.create(DetailsApiService::class.java)

    interface DetailsApiService {

        @GET("details")
        suspend fun getDetails(@Header("Authorization") token : String) : UserDetails

    }


    val retrofitJobs: Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.JOBS_URL)
        .client(if(BuildConfig.AUTH_BASE_URL.contains("10.0.2.2")) GlobalLookUp.getUnsafeOkHttpClient(context) else GlobalLookUp.getSafeOkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val Jobs = retrofitJobs.create(JobsApi::class.java)

    interface JobsApi {

        @GET("recententries")
        suspend fun recentEntries(@Header("Authorization") token : String) : List<RecentEntryRemote>

        @GET("timesheets/{tsId}")
        suspend fun getJob(@Header("Authorization") token : String, @Path("tsId") tsId : String) : ReceivedTimeSheet

        @POST("timesheets")
        suspend fun editTimesheet(@Header("Authorization") token: String, @Body body: EditingTimeSheet?)

        @DELETE("timesheets/{tsId}")
        suspend fun deleteTimesheet(@Header("Authorization") token : String, @Path("tsId") tsId : String)

       // @GET("livejobs")
       // suspend fun getJobs(@Header("Authorization") token : String) : List<LiveJob>

        @POST("timesheets")
        suspend fun postTimesheet(@Header("Authorization") token : String, @Body body : TimeSheetEntry)

      //  @GET("jobdetails/{jobId}")
      //  suspend fun getJobDetails(@Header("Authorization") token : String, @Path("jobId") jobId : String) : LiveJob


    }

    val instance = LocalUserDatabase.getInstance(context.applicationContext)
    val jobDao = instance.jobDao()
    val recentEntryDao = instance.recentEntriesDao()
    val pendingTimeSheetEntry = instance.pendingTimeSheetEntries()
    val timeSheetTable = instance.timeSheetDao()


    override suspend fun doWork(): Result {

        if (inputData.getBoolean("sync_sheets", false)) {
            val result = syncData()
            return if (result) {
                Result.success()
            } else {
                Result.failure()
            }
        } else if (inputData.getBoolean("login", false)) {

            val email = inputData.getString("email")
            val password = inputData.getString("password")
            val result = login(email.toString(), password.toString())

            return if (result) {
                Result.success()
            } else {
                Result.failure()
            }
        } else if (inputData.getBoolean("fetch_entries", false)) {
            val result = fetchEntries()
            return if (result) {
                Result.success()
            } else {
                Result.failure()
            }
        }
        return Result.success()
    }

    private suspend fun syncData() : Boolean {

        try {
            val pending = pendingTimeSheetEntry.fetchAllPending()

            if (pending.isNotEmpty()) {
                pending.forEach { entry ->

                    val offlineSync = toTimeSheetEntry(entry)
                    Jobs.postTimesheet("Bearer ${GlobalLookUp.token}", offlineSync)
                }
            }
            pendingTimeSheetEntry.deleteAll()
            return true
        } catch (e : Exception) {
            println("RESPONSE $e inside sync data")
            return false
        }
    }

    private suspend fun login(email : String, password: String) : Boolean {
        try {
            val response = apiService.authentication(email.toString(), password.toString())
            GlobalLookUp.token = response.access_token
            println("RESPONSE $response Outcome")
            return true
        } catch (e : Exception) {
            println("RESPONSE $e inside login")
            return false
        }

    }

    private suspend fun fetchEntries() : Boolean {
        try {

            val recentEntries = Jobs.recentEntries("Bearer ${GlobalLookUp.token}")
            GlobalLookUp.recentEntries = recentEntries
            println("RESPONSE $recentEntries Outcome")
            return true
        } catch (e : Exception) {
            println("RESPONSE $e inside fetch Entries")
            return false
        }
    }


//
//    private suspend fun selectJobToEdit(key : String, index : Int) : Data {
//
//
//        try {
//
//            if (GlobalLookUp.hasInternetAccess(context)) {
//                val data = Jobs.getJob("Bearer ${GlobalLookUp.token}", key)
//                GlobalLookUp.jobReturned.value = data
//            } else {
//                val sheetList = timeSheetTable.selectAllTimesheets()
//                val toEdit = sheetList[index]
//                val formattedData = mapTimeSheetTableToReceivedTimeSheet(toEdit)
//                GlobalLookUp.offlineTimeSheetId = toEdit.id
//                GlobalLookUp.jobReturned.value = formattedData
//            }
//            return workDataOf()
//        } catch (e : Exception) {
//            return workDataOf()
//        }
//
//
//    }
//
//    private suspend fun editTimeSheet(editedSheet : EditingTimeSheet) {
//
//        println("RESPONSE GONE TO EDIT $editedSheet")
//
//
//        try {
//            if(GlobalLookUp.hasInternetAccess(context)) {
//                Jobs.editTimesheet("Bearer ${GlobalLookUp.token}", editedSheet)
//                GlobalLookUp.jobReturned.value = null
//                println("RESPONSE SUCCESSFUL EDIT")
//            } else {
//
//                val timeSheet = editingTimeSheetToTimeSheetTable(editedSheet, GlobalLookUp.offlineTimeSheetId)
//                timeSheetTable.editTimeSheet(timeSheet)
//                val recentEntry = recentEntryDao.fetchByIdAndDescription(GlobalLookUp.jobReturned.value!!.timeId, GlobalLookUp.jobReturned.value!!.description)
//                val recentEntryEdit = editingTimeSheetToRecentEntryTable(editedSheet, recentEntry.id, recentEntry.jobNumber, recentEntry.jobDate, recentEntry.clientName)
//                recentEntryDao.editRecentEntry(recentEntryEdit)
//                GlobalLookUp.jobReturned.value = null
//            }
//
//        } catch (e : Exception) {
//            println("RESPONSE $e in edit job")
//
//        }
//
//
//    }
//
//    private suspend fun deleteEntry(deleteId : String?) : Boolean {
//
//        try {
//            println("RESPONSE DELETE ID INSIDE SERIVCE FUN $deleteId")
//            if (deleteId != null) {
//
//                Jobs.deleteTimesheet("Bearer ${GlobalLookUp.token}", deleteId)
//                recentEntryDao.deleteRecentEntry(deleteId.toString())
//                return true
//            } else {
//                println("FAILED DUE TO NULL ID")
//                return false
//            }
//
//        } catch (e : Exception) {
//            println("RESPONSE $e in delete job")
//            return false
//        }
//
//
//    }
//
//    private suspend fun offlineDeleteEntry(description : String) : Boolean {
//        try {
//            pendingTimeSheetEntry.deletePendingRecentEntry(description)
//            recentEntryDao.deleteRecentEntry(description)
//            return true
//        } catch (e : Exception) {
//            println("RESPONSE $e Failure in offline delete")
//            return false
//        }
//    }
//
//    private suspend fun getJobs() : List<LiveJob>? {
//        try {
//
//            if (GlobalLookUp.hasInternetAccess(applicationContext)) {
//                jobDao.clear()
//
//
//                val liveJobs = Jobs.getJobs("Bearer ${GlobalLookUp.token}")
//                liveJobs.forEach { job ->
//                    val newJob = convertToLiveJobsTable(job)
//                    jobDao.insert(newJob)
//                }
//                println("RESPONSE RETURNED LIVE JOB LIST $liveJobs")
//                return liveJobs
//            } else {
//                val liveJobs = jobDao.fetchJobs()
//                val convertedLiveJobs = liveJobs.map { job ->
//                    convertToLiveJob(job)
//                }
//
//                return convertedLiveJobs
//            }
//
//
//
//        } catch (e : Exception) {
//            println("RESPONSE $e in delete job")
//            return null
//        }
//
//    }
//
//    private suspend fun createJob(job : CreateJob) : Boolean {
//        try {
//
//            val jobId = Jobs.addJob("Bearer ${GlobalLookUp.token}", job)
//            val jobsList = Jobs.getJobs("Bearer ${GlobalLookUp.token}")
//            jobsList.forEach { job ->
//                if (job.jobGUID == jobId) {
//                    jobDao.insert(convertToLiveJobsTable(job))
//                }
//            }
//            return true
//        } catch (e : Exception) {
//
//            println("RESPONSE $e")
//            return false
//        }
//
//    }
//
//    private suspend fun offlineLogin() : Boolean {
//        try {
//
//            val users = userDao.getAllUsers()
//            val token = users[users.size - 1].access_token
//            val userDetails = userDetailsDao.getDetails(token)
//            GlobalLookUp.userState = convertToUserDetails(userDetails)
//            GlobalLookUp.token = token
//            GlobalLookUp.contactId = userDetails.id
//
//            return true
//        } catch (e : Exception) {
//
//            println("RESPONSE OFFLINE LOGIN FAILED $e")
//            return false
//        }
//    }
//
//    private suspend fun postNewEntry(timesheet : TimeSheetEntry) {
//
//        try {
//
//            timeSheetTable.insert(convertToTimeSheetTable(timesheet))
//
//            if (GlobalLookUp.hasInternetAccess(context)) {
//
//                recentEntryDao.clearEntries()
//
//                Jobs.postTimesheet("Bearer ${GlobalLookUp.token}", timesheet)
//
//                recentEntryDao.insert(
//                    recentEntry = mapTimeSheetEntryToRecentEntryTable(timesheet,
//                        GlobalLookUp.currentJob?.jobNumber?.toInt()
//                            ?: 0, timesheet.startDate, GlobalLookUp.currentJob?.clientName
//                    ))
//
//                if (GlobalLookUp.imageFile != null) {
//
//                    val submittedFile = FileToJob(
//                        jobGUID = timesheet.jobGUID.toString(),
//                        timesheetId = "",
//                        fileName = GlobalLookUp.imageFile?.name.toString(),
//                        fileExt = "",
//                        fileTypeId = "12C18C6B-5D84-46B8-93B3-B125013F9E9D",
//                        tempFileId = "",
//                        fileId = "",
//                        sessionId = ""
//                    )
////                    Jobs.sendFileToJob("Bearer ${GlobalLookUp.token}", submittedFile)
//                    imageFileDao.insert(fileToJobToTableEntity(submittedFile))
//                }
//
//            }
//
//            if (!GlobalLookUp.hasInternetAccess(context)) {
//                pendingTimeSheetEntry.insert(convertToTimeSheetEntryTable(timesheet, GlobalLookUp.currentJob?.clientName, GlobalLookUp.currentJob?.jobNumber?.toInt()))
//            }
//            GlobalLookUp.currentJob = null
//
//        } catch (e : Exception) {
//            println("RESPONSE $e")
//        }
//
//    }
//
//
}

fun editingTimeSheetToTimeSheetTable(
    editing: EditingTimeSheet,
    id: Int,
): TimeSheetTable {
    return TimeSheetTable(
        id = id,
        timeId = editing.timeSheetId,
        contactName = editing.contactName,
        contactId = editing.contactId,
        jobDate = editing.startTime,
        description = editing.description,
        materials = editing.materials,
        overTime = editing.overTime,
        timetake = editing.timetake.toIntOrNull() ?: 0, // Convert safely
        travellingTime = editing.travellingTime,
        createdDate = editing.createdDate,
        modifiedDate = editing.modifiedDate,
        jobGUID = editing.jobGUID,
        startTime = editing.startTime,
        endTime = editing.endTime,
        jobTimeStatusId = editing.jobTimeStatusId,
        jobTimeStatus = editing.jobTimeStatus
    )
}

fun toTimeSheetEntry(entryTable: TimeSheetEntryTable): TimeSheetEntry {
    return TimeSheetEntry(
        jobGUID = entryTable.jobGUID,
        timeSheetId = entryTable.timeSheetId,
        contactId = entryTable.contactId,
        description = entryTable.description,
        materials = entryTable.materials,
        startDate = entryTable.startDate,
        startTime = entryTable.startTime,
        endDate = entryTable.endDate,
        endTime = entryTable.endTime,
        timetake = entryTable.timetake,
        overTime = entryTable.overTime,
        travellingTime = entryTable.travellingTime
    )
}

fun convertToLiveJobsTable(liveJob: LiveJobRemote): LiveJobLocal {
    return LiveJobLocal(
        poNumber = liveJob.poNumber.toString(),
        createdDate = liveJob.createdDate,
        description = liveJob.description,
        jobDate = liveJob.jobDate,
        jobGUID = liveJob.jobGUID,
        jobNumber = liveJob.jobNumber,
        clientId = liveJob.clientId.toString(),
        clientName = liveJob.clientName.toString(),
        clientContactId = liveJob.clientContactId.toString(),
        clientContactName = liveJob.clientContactName.toString(),
        addressId = liveJob.addressId.toString(),
        customerSite = liveJob.customerSite.toString(),
        timeTime = liveJob.timeTime.toInt(),
        travellingTime = liveJob.travellingTime,
        overtime = liveJob.overtime,
        readyToClose = liveJob.readyToClose
    )
}

fun convertToLiveJob(entity: LiveJobLocal): LiveJobLocal {
    return LiveJobLocal(
        poNumber = entity.poNumber,
        createdDate = entity.createdDate,
        description = entity.description,
        jobDate = entity.jobDate,
        jobGUID = entity.jobGUID,
        jobNumber = entity.jobNumber,
        clientId = entity.clientId,
        clientName = entity.clientName,
        clientContactId = entity.clientContactId,
        clientContactName = entity.clientContactName,
        addressId = entity.addressId,
        customerSite = entity.customerSite,
        timeTime = 0,
        travellingTime = entity.travellingTime,
        overtime = entity.overtime,
        readyToClose = entity.readyToClose
    )
}

fun convertToTimeSheetEntryTable(entry: TimeSheetEntry, clientName : String?, jobNumber : Int?): TimeSheetEntryTable {
    return TimeSheetEntryTable(
        timeSheetId = entry.timeSheetId,
        jobGUID = entry.jobGUID,
        contactId = entry.contactId,
        description = entry.description,
        materials = entry.materials,
        startDate = entry.startDate,
        startTime = entry.startTime,
        endDate = entry.endDate,
        endTime = entry.endTime,
        timetake = entry.timetake,
        overTime = entry.overTime,
        travellingTime = entry.travellingTime,
        tempId = UUID.randomUUID().toString(),
        jobNumber = jobNumber ?: 0,
        clientName = clientName
    )
}

fun mapReceivedTimeSheetToTimeSheet(received: ReceivedTimeSheet): TimeSheetTable {
    return TimeSheetTable(
        id = 0,
        timeId = received.timeId,
        contactName = received.contactName,
        contactId = received.contactId,
        jobDate = received.jobDate,
        description = received.description,
        materials = received.materials,
        overTime = received.overTime,
        timetake = received.timetake,
        travellingTime = received.travellingTime,
        createdDate = received.createdDate,
        modifiedDate = received.modifiedDate,
        jobGUID = received.jobGUID,
        startTime = received.startTime,
        endTime = received.endTime,
        jobTimeStatusId = received.jobTimeStatusId,
        jobTimeStatus = received.jobTimeStatus
    )
}

fun mapTimeSheetTableToReceivedTimeSheet(entity: TimeSheetTable): ReceivedTimeSheet {
    return ReceivedTimeSheet(
        timeId = entity.timeId,
        contactName = entity.contactName,
        contactId = entity.contactId,
        jobDate = entity.jobDate,
        description = entity.description,
        materials = entity.materials,
        overTime = entity.overTime,
        timetake = entity.timetake,
        travellingTime = entity.travellingTime,
        createdDate = entity.createdDate,
        modifiedDate = entity.modifiedDate,
        jobGUID = entity.jobGUID,
        startTime = entity.startTime,
        endTime = entity.endTime,
        jobTimeStatusId = entity.jobTimeStatusId,
        jobTimeStatus = entity.jobTimeStatus
    )
}
//
//fun convertToTimeSheetTable(
//    entry: TimeSheetEntry,
//    contactName: String? = null,
//    createdDate: String = getCurrentTimestamp(),
//    modifiedDate: String = getCurrentTimestamp(),
//    jobTimeStatusId: String? = null,
//    jobTimeStatus: String? = null
//): TimeSheetTable {
//    return TimeSheetTable(
//        timeId = entry.timeSheetId,
//        contactName = contactName,
//        contactId = entry.contactId,
//        jobDate = entry.startDate,
//        description = entry.description,
//        materials = entry.materials,
//        overTime = entry.overTime,
//        timetake = entry.timetake,
//        travellingTime = entry.travellingTime,
//        createdDate = createdDate,
//        modifiedDate = modifiedDate,
//        jobGUID = entry.jobGUID,
//        startTime = entry.startTime,
//        endTime = entry.endTime,
//        jobTimeStatusId = jobTimeStatusId,
//        jobTimeStatus = jobTimeStatus
//    )
//}
//
//fun getCurrentTimestamp(): String {
//    return LocalDateTime.now().toString() // Format as needed
//}


