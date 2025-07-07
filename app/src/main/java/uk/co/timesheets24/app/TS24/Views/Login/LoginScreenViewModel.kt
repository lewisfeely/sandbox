package uk.co.timesheets24.app.TS24.Views.Login

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.google.gson.Gson
import kotlinx.coroutines.launch
import uk.co.timesheets24.app.TS24.API.AuthApiClass
import uk.co.timesheets24.app.TS24.API.ProfileApiClass
import uk.co.timesheets24.app.TS24.GlobalLookUp
import uk.co.timesheets24.app.TS24.Views.Dashboard.DashboardView
import kotlin.jvm.java
import kotlin.to

class LoginScreenViewModel: ViewModel() {

    val _loading = mutableStateOf(false)
    val loading : State<Boolean> = _loading

    val _loadingOffLog = mutableStateOf(false)
    val loadingOffLog : State<Boolean> = _loadingOffLog

    val _error = mutableStateOf(false)
    val error : State<Boolean> = _error

    val state = MutableLiveData<String>()

    fun processLogin(context: Context, email : String, password: String) {
        val authApi = AuthApiClass(context).authApi
        val profileApi = ProfileApiClass(context).profile

        viewModelScope.launch {
            try {
                state.value = "authenticating..."
                val logInResponse = authApi.authentication(email, password)
                state.value = "fetching account details"
                val accountDetails = profileApi.details(logInResponse.access_token)
                GlobalLookUp.userState = accountDetails
                state.value = "navigating..."
                val intent = Intent(context, DashboardView::class.java)
                context.startActivity(intent)

            } catch (e : Exception) {
                println("RESPONSE $e error inside login")
            }
        }


    }

    fun syncData(context : Context) {



    }

    fun offlineLogin(context : Context) {

    }

    fun onlineAutoLogin(context: Context) {

    }

}