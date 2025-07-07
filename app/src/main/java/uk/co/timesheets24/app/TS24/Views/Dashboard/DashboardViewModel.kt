package uk.co.timesheets24.app.TS24.Views.Dashboard

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import uk.co.timesheets24.app.TS24.API.AuthApiClass
import uk.co.timesheets24.app.TS24.GlobalLookUp
import uk.co.timesheets24.app.TS24.LocalDataSevice.RefreshLocalData

class DashboardViewModel : ViewModel() {

    val timeSheets = mutableStateOf(GlobalLookUp.recentEntries)

    val error = mutableStateOf(false)

    val loading = mutableStateOf(false)

    val loaded = mutableStateOf(false)

    val state = mutableStateOf("")

    fun syncData(context : Context, lifecycleOwner: LifecycleOwner) {
        viewModelScope.launch {
            try {

                val authApi = AuthApiClass(context).authApi
                val board = RefreshLocalData(context)
                board.refreshDescription.observe(lifecycleOwner) {newValue ->
                    state.value = newValue
                }
                val response = authApi.authentication("mike.feely@outlook.com", "London2016#")
                GlobalLookUp.token = response.access_token
                board.DoWork()
                loading.value = false

            } catch (e: Exception) {
                println("RESPONSE $e Inside Login ViewModel")
                error.value = true
                loading.value = false
            }
        }
    }
}