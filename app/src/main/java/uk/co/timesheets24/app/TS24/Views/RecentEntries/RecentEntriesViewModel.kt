package uk.co.timesheets24.app.TS24.Views.RecentEntries

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import uk.co.timesheets24.app.TS24.GlobalLookUp
import uk.co.timesheets24.app.TS24.LocalDataBase.LocalUserDatabase
import uk.co.timesheets24.app.TS24.LocalDataSevice.RefreshLocalData
import uk.co.timesheets24.app.TS24.Models.EditingTimeSheet
import uk.co.timesheets24.app.TS24.Models.LocalData.RecentEntryLocal
import uk.co.timesheets24.app.TS24.Models.RemoteData.RecentEntryRemote
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.collections.forEach
import kotlin.to

class RecentEntriesViewModel : ViewModel() {

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
        }
    }

    fun editRecentEntry(context: Context, currentTimeSheet: EditingTimeSheet?) {
        val workManager = WorkManager.getInstance(context.applicationContext)

        viewModelScope.launch {

        }
    }

    fun deleteRecentEntry(context: Context, timeId : String, description : String) {

    }

}