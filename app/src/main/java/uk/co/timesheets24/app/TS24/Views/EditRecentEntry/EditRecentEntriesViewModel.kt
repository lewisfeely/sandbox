package uk.co.timesheets24.app.TS24.Views.EditRecentEntry

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import uk.co.timesheets24.app.TS24.API.AuthApiClass
import uk.co.timesheets24.app.TS24.API.JobsApiClass
import uk.co.timesheets24.app.TS24.GlobalLookUp
import uk.co.timesheets24.app.TS24.Models.EditingTimeSheet
import uk.co.timesheets24.app.TS24.Models.ReceivedTimeSheet
import uk.co.timesheets24.app.TS24.R
import uk.co.timesheets24.app.TS24.Views.DashboardContainer.DashboardView
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
class EditRecentEntriesViewModel : ViewModel() {

    val loading = mutableStateOf(false)

    val _loaded = mutableStateOf(false)
    val loaded: State<Boolean> = _loaded

    val _editing = mutableStateOf(false)
    val editing: State<Boolean> = _editing

    val timesheet = mutableStateOf<ReceivedTimeSheet?>(null)

    val timeTaken = mutableStateOf("0")
    val description = mutableStateOf("")
    val startTime = mutableStateOf("")
    val endTime = mutableStateOf("")
    val materials = mutableStateOf("")
    val overTime = mutableStateOf("")
    val travelTime = mutableStateOf("")

    val startTimeEntered = mutableStateOf(false)
    val endTimeEntered = mutableStateOf(false)
    val error = mutableStateOf(false)

    val fontAwesomeSolid = FontFamily(
        Font(R.font.fontawesome6freesolid900)  // refer to your font resource here
    )

    fun fetchEntry(context: Context) {

        loading.value = true
        val jobApi = JobsApiClass(context).jobs

        viewModelScope.launch {
            try {
                val timesheetReceived =
                    jobApi.getRecentEntry("Bearer ${GlobalLookUp.token}", GlobalLookUp.timeId)
                timesheet.value = timesheetReceived
                description.value = timesheet.value?.description.toString()
                startTime.value = timesheet.value?.startTime.toString()
                endTime.value = timesheet.value?.endTime.toString()
                materials.value = timesheet.value?.materials.toString()
                overTime.value = timesheet.value?.overTime.toString()
                travelTime.value = timesheet.value?.travellingTime.toString()
                loading.value = false
            } catch (e: Exception) {
                println("RESPONSE $e ERROR inside edit entry")
                loading.value = false
            }
        }


    }

    fun editRecentEntry(context: Context) {
        val jobApi = JobsApiClass(context).jobs

        viewModelScope.launch {
            try {
                loading.value = true

                val finishTime = endTime.value + if (endTimeEntered.value) ":00.00" else ".00"
                val finishFormatted = finishTime.replace(" ", "T")

                val startTime = startTime.value + if (startTimeEntered.value) ":00.00" else ".00"
                val startFormatted = startTime.replace(" ", "T")

                jobApi.editTimesheet(
                    "Bearer ${GlobalLookUp.token}", EditingTimeSheet(
                        sessionId = "",
                        timeSheetId = timesheet.value?.timeId.toString(),
                        contactName = timesheet.value?.contactName,
                        contactId = timesheet.value?.contactId.toString(),
                        description = description.value,
                        materials = materials.value,
                        overTime = overTime.value.toInt(),
                        timetake = timeTaken.value,
                        travellingTime = travelTime.value.toInt(),
                        createdDate = timesheet.value?.createdDate.toString(),
                        modifiedDate = "2025-07-09T12:00:00Z",
                        jobGUID = timesheet.value?.jobGUID,
                        startTime = startFormatted,
                        endTime = finishFormatted,
                        jobTimeStatusId = "status_02",
                        jobTimeStatus = "Completed"
                    )
                )

                val intent = Intent(context, DashboardView::class.java)
                context.startActivity(intent)

                loading.value = false

            } catch (e: Exception) {
                println("RESPONSE $e error in editting")
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun formatReadableDateTime(isoDateTime: String): String {
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val outputFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy, h:mm a", Locale.getDefault())

        val dateTime = LocalDateTime.parse(isoDateTime, inputFormatter)
        return dateTime.format(outputFormatter)
    }

    val startCalendar = Calendar.getInstance()
    val finishCalendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

}