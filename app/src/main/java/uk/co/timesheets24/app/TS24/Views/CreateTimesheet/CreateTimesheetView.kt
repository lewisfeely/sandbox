package uk.co.timesheets24.app.TS24.Views.CreateTimesheet

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import uk.co.timesheets24.app.TS24.UI.theme.TS24Theme
import uk.co.timesheets24.app.TS24.UI.theme.TSDarkBlue
import uk.co.timesheets24.app.TS24.R
import uk.co.timesheets24.app.TS24.Views.Jobs.SelectJobView
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.UUID
import java.util.jar.Manifest
import kotlin.jvm.java
import kotlin.let
import kotlin.text.replace
import kotlin.text.toInt

class LogTimesheetView : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel : CreateTimesheetViewModel = viewModel()
            val context : Context = this

            TS24Theme {
                CreateTimesheetScreen(context, viewModel)
            }
        }
    }
}

@Composable
fun CreateTimesheetScreen(context: Context, viewModel: CreateTimesheetViewModel) {

    /*val photoUri = remember {
        val file = File(context.cacheDir, "camera_image.jpg")
        FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )
    }*/

//    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
//        if (success) {
//            imageUri.value = photoUri
//        }
//    }
//
//    val cameraPermissionLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.RequestPermission(),
//        onResult = { isGranted ->
//            if (isGranted) {
//                launcher.launch(photoUri)
//            } else {
//                println("RESPONSE CAMERA PERMISSION DENIED")
//            }
//        }
//    )

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
                    viewModel.startingSelectedDateTime.value =
                        viewModel.dateFormat.format(viewModel.startCalendar.time)
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
                    viewModel.finishSelectedDateTime.value =
                        viewModel.dateFormat.format(viewModel.finishCalendar.time)
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

    val scrollState = rememberScrollState()

    Column(
        Modifier.fillMaxSize().background(color = TSDarkBlue).verticalScroll(scrollState),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Spacer(Modifier.height(100.dp))
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.timesheets_blue),
                contentDescription = "ts24 logo",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(180.dp)
            )
        }
        Spacer(Modifier.height(40.dp))
        Spacer(Modifier.height(15.dp))


        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("select job", color = Color.White)
            Box(
                Modifier.width(250.dp).height(55.dp).clickable {
                    val intent = Intent(context, SelectJobView::class.java)
                    context.startActivity(intent)
                }.background(MaterialTheme.colorScheme.background)
                    .border(width = 1.dp, color = Color.White), contentAlignment = Alignment.Center
            ) {
//                Text(if (jobPosting.value == null) {
//                    "post without Job"
//                } else {
//                    "${jobPosting.value?.description} for ${jobPosting.value?.clientName}"
//                       }, color = Color.White)
//            }
            }

            Spacer(Modifier.height(15.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("start date", color = Color.White)
                Box(
                    Modifier.width(250.dp).height(55.dp).clickable {
                        startDatePickerDialog.show()
                    }.background(MaterialTheme.colorScheme.background)
                        .border(width = 1.dp, color = Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        if (viewModel.startingSelectedDateTime.value != "") viewModel.startingSelectedDateTime.value else "select start date",
                        color = Color.White
                    )
                }
            }

            Spacer(Modifier.height(15.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("finish date", color = Color.White)
                Box(
                    Modifier.width(250.dp).height(55.dp).clickable {
                        finishDatePickerDialog.show()
                    }.background(MaterialTheme.colorScheme.background)
                        .border(width = 1.dp, color = Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        if (viewModel.finishSelectedDateTime.value != "") viewModel.finishSelectedDateTime.value else "select end date",
                        color = Color.White
                    )
                }
            }



            Spacer(Modifier.height(15.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("description", color = Color.White)
                TextField(
                    value = viewModel.jobDescription.value,
                    onValueChange = { viewModel.jobDescription.value = it },
                    modifier = Modifier.width(250.dp).height(55.dp)
                        .border(width = 1.dp, color = MaterialTheme.colorScheme.onPrimary)
                        .testTag("email_Field"),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = MaterialTheme.colorScheme.background,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
                        cursorColor = Color.Blue,
                        focusedLabelColor = Color.Blue,
                        unfocusedPlaceholderColor = Color.White
                    )
                )
            }
            Spacer(Modifier.height(10.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("time to complete", color = Color.White)
                TextField(
                    value = viewModel.timeToComplete.value,
                    onValueChange = { viewModel.timeToComplete.value = it },
                    placeholder = { Text("time to Complete") },
                    modifier = Modifier.width(250.dp).height(55.dp)
                        .border(width = 1.dp, color = MaterialTheme.colorScheme.onPrimary)
                        .testTag("email_Field"),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = MaterialTheme.colorScheme.background,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
                        cursorColor = Color.Blue,
                        focusedLabelColor = Color.Blue,
                        unfocusedPlaceholderColor = Color.White
                    )
                )
            }
            Spacer(Modifier.height(10.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("over time", color = Color.White)
                TextField(
                    value = viewModel.overTime.value,
                    onValueChange = { viewModel.overTime.value = it },
                    placeholder = { Text("overtime") },
                    modifier = Modifier.width(250.dp).height(55.dp)
                        .border(width = 1.dp, color = MaterialTheme.colorScheme.onPrimary)
                        .testTag("email_Field"),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = MaterialTheme.colorScheme.background,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
                        cursorColor = Color.Blue,
                        focusedLabelColor = Color.Blue,
                        unfocusedPlaceholderColor = Color.White
                    )
                )
            }

//        if (imageUri.value == null) {
//            Spacer(Modifier.height(10.dp))
//            Button(onClick = { cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA) }) {
//                Text("attach image")
//            }
//        } else {
//            Button(onClick = {viewModel.viewImage.value = true}) {
//                Text("View Image")
//            }
//        }
//
//        if (viewModel.viewImage.value == true) {
//            Dialog(onDismissRequest = {viewModel.viewImage.value = false}) {
//                Column (modifier = Modifier.size(250.dp)){
//                    imageUri.value?.let { uri ->
//                        Image(
//                            painter = rememberAsyncImagePainter(uri),
//                            contentDescription = null,
//                            modifier = Modifier.size(250.dp).clip(RoundedCornerShape(8.dp)),
//                            contentScale = ContentScale.Crop
//                        )
//                    }
//                }
//            }
//        }

            Button(onClick = {

//            val fileName : String = UUID.randomUUID().toString()
//
//            AppState.imageFile = File(context.cacheDir, "$fileName.jpg")

                viewModel.sendTimeSheet(context)
            }, Modifier) {
                Text("Submit")
            }
            Button(
                onClick = {
//                AppState.currentJob = null
//                val intent = Intent(context, AuthenticatedUserView::class.java)
//                context.startActivity(intent)
                },
            ) {
                Text("cancel Log")
            }
        }
    }
}

