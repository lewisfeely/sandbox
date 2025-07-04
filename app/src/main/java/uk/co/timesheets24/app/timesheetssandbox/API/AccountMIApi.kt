package uk.co.timesheets24.app.timesheetssandbox.API

import android.content.Context
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import uk.co.timesheets24.app.timesheetssandbox.BuildConfig
import uk.co.timesheets24.app.timesheetssandbox.GlobalLookUp
import uk.co.timesheets24.app.timesheetssandbox.Models.RemoteData.DashboardRemote
import uk.co.timesheets24.app.timesheetssandbox.Models.TimeSheetEntry

class AccountMIApiClass(context: Context) {

    val retrofitMIs: Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.ACCOUNTMI_URL)
        .client(
            if (BuildConfig.AUTH_BASE_URL.contains("10.0.2.2")) GlobalLookUp.getUnsafeOkHttpClient(
                context
            ) else GlobalLookUp.getSafeOkHttpClient()
        )        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val accountMI : AccountMiApi = retrofitMIs.create(AccountMiApi::class.java)

    interface AccountMiApi {

        @GET("userdashboard/{request}")
        suspend fun dashBoard(@Header("Authorization") token : String, @Path("request") request : String) : DashboardRemote

    }

}