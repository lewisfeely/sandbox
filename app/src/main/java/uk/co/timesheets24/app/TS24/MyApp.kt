package uk.co.timesheets24.app.TS24

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.ui.graphics.Color
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import uk.co.timesheets24.app.TS24.Models.RemoteData.PermissionRemote
import uk.co.timesheets24.app.TS24.Models.RemoteData.ProfileRemote
import uk.co.timesheets24.app.TS24.Models.RemoteData.RecentEntryRemote
import uk.co.timesheets24.app.TS24.R
import uk.co.timesheets24.app.TS24.UI.theme.TSDarkBlue
import uk.co.timesheets24.app.TS24.UI.theme.TSSecondary
import java.security.KeyStore
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager
import kotlin.apply
import kotlin.collections.first

object GlobalLookUp {

    var Permissions : List<PermissionRemote>? = null
    var contactId : String = ""
    var selectedTab : Int = 0
    var offlineTimeSheetId : Int = 0
    var userState : ProfileRemote? = null
    lateinit var timeId : String
    var token : String? = null
    var refresh_token : String? = null
    var recentEntries : List<RecentEntryRemote>? = null

    fun getUnsafeOkHttpClient(): OkHttpClient {
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            override fun checkClientTrusted(
                chain: Array<out X509Certificate>?,
                authType: String?
            ) {}

            override fun checkServerTrusted(
                chain: Array<out X509Certificate>?,
                authType: String?
            ) {}

            override fun getAcceptedIssuers() = arrayOf<X509Certificate>()
        })

        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, java.security.SecureRandom())

        val sslSocketFactory = sslContext.socketFactory

        return OkHttpClient.Builder()
            .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            .hostnameVerifier{ _, _ -> true }
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

    val primaryColor = TSDarkBlue
    val secondary  = TSSecondary
    val tertiary = Color.DarkGray
    val background = TSDarkBlue
    val onPrimary = Color.White
    val onSecondary = Color.White
    val onTertiary = Color.White
    val error = Color.Red


//    val fileSend = files.sendFile("Bearer ${AppState.token}", prepareFilePart(AppState.imageFile!!))

}
