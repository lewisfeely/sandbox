package uk.co.timesheets24.app.TS24.LocalDataSevice

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import uk.co.timesheets24.app.TS24.API.AccountMIApiClass
import uk.co.timesheets24.app.TS24.API.AuthApiClass
import uk.co.timesheets24.app.TS24.API.JobsApiClass
import uk.co.timesheets24.app.TS24.API.ProfileApiClass
import uk.co.timesheets24.app.TS24.GlobalLookUp
import uk.co.timesheets24.app.TS24.LocalDataBase.LocalUserDatabase
import uk.co.timesheets24.app.TS24.Models.Conversion.ConvertToLocalData
import uk.co.timesheets24.app.TS24.Models.DashboardRequest
import uk.co.timesheets24.app.TS24.Models.LocalData.AccessTokenLocal
import uk.co.timesheets24.app.TS24.Models.RemoteData.RefreshTokenRemote
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit

class RefreshLocalData (private val context: Context, private val localDBConnection: LocalUserDatabase = LocalUserDatabase.getInstance(context.applicationContext)
) : IRefreshLocalData {


    private val convertService = ConvertToLocalData();

    val profileApi = ProfileApiClass(context).profile
    val jobsApi = JobsApiClass(context).jobs
    val accountMIApi = AccountMIApiClass(context).accountMI

    var refreshRunning:Boolean = false
    val refreshDescription = MutableLiveData<String>()

    override suspend fun DoWork() : Boolean {

        if (refreshRunning) {
            return false;
        }

        refreshDescription.postValue("Starting refresh")
        refreshRunning = true;

        refreshDescription.postValue("checking token")
        refreshToken()

        refreshDescription.postValue("Refreshing live jobs")
        RefreshLiveJobs()

        refreshDescription.postValue("Refreshing live timesheets")
        RefreshRecentEntries()

        refreshDescription.postValue("Refreshing dashboard data")
        RefreshDashboard()

        refreshDescription.postValue("Refreshing job status codes")
        RefreshJobTimeStatus()

        refreshDescription.postValue("Refreshing user profile")
        RefreshUser()

        refreshDescription.postValue("Refreshing permissions")
        RefreshRecentPermissions()

        refreshDescription.postValue("")
        refreshRunning = false;
        return false
    }

    suspend fun refreshToken() : Boolean {
        val tokenTable = localDBConnection.accessTokenDao()
        val createdDate = tokenTable.fetch()
        val date = createdDate.timeCreated
        if (isOverAnHourOld(date.toString())) {
            try {
                val authApi = AuthApiClass(context).authApi
                val response = authApi.refreshToken(RefreshTokenRemote("refresh_token", GlobalLookUp.refresh_token.toString()))
                GlobalLookUp.token = response.access_token
                val tokenTable = localDBConnection.accessTokenDao()
                tokenTable.clear()
                tokenTable.insert(AccessTokenLocal(accessToken = response.access_token, refreshToken = response.refresh_token, timeCreated = getCurrentTimestamp()))
            } catch (e : Exception) {
                println("RESPONSE $e error inside refresh token")
                return false
            }

            return true
        }
        return true
    }

    fun getCurrentTimestamp(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return dateFormat.format(Date())
    }

    suspend fun RefreshLiveJobs(): Boolean {
        val liveJobList = jobsApi.getJobs("Bearer ${GlobalLookUp.token}")
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
        val recentEntryList = jobsApi.recentEntries("Bearer ${GlobalLookUp.token}")
        val recentEntryDao = localDBConnection.recentEntriesDao()

        recentEntryDao.clearEntries();
        recentEntryList.forEach{ entry ->
            recentEntryDao.insert(convertService.convertToLocalRecentEntry(entry))
        }
        if (recentEntryDao.fetchEntries().size == recentEntryList.size){
            return true;
        }
        return false;
    }

    suspend fun RefreshDashboard(): Boolean {
        val requestStr = Gson().toJson(DashboardRequest(timeSheetJobdateFrom = "2025-05-01T13:45:00Z", timeSheetJobdateTo = "2025-07-20T13:45:00Z"))
        val dashboardData = accountMIApi.dashBoard("Bearer ${GlobalLookUp.token}", requestStr)
        val dashboardDao = localDBConnection.dashboardDao()
        dashboardDao.insert(convertService.convertToLocalDashboard(dashboardData))
        if (dashboardDao.fetch().Timetake == dashboardData.Timetake){
            return true;
        }
        return false;
    }

    suspend fun RefreshJobTimeStatus(): Boolean {
        val jobTimeStatusList = jobsApi.getJobTimeStatus("Bearer ${GlobalLookUp.token}")
        val jobTimeStatusDao = localDBConnection.jobTimeStatusDao()
        jobTimeStatusDao.clear();
        jobTimeStatusList.forEach{entry ->
            jobTimeStatusDao.insert(convertService.convertToLocalJobStatus(entry))
        }
        if (jobTimeStatusDao.fetch().size == jobTimeStatusList.size){
            return true;
        }
        return false;
    }

    fun CheckDeviceId(): Boolean {
        TODO("Not yet implemented")
    }

    suspend fun RefreshUser(): Boolean {
        val profileData = profileApi.details("Bearer ${GlobalLookUp.token}")
        GlobalLookUp.contactId = profileData.id
        val profileDao = localDBConnection.profileDao()
        profileDao.insert(convertService.convertToLocalProfile(profileData))
        if (profileDao.fetch().id == profileData.id){
            return true;
        }
        return false;
    }

    suspend fun RefreshRecentPermissions(): Boolean {
        val permissionList = profileApi.permissions(("Bearer ${GlobalLookUp.token}"))
        val permissionDao = localDBConnection.permissionDao()

        permissionDao.clear();
        permissionList.forEach{entry ->
            permissionDao.insert(convertService.convertToLocalPermission(entry))
        }
        if (permissionDao.fetch().size == permissionList.size){
            return true;
        }
        return false;
    }

    fun isOverAnHourOld(dateString: String): Boolean {
        return try {
            val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            format.timeZone = TimeZone.getDefault() // optional; use UTC if needed

            val inputDate = format.parse(dateString)
            val currentDate = Date()

            val differenceInMillis = currentDate.time - inputDate.time
            val differenceInHours = TimeUnit.MILLISECONDS.toHours(differenceInMillis)

            differenceInHours >= 1
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }


}