package uk.co.timesheets24.app.TS24.Views.CreateTimesheet

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import uk.co.timesheets24.app.TS24.API.JobsApiClass
import uk.co.timesheets24.app.TS24.GlobalLookUp
import uk.co.timesheets24.app.TS24.Models.TimeSheetEntry
import uk.co.timesheets24.app.TS24.Views.DashboardContainer.DashboardView
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.jvm.java
import kotlin.to

class CreateTimesheetViewModel : ViewModel() {

    val startCalendar = Calendar.getInstance()
    val finishCalendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    val jobDescription = mutableStateOf("")
    val timeToComplete = mutableStateOf("0")
    val overTime = mutableStateOf("0")

    val clientName = mutableStateOf("")

    val imageUri = mutableStateOf<Uri?>(null)

    val imageFile = mutableStateOf<File?>(null)

    val viewImage = mutableStateOf(false)

    val startingSelectedDateTime = mutableStateOf("")

    val finishSelectedDateTime = mutableStateOf("")

    fun sendTimeSheet(context: Context) {
        val jobApi = JobsApiClass(context).jobs
        viewModelScope.launch {
            try {
                val finishTime = finishSelectedDateTime.value + "+01:00"
                val finishDate = finishSelectedDateTime.value + ":00.000Z"
                val finishDateFormatted = finishDate.replace(" ", "T")
                val finishFormatted = finishTime.replace(" ", "T")

                val startTime = startingSelectedDateTime.value + "+01:00"
                val startDate = startingSelectedDateTime.value + ":00.000Z"
                val startDateFormatted = startDate.replace(" ", "T")
                val startFormatted = startTime.replace(" ", "T")

                val timeSheet = TimeSheetEntry(jobGUID = GlobalLookUp.selectedJob?.jobGUID, timeSheetId = "", contactId = GlobalLookUp.contactId,
                    description = jobDescription.value, materials = "none", startDate = startDateFormatted,
                    startTime = startFormatted, endDate = finishDateFormatted, endTime = finishFormatted,
                    timetake = timeToComplete.value.toInt(), overTime = overTime.value.toInt(), travellingTime = 100
                )
                println("RESPONSE Request sent $timeSheet")

                jobApi.postTimesheet("Bearer ${GlobalLookUp.token}", timeSheet)

                GlobalLookUp.selectedJob = null

                val intent = Intent(context , DashboardView::class.java)
                context.startActivity(intent)


            } catch (e : Exception) {

                println("RESPONSE $e failed timesheet post")

            }

        }

    }
}