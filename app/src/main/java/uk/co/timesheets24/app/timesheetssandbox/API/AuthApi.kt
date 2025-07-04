package uk.co.timesheets24.app.timesheetssandbox.API

import android.content.Context
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import uk.co.timesheets24.app.timesheetssandbox.BuildConfig
import uk.co.timesheets24.app.timesheetssandbox.GlobalLookUp
import uk.co.timesheets24.app.timesheetssandbox.Models.AuthResponse

class AuthApiClass(context: Context) {

    val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl(BuildConfig.AUTH_BASE_URL)
        .client(
            if (BuildConfig.AUTH_BASE_URL.contains("10.0.2.2")) GlobalLookUp.getUnsafeOkHttpClient(
                context
            ) else GlobalLookUp.getSafeOkHttpClient()
        )    .addConverterFactory(GsonConverterFactory.create())
    .build()

    val authApi : ApiService = retrofit.create(ApiService::class.java)

    interface ApiService {

    @FormUrlEncoded
    @POST("auth")
    suspend fun authentication(@Field("email") email: String,
                               @Field("password") password: String) : AuthResponse

    }

}