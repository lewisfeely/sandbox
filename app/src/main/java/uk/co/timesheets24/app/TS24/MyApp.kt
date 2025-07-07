package uk.co.timesheets24.app.TS24

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import uk.co.timesheets24.app.TS24.Models.RemoteData.ProfileRemote
import uk.co.timesheets24.app.TS24.Models.RemoteData.RecentEntryRemote
import uk.co.timesheets24.app.timesheetssandbox.R
import java.security.KeyStore
import java.security.cert.CertificateFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager
import kotlin.apply
import kotlin.collections.first

object GlobalLookUp {


    var contactId : String = ""
    var selectedTab : Int = 0
    var offlineTimeSheetId : Int = 0
    var userState : ProfileRemote? = null
    var token : String? = null
    var recentEntries : List<RecentEntryRemote>? = null

    fun getUnsafeOkHttpClient(context: Context): OkHttpClient {
        val certificateFactory = CertificateFactory.getInstance("X.509")

        val inputStream =
            context.resources.openRawResource(R.raw.certificate)
        val certificate = certificateFactory.generateCertificate(inputStream)
        inputStream.close()

        val keyStore = KeyStore.getInstance(KeyStore.getDefaultType()).apply {
            load(null, null)
            setCertificateEntry("ssl", certificate)
        }

        val trustManagerFactory = TrustManagerFactory.getInstance(
            TrustManagerFactory.getDefaultAlgorithm()
        ).apply {
            init(keyStore)
        }

        val trustManager = trustManagerFactory.trustManagers
            .first { it is X509TrustManager } as X509TrustManager

        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, arrayOf(trustManager), null)

        return OkHttpClient.Builder()
            .sslSocketFactory(sslContext.socketFactory, trustManager)
            .hostnameVerifier { hostname, _ -> hostname == "10.0.2.2" }
            .connectionSpecs(listOf(ConnectionSpec.MODERN_TLS))
            .build()



    }

    fun getSafeOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .build()
    }

    fun hasInternetAccess(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }


//    val fileSend = files.sendFile("Bearer ${AppState.token}", prepareFilePart(AppState.imageFile!!))

}
