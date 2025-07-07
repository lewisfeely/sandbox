package uk.co.timesheets24.app.TS24.LocalDataSevice

import android.content.Context
import com.google.gson.Gson
import uk.co.timesheets24.app.TS24.API.AccountMIApiClass
import uk.co.timesheets24.app.TS24.API.JobsApiClass
import uk.co.timesheets24.app.TS24.API.ProfileApiClass
import uk.co.timesheets24.app.TS24.GlobalLookUp
import uk.co.timesheets24.app.TS24.LocalDataBase.LocalUserDatabase
import uk.co.timesheets24.app.TS24.Models.Conversion.ConvertToLocalData
import uk.co.timesheets24.app.TS24.Models.DashboardRequest

class RefreshLocalData (private val context: Context) : IRefreshLocalData {


    private val localDBConnection = LocalUserDatabase.getInstance(context.applicationContext)
    private val convertService = ConvertToLocalData();

    val profileApi = ProfileApiClass(context).profile
    val jobsApi = JobsApiClass(context).jobs
    val accountMIApi = AccountMIApiClass(context).accountMI

    var refreshRunning:Boolean = false
    var resreshDescription:String = ""

    override suspend fun DoWork() : Boolean {

        if (refreshRunning) {
            return false;
        }

        resreshDescription = "Starting refresh"
        refreshRunning = true;

        // body

        resreshDescription = "Refreshing live jobs"
        RefreshLiveJobs()

        resreshDescription = "Refreshing live jobs"
        RefreshRecentEntries()

        resreshDescription = "Refreshing dashboard data"
        RefreshDashboard()

        resreshDescription = "Refreshing job status codes"
        RefreshJobTimeStatus()

        resreshDescription = "Refreshing user profile"
        RefreshUser()

        resreshDescription = "Refreshing permissions"
        RefreshRecentPermissions()

        refreshRunning = false;
        return false
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
        val requestStr = Gson().toJson(DashboardRequest(timeSheetJobdateFrom = "2025-04-01T13:45:00Z", timeSheetJobdateTo = "2025-07-20T13:45:00Z"))
        println("RESPONSE $requestStr")
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


}