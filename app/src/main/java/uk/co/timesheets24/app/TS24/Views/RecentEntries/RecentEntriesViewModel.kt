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
import uk.co.timesheets24.app.TS24.Models.EditingTimeSheet
import uk.co.timesheets24.app.TS24.Models.RemoteData.RecentEntryRemote
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.collections.forEach
import kotlin.to

class RecentEntriesViewModel : ViewModel() {

    val loading = mutableStateOf(false)

    val isLoading = mutableStateOf(false)

    val _timeSheetId = mutableIntStateOf(0)
    val timeSheetId : State<Int> = _timeSheetId

    val recentEntriesList = mutableListOf<RecentEntryRemote>()

    val dialog = mutableStateOf(false)

    val _loaded = mutableStateOf(false)
    val loaded : State<Boolean>  = _loaded

    val _editing = mutableStateOf(false)
    val editing : State<Boolean>  = _editing

    val _jobEditing = mutableStateOf("")
    val jobEditing : State<String> = _jobEditing

    val _jobDate = mutableStateOf("")
    val jobDate : State<String> = _jobDate

    val _description = mutableStateOf("")
    val description : State<String> = _description

    val _startTime = mutableStateOf("")
    val startTime : State<String> = _startTime

    val _endTime = mutableStateOf("")
    val endTime : State<String> = _endTime

    val _materials = mutableStateOf("")
    val materials : State<String> = _materials

    val _travelTime = mutableIntStateOf(0)
    val travelTime : State<Int> = _travelTime

    val _overTime = mutableIntStateOf(0)
    val overTime : State<Int> = _overTime


    fun fetchRecentEntries(context: Context) {


        isLoading.value = true
        recentEntriesList.clear()
        val instance = LocalUserDatabase.getInstance(context.applicationContext)
        val recentEntryDao = instance.recentEntriesDao()



    }

    fun selectedJob(context: Context) {


    }

    fun editRecentEntry(context: Context, currentTimeSheet: EditingTimeSheet?) {

        val workManager = WorkManager.getInstance(context.applicationContext)



        viewModelScope.launch {


        }
    }

    fun deleteRecentEntry(context: Context, timeId : String, description : String) {




    }

    fun addOfflineData(context : Context) {



    }

}