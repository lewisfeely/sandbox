package uk.co.timesheets24.app.TS24.Views.Dashboard

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import uk.co.timesheets24.app.TS24.API.AuthApiClass
import uk.co.timesheets24.app.TS24.GlobalLookUp
import uk.co.timesheets24.app.TS24.LocalDataBase.LocalUserDatabase
import uk.co.timesheets24.app.TS24.LocalDataSevice.RefreshLocalData
import uk.co.timesheets24.app.TS24.Models.LocalData.DashboardLocal
import uk.co.timesheets24.app.TS24.R

class DashboardViewModel : ViewModel() {

    val lifecycleOwner = mutableStateOf<LifecycleOwner?>(null)

    val error = mutableStateOf(false)

    val loading = mutableStateOf(false)

    val state = mutableStateOf("")

    val _userHours = mutableStateOf<DashboardLocal?>(null)
    val userHours : State<DashboardLocal?> = _userHours

    val fontAwesomeSolid = FontFamily(
        Font(R.font.fontawesome6freesolid900)  // refer to your font resource here
    )

    fun fetchJobDetails(context: Context, localUserDatabase: LocalUserDatabase) {
        val dashboardDao = localUserDatabase.dashboardDao()

        viewModelScope.launch {
            try {
                loading.value = true
                _userHours.value = dashboardDao.fetch()
                loading.value = false
            } catch (e : Exception) {
                println("RESPONSE $e inside dashboard details fetch")
            }
        }
    }

//    fun syncData(context : Context) {
//        viewModelScope.launch {
//            try {
//                val authApi = AuthApiClass(context).authApi
//                val board = RefreshLocalData(context)
//                board.refreshDescription.observe(lifecycleOwner.value!!) {newValue ->
//                    state.value = newValue
//                }
//                val response = authApi.authentication("mike.feely@outlook.com", "London2016#")
//                GlobalLookUp.token = response.access_token
//                board.DoWork()
//                fetchJobDetails(context, LocalUserDatabase.getInstance(context.applicationContext))
//                loading.value = false
//
//            } catch (e: Exception) {
//                println("RESPONSE $e Inside Login ViewModel")
//                error.value = true
//                loading.value = false
//
//            }
//        }
//    }

    fun convertMinutesToHoursAndMinutes(totalMinutes: Int?): String {
        val hours = totalMinutes?.div(60)
        val minutes = totalMinutes?.rem(60)
        return "${hours}h ${minutes}m"
    }
}