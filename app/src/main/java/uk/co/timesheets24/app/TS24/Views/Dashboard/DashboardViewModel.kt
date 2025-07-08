package uk.co.timesheets24.app.TS24.Views.Dashboard

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.google.gson.Gson
import kotlinx.coroutines.launch
import uk.co.timesheets24.app.TS24.API.AccountMIApiClass
import uk.co.timesheets24.app.TS24.API.AuthApiClass
import uk.co.timesheets24.app.TS24.GlobalLookUp
import uk.co.timesheets24.app.TS24.LocalDataBase.LocalUserDatabase
import uk.co.timesheets24.app.TS24.LocalDataSevice.RefreshLocalData
import uk.co.timesheets24.app.TS24.Models.LocalData.DashboardLocal
import uk.co.timesheets24.app.TS24.Models.RemoteData.DashboardRemote
import uk.co.timesheets24.app.TS24.Models.RemoteData.DashboardRequestRemote
import uk.co.timesheets24.app.TS24.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DashboardViewModel : ViewModel() {

    val timeSheets = mutableStateOf(GlobalLookUp.recentEntries)

    val error = mutableStateOf(false)

    val loading = mutableStateOf(false)

    val loaded = mutableStateOf(false)

    val state = mutableStateOf("")

    val navigationPopUp = mutableStateOf(false)

    val loadingUserHours = mutableStateOf(true)

    private val _userHours = mutableStateOf<DashboardLocal?>(null)
    val userHours : State<DashboardLocal?> = _userHours

    val fontAwesomeSolid = FontFamily(
        Font(R.font.fontawesome6freesolid900)  // refer to your font resource here
    )


    fun convertMinutesToHoursAndMinutes(totalMinutes: Int?): String {
        val hours = totalMinutes?.div(60)
        val minutes = totalMinutes?.rem(60)
        return "${hours}h ${minutes}m"
    }

    fun permissionsCheck() : Boolean {

        GlobalLookUp.Permissions?.forEach { permission ->
            if (permission.permissionID == "16118F40-404A-466A-A804-1F1647C97043") {
                return true
            }

        }
        return false

    }

    fun syncCheck(context : Context)  {

    }

    fun syncData(context : Context) {

    }


    fun fetchJobDetails(context: Context) {
        val instance = LocalUserDatabase.getInstance(context.applicationContext)
        val dashboardDao = instance.dashboardDao()

        viewModelScope.launch {
            try {
                _userHours.value = dashboardDao.fetch()

            } catch (e : Exception) {
                println("RESPONSE $e inside dashboard details fetch")
            }
        }



    }

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

//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun getTodayAndThreeMonthsAgo(): Pair<String, String> {
//        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-ddZHH:mm:ss")
//
//        val today = LocalDateTime.now()
//        val threeMonthsAgo = today.minusMonths(3)
//
//        val todayFormatted = today.format(formatter)
//        val threeMonthsAgoFormatted = threeMonthsAgo.format(formatter)
//
//        return Pair(todayFormatted, threeMonthsAgoFormatted)
//    }



}