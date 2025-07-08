package uk.co.timesheets24.app.TS24.Views.Login

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
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
import uk.co.timesheets24.app.TS24.LocalDataBase.LocalUserDatabase
import uk.co.timesheets24.app.TS24.LocalDataSevice.RefreshLocalData
import uk.co.timesheets24.app.TS24.Models.LocalData.AccessTokenLocal
import uk.co.timesheets24.app.TS24.Views.Dashboard.DashboardView
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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
        val accessDao = LocalUserDatabase.getInstance(context.applicationContext).accessTokenDao()


        _loading.value = true
        val authApi = AuthApiClass(context).authApi


        viewModelScope.launch {
            try {
                state.value = "authenticating..."
                val logInResponse = authApi.authentication(email, password)
                GlobalLookUp.token = logInResponse.access_token
                GlobalLookUp.refresh_token = logInResponse.refresh_token

                accessDao.clear()
                accessDao.insert(AccessTokenLocal( accessToken =  logInResponse.access_token, refreshToken = logInResponse.refresh_token ))

                state.value = "navigating..."

                val intent = Intent(context, DashboardView::class.java)
                context.startActivity(intent)
                _loading.value = false
            } catch (e : Exception) {
                println("RESPONSE $e error inside login")
                _loading.value = false
                _error.value = true
            }
        }
    }

    fun checkRefreshToken(context: Context){



        _loading.value = true
        val authApi = AuthApiClass(context).authApi



        viewModelScope.launch {
            try {
                val accessToken = LocalUserDatabase.getInstance(context.applicationContext).accessTokenDao().fetch()

                if (accessToken.refreshToken == null || accessToken.refreshToken == "") {
                    _loading.value = false
                }
                else {


                    state.value = "authenticating..."
                    val logInResponse =
                        authApi.RefreshToken("refresh_token", GlobalLookUp.refresh_token.toString())

                    GlobalLookUp.token = logInResponse.access_token
                    GlobalLookUp.refresh_token = logInResponse.refresh_token
                    state.value = "navigating..."

                    val intent = Intent(context, DashboardView::class.java)
                    context.startActivity(intent)
                    _loading.value = false
                }

            } catch (e: Exception) {
                println("RESPONSE $e error inside login")
                _loading.value = false
                _error.value = true
            }
        }

    }

}