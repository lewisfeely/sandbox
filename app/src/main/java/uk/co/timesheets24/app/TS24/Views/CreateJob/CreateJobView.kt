package uk.co.timesheets24.app.TS24.Views.CreateJob

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.DatePicker
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.unit.dp
import uk.co.timesheets24.app.TS24.UI.theme.TS24Theme
import uk.co.timesheets24.app.TS24.UI.theme.TSDarkBlue
import uk.co.timesheets24.app.TS24.Views.DashboardContainer.DashboardView
import java.util.Calendar
import kotlin.collections.forEach
import kotlin.jvm.java

class CreateJobView : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context : Context = this
            val viewModel = CreateJobViewModel()

            viewModel.fetchClients(context)

            TS24Theme {
                CreateJobScreen(context, viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateJobScreen(context: Context, viewModel : CreateJobViewModel) {

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            val date = "$year-${if (month >= 10) month + 1 else "0${month + 1}"}-${if (dayOfMonth >= 10) dayOfMonth + 1 else "0${dayOfMonth + 1}"}"
            viewModel.selectedDate.value = date
            viewModel.dateSelected = true
        },
        viewModel.calendar.get(Calendar.YEAR),
        viewModel.calendar.get(Calendar.MONTH),
        viewModel.calendar.get(Calendar.DAY_OF_MONTH)
    )


    Column (Modifier.fillMaxSize().background(color = TSDarkBlue), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
        Text("create Job", color = Color.White)

        if (!viewModel.loading.value) {


            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Client", color = Color.White)
                ExposedDropdownMenuBox(
                    expanded = viewModel.expanded.value,
                    onExpandedChange = {
                        viewModel.expanded.value = !viewModel.expanded.value
                    }
                ) {
                    OutlinedTextField(
                        readOnly = true,
                        value = viewModel.selected.value?.clientName ?: "select a client",
                        onValueChange = {},
                        modifier = Modifier
                            .width(250.dp)
                            .height(50.dp)
                            .menuAnchor()
                            .border(1.dp, Color.White),
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = viewModel.expanded.value)
                        },
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.LightGray,
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.LightGray,
                            disabledTextColor = Color.Gray,
                            cursorColor = Color.White
                        )
                    )

                    ExposedDropdownMenu(
                        expanded = viewModel.expanded.value,
                        onDismissRequest = { viewModel.expanded.value = false }
                    ) {
                        viewModel.clientsList.forEach { client ->
                            DropdownMenuItem(
                                text = { Text(client.clientName) },
                                onClick = {
                                    viewModel.selected.value = client
                                    viewModel.expanded.value = false
                                }
                            )
                        }
                    }
                }
            }
        } else {
            Text("Loading Clients...", color = Color.White)
            CircularProgressIndicator(color = Color.White)
        }

        Spacer(Modifier.height(10.dp))
        Row (Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){
            Text("job date", color = Color.White)
                Column (
                    modifier = Modifier.testTag("start_time").clickable{
                        datePickerDialog.show()
                    }.border(width = 1.dp, color = Color.White).width(250.dp).height(50.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Row {
                        Spacer(Modifier.width(15.dp))
                        Text(viewModel.selectedDate.value , color = Color.White)
                    }
                }
        }
        Spacer(Modifier.height(10.dp))
        Row (Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){
            Text("description", color = Color.White)
            TextField(
                value = viewModel.description.value,
                onValueChange = { viewModel.description.value = it },
                placeholder = { Text("description") },
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
        Row (Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){
            Text("notes", color = Color.White)
            TextField(
                value = viewModel.notes.value,
                onValueChange = { viewModel.notes.value = it },
                placeholder = { Text("notes") },
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
        Row (Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){
            Text("quote ref", color = Color.White)
            TextField(
                value = viewModel.quoteRef.value,
                onValueChange = { viewModel.quoteRef.value = it },
                placeholder = { Text("quote ref") },
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

        Button(onClick = {

//            if (selectedDate.value != "select a date" && selected.value != null) {
//
//                val createJob = CreateJob(
//                    clientId = selected.value!!.clientId,
//                    siteId = "siteId",
//                    clientContactID = selected.value!!.contacts[0].contactID,
//                    description = description.value,
//                    poNumber = "",
//                    jobDate = selectedDate.value,
//                    note = notes.value,
//                    quoteRef = quoteRef.value,
//                    isQuote = isQuote.value
//                )
//
//                viewModel.createJob(createJob, context)
//
//            } else if (selected.value == null && selectedDate.value == "") {
//                clientSelected = false
//                dateSelected = false
//            } else if (selected.value == null) {
//                clientSelected = false
//            } else if (selectedDate.value == "") {
//                dateSelected = false
//            }

            viewModel.createJob(context)
        }) {
            Text("create job")
        }


        Button(onClick = {

//            selected.value = null
//            description.value = ""
//            selectedDate.value = ""
//            notes.value = ""
//            quoteRef.value = ""
//            isQuote.value = false
//
            val intent = Intent(context, DashboardView::class.java)
            context.startActivity(intent)
        }) {
            Text("back to dashboard")
        }
    }

}