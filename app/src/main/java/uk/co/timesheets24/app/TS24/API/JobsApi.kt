package uk.co.timesheets24.app.TS24.API

import android.content.Context
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import uk.co.timesheets24.app.TS24.BuildConfig
import uk.co.timesheets24.app.TS24.GlobalLookUp
import uk.co.timesheets24.app.TS24.Models.CreateJob
import uk.co.timesheets24.app.TS24.Models.EditingTimeSheet
import uk.co.timesheets24.app.TS24.Models.ReceivedTimeSheet
import uk.co.timesheets24.app.TS24.Models.RemoteData.JobTimeStatusRemote
import uk.co.timesheets24.app.TS24.Models.RemoteData.LiveJobRemote
import uk.co.timesheets24.app.TS24.Models.RemoteData.RecentEntryRemote
import uk.co.timesheets24.app.TS24.Models.TimeSheetEntry


class JobsApiClass(context: Context) {

    val retrofitJobs : Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.JOBS_URL)
        .client(
            if (BuildConfig.AUTH_BASE_URL.contains("10.0.2.2")) GlobalLookUp.getUnsafeOkHttpClient(

            ) else GlobalLookUp.getSafeOkHttpClient()
        )
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val jobs : JobsApi = retrofitJobs.create(JobsApi::class.java)

    interface JobsApi {

        @GET("recententries")
        suspend fun recentEntries(@Header("Authorization") token: String): List<RecentEntryRemote>

        @GET("timesheets/{tsId}")
        suspend fun getRecentEntry(@Header("Authorization") token : String, @Path("tsId") tsId : String) : ReceivedTimeSheet

        @POST("timesheets")
        suspend fun editTimesheet(
            @Header("Authorization") token: String,
            @Body body: EditingTimeSheet?
        )

        @DELETE("timesheets/{tsId}")
        suspend fun deleteTimesheet(
            @Header("Authorization") token: String,
            @Path("tsId") tsId: String
        )

        @GET("livejobs")
        suspend fun getJobs(@Header("Authorization") token: String): List<LiveJobRemote>

        @POST("timesheets")
        suspend fun postTimesheet(
            @Header("Authorization") token: String,
            @Body body: TimeSheetEntry
        )

        @GET("jobdetails/{jobId}")
        suspend fun getJobDetails(
            @Header("Authorization") token: String,
            @Path("jobId") jobId: String
        ): LiveJobRemote

        @GET("jobtimestatus")
        suspend fun getJobTimeStatus(@Header("Authorization") token: String) : List<JobTimeStatusRemote>

        @POST("new")
        suspend fun postJob(
            @Header("Authorization") token: String,
            @Body body: CreateJob
        )

    }
}