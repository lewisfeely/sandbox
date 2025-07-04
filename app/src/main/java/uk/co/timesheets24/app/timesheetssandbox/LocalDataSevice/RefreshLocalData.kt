package uk.co.timesheets24.app.timesheetssandbox.LocalDataSevice

import android.content.Context
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import uk.co.timesheets24.app.timesheetssandbox.BuildConfig
import uk.co.timesheets24.app.timesheetssandbox.GlobalLookUp
import uk.co.timesheets24.app.timesheetssandbox.LocalDataBase.LocalUserDatabase
import uk.co.timesheets24.app.timesheetssandbox.Models.Conversion.ConvertToLocalData
import uk.co.timesheets24.app.timesheetssandbox.Models.EditingTimeSheet
import uk.co.timesheets24.app.timesheetssandbox.Models.RemoteData.LiveJobRemote

import uk.co.timesheets24.app.timesheetssandbox.Models.ReceivedTimeSheet
import uk.co.timesheets24.app.timesheetssandbox.Models.RemoteData.RecentEntryRemote
import uk.co.timesheets24.app.timesheetssandbox.Models.TimeSheetEntry

class RefreshLocalData (context: Context) : IRefreshLocalData {

    private val context : Context = context
    private val localDBConnection = LocalUserDatabase.getInstance(context.applicationContext)
    private val convertService = ConvertToLocalData();

    override suspend fun DoWork() : Boolean {
        // body
        GlobalLookUp.token = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjQ3YWU0OWM0YzlkM2ViODVhNTI1NDA3MmMzMGQyZThlNzY2MWVmZTEiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vdHMyNC0zODE5MCIsImF1ZCI6InRzMjQtMzgxOTAiLCJhdXRoX3RpbWUiOjE3NTE2MTQ1NjQsInVzZXJfaWQiOiJiemZ6Q0FieEFpZG93RXB6UFRXMjBVN0FXc3cyIiwic3ViIjoiYnpmekNBYnhBaWRvd0VwelBUVzIwVTdBV3N3MiIsImlhdCI6MTc1MTYxNDU2NCwiZXhwIjoxNzUxNjE4MTY0LCJlbWFpbCI6Im1pa2UuZmVlbHlAb3V0bG9vay5jb20iLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsImZpcmViYXNlIjp7ImlkZW50aXRpZXMiOnsiZW1haWwiOlsibWlrZS5mZWVseUBvdXRsb29rLmNvbSJdfSwic2lnbl9pbl9wcm92aWRlciI6InBhc3N3b3JkIn19.fqa9_7SGI3xCjsbB9Y-XAIQ6yzkRB1y2orVJJMpb0k4T8BSuSsE-Su1pRtDSx2kLIPZQjnxl3PxZ9Jr_n8smo4qaaI7CdTBzaIPfgrtCH1Nm2LQr9EqPTo4MbuluT13xLqbcRKGGOj8eptAPNL4945m1z26bhQRwfiIbcUooCzduaX64Kru69cvk6LR57GSOPov7Vggx7KimmerarffsHLRYjObuCZRRCeDis66g9skWlyEl5Uvnlg9zGvfpT450xRynrviyfB38jFGrOx3UBJYKFr8gNALsD6m6nPC2om_VSSaxMqVDRv8YhpKB6UrEqIdF0-nHbfB8z66Zf_ePkA"

        RefreshLiveJobs()

        RefreshRecentEntries()

        return false
    }

    suspend fun RefreshLiveJobs(): Boolean {
        val liveJobList = Jobs.getJobs("Bearer ${GlobalLookUp.token}")
        val jobDao = localDBConnection.jobDao()

        jobDao.clear();
        liveJobList.forEach{job ->
            jobDao.insert(convertService.convertToLocalLiveJob(job))
        }
        if (jobDao.fetchJobs().size == liveJobList.size){
            return true;
        }
        return false;
    }

    suspend fun RefreshRecentEntries(): Boolean {
        val recentEntryList = Jobs.recentEntries("Bearer ${GlobalLookUp.token}")
        val recentEntryDao = localDBConnection.recentEntriesDao()

        recentEntryDao.clearEntries();
        recentEntryList.forEach{entry ->
            recentEntryDao.insert(convertService.convertToLocalRecentEntry(entry))
        }
        if (recentEntryDao.fetchEntries().size == recentEntryList.size){
            return true;
        }
        return false;
    }

    fun RefreshDashboard(): Boolean {
        TODO("Not yet implemented")
    }

    fun RefJobStatus(): Boolean {
        TODO("Not yet implemented")
    }

    fun CheckDeviceId(): Boolean {
        TODO("Not yet implemented")
    }

    fun RefreshUser(): Boolean {
        TODO("Not yet implemented")
    }

    val retrofitJobs: Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.JOBS_URL)
        .client(GlobalLookUp.getSafeOkHttpClient())
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

        @GET("livejobs")
        suspend fun getJobs(@Header("Authorization") token : String) : List<LiveJobRemote>

        @POST("timesheets")
        suspend fun postTimesheet(@Header("Authorization") token : String, @Body body : TimeSheetEntry)

        @GET("jobdetails/{jobId}")
        suspend fun getJobDetails(@Header("Authorization") token : String, @Path("jobId") jobId : String) : LiveJobRemote


    }




}