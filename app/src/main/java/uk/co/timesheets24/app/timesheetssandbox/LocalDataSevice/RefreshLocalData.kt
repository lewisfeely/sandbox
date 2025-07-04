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
import uk.co.timesheets24.app.timesheetssandbox.Models.RemoteData.DashboardRemote
import uk.co.timesheets24.app.timesheetssandbox.Models.RemoteData.JobTimeStatusRemote
import uk.co.timesheets24.app.timesheetssandbox.Models.RemoteData.PermissionRemote
import uk.co.timesheets24.app.timesheetssandbox.Models.RemoteData.ProfileRemote
import uk.co.timesheets24.app.timesheetssandbox.Models.RemoteData.RecentEntryRemote
import uk.co.timesheets24.app.timesheetssandbox.Models.TimeSheetEntry

class RefreshLocalData (context: Context) : IRefreshLocalData {

    private val context : Context = context
    private val localDBConnection = LocalUserDatabase.getInstance(context.applicationContext)
    private val convertService = ConvertToLocalData();

    override suspend fun DoWork() : Boolean {
        // body
        GlobalLookUp.token = "eyJhbGciOiJSUzI1NiIsImtpZCI6Ijg3NzQ4NTAwMmYwNWJlMDI2N2VmNDU5ZjViNTEzNTMzYjVjNThjMTIiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vdHMyNC0zODE5MCIsImF1ZCI6InRzMjQtMzgxOTAiLCJhdXRoX3RpbWUiOjE3NTE2MzI1NzUsInVzZXJfaWQiOiJiemZ6Q0FieEFpZG93RXB6UFRXMjBVN0FXc3cyIiwic3ViIjoiYnpmekNBYnhBaWRvd0VwelBUVzIwVTdBV3N3MiIsImlhdCI6MTc1MTYzMjU3NSwiZXhwIjoxNzUxNjM2MTc1LCJlbWFpbCI6Im1pa2UuZmVlbHlAb3V0bG9vay5jb20iLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsImZpcmViYXNlIjp7ImlkZW50aXRpZXMiOnsiZW1haWwiOlsibWlrZS5mZWVseUBvdXRsb29rLmNvbSJdfSwic2lnbl9pbl9wcm92aWRlciI6InBhc3N3b3JkIn19.3cG1fRNwlD8rZHJZAUSIwC3setF8QCBB7joU6fIrmcyE7sXN5iMEdP--pR7eKSi4atiJPPeW7Em6mUq1EZAz23iedB_xKsGqqJJlk3z2z_HdNOouUZ4RKNlaXtwyb1YqFD7zyHV6d-YHptVg17dEovY1CQlbkGxSvsSTXyYd8hmHb52LWw-V4_qZK49M8cS2DGKOgY6rP_9a5rkaz0DniAPhX81gopOXH5-BsXBVLTIzyfajkdETs1uXYgMjXvuDk9vBhECf5Vj7jcu3vYe1S2Kqdade8ilhLGZLDfdXglwbgF3bCUpJ8GpmFpffj2LTwaQhnYtjB7m18thBH0br-w"

        RefreshLiveJobs()

        RefreshRecentEntries()

        //RefreshDashboard()

        RefreshJobTimeStatus()

        RefreshUser()

        RefreshRecentPermissions()

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

    suspend fun RefreshDashboard(): Boolean {
        val dashboardData = MIapi.dashboard("Bearer ${GlobalLookUp.token}")
        val dashboardDao = localDBConnection.dashboardDao()
        dashboardDao.insert(convertService.convertToLocalDashboard(dashboardData))
        if (dashboardDao.fetch().Timetake == dashboardData.Timetake){
            return true;
        }
        return false;
    }

    suspend fun RefreshJobTimeStatus(): Boolean {
        val jobTimeStatusList = Jobs.getJobTimeStatus("Bearer ${GlobalLookUp.token}")
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
        val profileData = Profileapi.details("Bearer ${GlobalLookUp.token}")
        val profileDao = localDBConnection.profileDao()
        profileDao.insert(convertService.convertToLocalProfile(profileData))
        if (profileDao.fetch().id == profileData.id){
            return true;
        }
        return false;
    }

    suspend fun RefreshRecentPermissions(): Boolean {
        val permissionList = Profileapi.permissions(("Bearer ${GlobalLookUp.token}"))
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


    val retrofitProfile: Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.PROFILE_URL)
        .client(GlobalLookUp.getSafeOkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val Profileapi = retrofitProfile.create(ProfileApi::class.java)

    interface ProfileApi {

        @GET("Details")
        suspend fun details(@Header("Authorization") token: String): ProfileRemote

        @GET("Permission")
        suspend fun permissions(@Header("Authorization") token: String): List<PermissionRemote>
    }



    val retrofitMIs: Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.ACCOUNTMI_URL)
        .client(GlobalLookUp.getSafeOkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val MIapi = retrofitMIs.create(AccountMiApi::class.java)

    interface AccountMiApi {

        @GET("Contact")
        suspend fun dashboard(@Header("Authorization") token: String): DashboardRemote
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

        @GET("JobTimeStatus")
        suspend fun getJobTimeStatus(@Header("Authorization") token : String) : List<JobTimeStatusRemote>

    }




}