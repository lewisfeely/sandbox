package uk.co.timesheets24.app.timesheetssandbox.API

import android.content.Context
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import uk.co.timesheets24.app.timesheetssandbox.BuildConfig
import uk.co.timesheets24.app.timesheetssandbox.GlobalLookUp
import uk.co.timesheets24.app.timesheetssandbox.Models.RemoteData.PermissionRemote
import uk.co.timesheets24.app.timesheetssandbox.Models.RemoteData.ProfileRemote

class ProfileApiClass(context: Context) {

    val retrofitProfile: Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.PROFILE_URL)
        .client(
            if (BuildConfig.AUTH_BASE_URL.contains("10.0.2.2")) GlobalLookUp.getUnsafeOkHttpClient(
                context
            ) else GlobalLookUp.getSafeOkHttpClient()
        )
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val profile : ProfileApi = retrofitProfile.create(ProfileApi::class.java)

    interface ProfileApi {

        @GET("Details")
        suspend fun details(@Header("Authorization") token: String): ProfileRemote

        @GET("Permission")
        suspend fun permissions(@Header("Authorization") token: String): List<PermissionRemote>
    }

}