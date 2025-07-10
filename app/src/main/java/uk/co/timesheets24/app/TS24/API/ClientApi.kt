package uk.co.timesheets24.app.TS24.API

import android.content.Context
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import uk.co.timesheets24.app.TS24.BuildConfig
import uk.co.timesheets24.app.TS24.GlobalLookUp
import uk.co.timesheets24.app.TS24.Models.ClientDetails
import uk.co.timesheets24.app.TS24.Models.RemoteData.LiveJobRemote

class ClientApiClass(context : Context) {
    val retrofitClients: Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.CLIENT_URL)
        .client(if(BuildConfig.AUTH_BASE_URL.contains("10.0.2.2")) GlobalLookUp.getUnsafeOkHttpClient() else GlobalLookUp.getSafeOkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val Clients = retrofitClients.create(ClientsApi::class.java)

    interface ClientsApi {

        @GET("search/{search}")
        suspend fun fetchClients(@Header("Authorization") token : String, @Path("search") search : String) : List<ClientDetails>

        @GET("details/{clientId}")
        suspend fun getClientDetails(@Header("Authorization") token : String, @Path("clientId") clientId : String) : ClientDetails

    }
}