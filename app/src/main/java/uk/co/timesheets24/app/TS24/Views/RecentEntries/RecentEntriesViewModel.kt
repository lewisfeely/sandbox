package uk.co.timesheets24.app.TS24.Views.RecentEntries

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import uk.co.timesheets24.app.TS24.API.JobsApiClass
import uk.co.timesheets24.app.TS24.GlobalLookUp
import uk.co.timesheets24.app.TS24.LocalDataBase.LocalUserDatabase
import uk.co.timesheets24.app.TS24.LocalDataSevice.RefreshLocalData
import uk.co.timesheets24.app.TS24.Models.EditingTimeSheet
import uk.co.timesheets24.app.TS24.Models.LocalData.RecentEntryLocal
import uk.co.timesheets24.app.TS24.Models.RemoteData.RecentEntryRemote
import uk.co.timesheets24.app.TS24.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.collections.forEach
import kotlin.to

class RecentEntriesViewModel : ViewModel() {

    val fontAwesomeSolid = FontFamily(
        Font(R.font.fontawesome6freesolid900)  // refer to your font resource here
    )

    val loading = mutableStateOf(false)
    var recentEntriesList = mutableListOf<RecentEntryLocal>()
    val dialog = mutableStateOf(false)

    fun fetchRecentEntries(context: Context) {
        viewModelScope.launch {
            loading.value = true
            recentEntriesList.clear()
            val instance = LocalUserDatabase.getInstance(context.applicationContext)
            val recentEntryDao = instance.recentEntriesDao()
            recentEntriesList = recentEntryDao.fetchEntries().toMutableList()
            loading.value = false
            dialog.value = false
        }
    }

    fun viewTimeSheet(context: Context, recentEntry : RecentEntryLocal) {
        val timesheetApi = JobsApiClass(context).jobs

        viewModelScope.launch {
            try {
                val timesheet = timesheetApi.getRecentEntry("Bearer ${GlobalLookUp.token}", recentEntry.timeId)
                // navigate to edit time sheets page here and pass the timeid or temp id if offline and make the entries list globally accessable
//                val intent = Intent(context, )

            } catch (e : Exception) {

                println("RESPONSE $e failed inside view timesheet")

            }


        }
    }

    fun deleteRecentEntry(context: Context, recentEntry : RecentEntryLocal) {
        loading.value = true
        val jobs = JobsApiClass(context).jobs

        viewModelScope.launch {
            try {
                    jobs.deleteTimesheet("Bearer ${GlobalLookUp.token}", recentEntry.timeId.toString())
                    val instance = LocalUserDatabase.getInstance(context.applicationContext)
                    val recentEntryDao = instance.recentEntriesDao()
                    recentEntryDao.clearEntries()
                    RefreshLocalData(context)
                    dialog.value = false
            } catch (e : Exception) {
                println("RESPONSE $e error in delete timesheet")
                dialog.value = false
            }
            loading.value = false
        }
    }

}