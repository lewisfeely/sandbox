package uk.co.timesheets24.app.TS24.Views.EditRecentEntry

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.tehras.charts.piechart.PieChart
import com.github.tehras.charts.piechart.PieChartData
import com.github.tehras.charts.piechart.PieChartData.Slice
import com.github.tehras.charts.piechart.animation.simpleChartAnimation
import com.github.tehras.charts.piechart.renderer.SimpleSliceDrawer
import com.google.gson.Gson
import uk.co.timesheets24.app.TS24.GlobalLookUp
import uk.co.timesheets24.app.TS24.Models.EditingTimeSheet
import uk.co.timesheets24.app.TS24.UI.theme.TS24Theme
import uk.co.timesheets24.app.TS24.UI.theme.TSDarkBlue
import uk.co.timesheets24.app.TS24.Views.DashboardContainer.DashboardView
import uk.co.timesheets24.app.TS24.Views.RecentEntries.RecentEntriesView
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale
import kotlin.jvm.java
import kotlin.text.replace
import kotlin.text.toInt
import kotlin.toString

@RequiresApi(Build.VERSION_CODES.O)
class EditRecentEntryView : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel = EditRecentEntriesViewModel()
            val context : Context = this
            viewModel.fetchEntry(context)
            TS24Theme {
                EditRecentEntryScreen(context, viewModel)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EditRecentEntryScreen(context: Context, viewModel: EditRecentEntriesViewModel) {
    val scrollState = rememberScrollState()

    if (viewModel.loading.value || viewModel.startTime.value == "") {
        Column (modifier = Modifier.fillMaxSize().background(color = TSDarkBlue), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
            CircularProgressIndicator(color = Color.White)
            Text("fetching info...", color = Color.White)
        }
    } else {

        val finishDatePickerDialog = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                viewModel.finishCalendar.set(Calendar.YEAR, year)
                viewModel.finishCalendar.set(Calendar.MONTH, month)
                viewModel.finishCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                // After date is selected, show time picker
                TimePickerDialog(
                    context,
                    { _, hour, minute ->
                        viewModel.finishCalendar.set(Calendar.HOUR_OF_DAY, hour)
                        viewModel.finishCalendar.set(Calendar.MINUTE, minute)
                        viewModel.endTime.value = viewModel.dateFormat.format(viewModel.finishCalendar.time)
                    },
                    viewModel.finishCalendar.get(Calendar.HOUR_OF_DAY),
                    viewModel.finishCalendar.get(Calendar.MINUTE),
                    true
                ).show()
            },
            viewModel.finishCalendar.get(Calendar.YEAR),
            viewModel.finishCalendar.get(Calendar.MONTH),
            viewModel.finishCalendar.get(Calendar.DAY_OF_MONTH)
        )
        val startDatePickerDialog = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                viewModel.startCalendar.set(Calendar.YEAR, year)
                viewModel.startCalendar.set(Calendar.MONTH, month)
                viewModel.startCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                // After date is selected, show time picker
                TimePickerDialog(
                    context,
                    { _, hour, minute ->
                        viewModel.startCalendar.set(Calendar.HOUR_OF_DAY, hour)
                        viewModel.startCalendar.set(Calendar.MINUTE, minute)
                        viewModel.startTime.value = viewModel.dateFormat.format(viewModel.startCalendar.time)
                    },
                    viewModel.startCalendar.get(Calendar.HOUR_OF_DAY),
                    viewModel.startCalendar.get(Calendar.MINUTE),
                    true
                ).show()
            },
            viewModel.startCalendar.get(Calendar.YEAR),
            viewModel.startCalendar.get(Calendar.MONTH),
            viewModel.startCalendar.get(Calendar.DAY_OF_MONTH)
        )


    Column(
        Modifier.fillMaxWidth().height(1400.dp).background(TSDarkBlue).verticalScroll(scrollState),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Spacer(Modifier.height(80.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround

        ) {
            Column() {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier.size(20.dp)
                            .background(color = Color(0XFFb33715), shape = CircleShape)
                    ) {}
                    Spacer(Modifier.width(10.dp))
                    Text("overTime", color = Color.White)
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier.size(20.dp)
                            .background(color = Color(0XFF67c26e), shape = CircleShape)
                    ) {}
                    Spacer(Modifier.width(10.dp))
                    Text("travel time", color = Color.White)
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier.size(20.dp)
                            .background(color = Color(0XFF7f8ddb), shape = CircleShape)
                    ) {}
                    Spacer(Modifier.width(10.dp))
                    Text("time worked", color = Color.White)
                }
            }
            Box(Modifier.size(150.dp)) {
                PieChart(
                    pieChartData = PieChartData(
                        listOf(
                            Slice(
                                if (viewModel.overTime.value != "") {
                                viewModel.overTime.value.toFloat()
                                } else {
                                    0f
                                }
                                ,
                                Color(0XFFb33715)
                            ), Slice(
                                if (viewModel.travelTime.value != "") {
                                    viewModel.travelTime.value.toFloat()
                                } else {
                                    0f
                                },
                                Color(0XFF67c26e)
                            ),
                            Slice(
                                if (viewModel.timeTaken.value != "") {
                                    viewModel.timeTaken.value.toFloat()
                                } else {
                                    0f
                                },
                                Color(0XFF7f8ddb)
                            )
                        )
                    ),
                    animation = simpleChartAnimation(),
                    sliceDrawer = SimpleSliceDrawer(sliceThickness = 30F)
                )
            }
        }
        Spacer(Modifier.height(70.dp))
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("description", color = Color.White)
            TextField(
                value = viewModel.description.value.toString(),
                onValueChange = { viewModel.description.value = it },
                placeholder = { Text("description") },
                label = { Text("description") },
                modifier = Modifier.width(250.dp)
                    .border(width = 1.dp, color = MaterialTheme.colorScheme.onPrimary)
                    .testTag("email_Field"),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = MaterialTheme.colorScheme.background,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
                    cursorColor = Color.Blue,
                    focusedLabelColor = Color.Blue,
                    unfocusedPlaceholderColor = Color.White,
                    unfocusedLabelColor = Color.White
                )
            )
        }
        Spacer(Modifier.height(10.dp))
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("materials", color = Color.White)
            TextField(
                value = viewModel.materials.value.toString(),
                onValueChange = { viewModel.materials.value = it },
                placeholder = { Text("materials") },
                label = { Text("materials") },
                modifier = Modifier.width(250.dp)
                    .border(width = 1.dp, color = MaterialTheme.colorScheme.onPrimary)
                    .testTag("email_Field"),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = MaterialTheme.colorScheme.background,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
                    cursorColor = Color.Blue,
                    focusedLabelColor = Color.Blue,
                    unfocusedPlaceholderColor = Color.White,
                    unfocusedLabelColor = Color.White
                )
            )
        }
        Spacer(Modifier.height(10.dp))
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("overTime", color = Color.White)
            TextField(
                value = viewModel.overTime.value.toString(),
                onValueChange = { viewModel.overTime.value = it },
                placeholder = { Text("over Time") },
                label = { Text("over Time") },
                modifier = Modifier.width(250.dp)
                    .border(width = 1.dp, color = MaterialTheme.colorScheme.onPrimary)
                    .testTag("email_Field"),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = MaterialTheme.colorScheme.background,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
                    cursorColor = Color.Blue,
                    focusedLabelColor = Color.Blue,
                    unfocusedPlaceholderColor = Color.White,
                    unfocusedLabelColor = Color.White
                )
            )
        }
        Spacer(Modifier.height(10.dp))
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("travelTime", color = Color.White)
            TextField(
                value = viewModel.travelTime.value.toString(),
                onValueChange = { viewModel.travelTime.value = it },
                placeholder = { Text("travel Time") },
                label = { Text("travel Time") },
                modifier = Modifier.width(250.dp)
                    .border(width = 1.dp, color = MaterialTheme.colorScheme.onPrimary)
                    .testTag("email_Field"),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = MaterialTheme.colorScheme.background,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
                    cursorColor = Color.Blue,
                    focusedLabelColor = Color.Blue,
                    unfocusedPlaceholderColor = Color.White,
                    unfocusedLabelColor = Color.White
                )
            )
        }
        Spacer(Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
//
            Text(text = "start time", color = Color.White)
            Column(
                modifier = Modifier.testTag("start_time").clickable {
                    startDatePickerDialog.show()
                    viewModel.endTimeEntered.value = true
                }.border(width = 1.dp, color = Color.White).width(250.dp).height(50.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    viewModel.formatReadableDateTime(viewModel.startTime.value.toString()),
                    color = Color.White
                )
            }
        }
        Spacer(Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "finish time", color = Color.White)
            Column(
                modifier = Modifier.testTag("finish_time").clickable {
                    finishDatePickerDialog.show()
                    viewModel.endTimeEntered.value = true
                }.border(width = 1.dp, color = Color.White).width(250.dp).height(50.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    viewModel.formatReadableDateTime(viewModel.endTime.value.toString()),
                    color = Color.White
                )
            }
        }
            Button(onClick = {
                viewModel.editRecentEntry(context)
            }) {
                Text("save format")
            }
            Button(onClick = {
                viewModel.timesheet.value = null
                val intent = Intent(context, DashboardView::class.java)
                context.startActivity(intent)

            }) {
                Text("cancel edits")
            }
    }

        }

    }


//fun toEditingTimeSheet(source: ReceivedTimeSheet?): EditingTimeSheet? {
//    if (source == null) {
//        return null
//    } else {
//        return EditingTimeSheet(
//            timeSheetId = source.timeId,
//            contactName = source.contactName ?: "",
//            contactId = source.contactId,
//            description = source.description,
//            materials = source.materials ?: "",
//            overTime = source.overTime,
//            timetake = source.timetake.toString(),
//            travellingTime = source.travellingTime,
//            createdDate = source.createdDate,
//            modifiedDate = source.modifiedDate,
//            jobGUID = source.jobGUID,
//            startTime = source.startTime,
//            endTime = source.endTime,
//            jobTimeStatusId = source.jobTimeStatusId,
//            jobTimeStatus = source.jobTimeStatus
//        )
//    }
//}