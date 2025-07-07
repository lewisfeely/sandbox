package uk.co.timesheets24.app.TS24.Views.RecentEntries

import android.R.attr.shape
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import uk.co.timesheets24.app.TS24.GlobalLookUp
import uk.co.timesheets24.app.TS24.UI.theme.TS24Theme
import uk.co.timesheets24.app.TS24.UI.theme.TSDarkBlue
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.collections.isNotEmpty
import kotlin.jvm.java


class RecentEntriesView : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TS24Theme {
                    RecentEntriesScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun RecentEntriesScreen() {

    val context: Context = LocalContext.current
    val viewModel : RecentEntriesViewModel = viewModel()

    val selectedTimeId = remember { mutableIntStateOf(0) }


    LaunchedEffect(Unit) {
        if (!viewModel.recentEntriesList.isNotEmpty() && !viewModel.isLoading.value) {
            viewModel.fetchRecentEntries(context)
        }
    }


        Column(
            modifier = Modifier.fillMaxSize().background(TSDarkBlue).padding(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(20.dp))
            if (viewModel.loaded.value) {
                if (viewModel.recentEntriesList.isNotEmpty()) {

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth().height(700.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        items(viewModel.recentEntriesList.size) { job ->
                            val dismissState = rememberDismissState(
                                confirmStateChange = {
                                    if (it == DismissValue.DismissedToStart) {
                                        selectedTimeId.intValue = job
                                        viewModel.dialog.value = true
                                    }
                                    true
                                }
                            )

                            SwipeToDismiss(
                                state = dismissState,
                                background = {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(Color.Red)
                                            .padding(20.dp),
                                        contentAlignment = Alignment.CenterEnd
                                    ) {
                                        Text("Delete", color = Color.White)
                                    }
                                },
                                directions = setOf(DismissDirection.EndToStart), // Swipe right-to-left
                                dismissContent = {
                                    Spacer(modifier = Modifier.height(20.dp))
                                    Column {
                                        Row(
                                            Modifier.background(Color(0XFF1e293b))
                                                .fillMaxWidth()
                                                .height(100.dp)
                                                .clickable(onClick = {
                                                    if (GlobalLookUp.hasInternetAccess(context)) {
                                                        viewModel._jobEditing.value =
                                                            viewModel.recentEntriesList[job].timeId
                                                    } else {
                                                        viewModel._jobEditing.value =
                                                            viewModel.recentEntriesList[job].description

                                                    }
                                                    viewModel._timeSheetId.intValue = job
                                                    viewModel.loading.value = false
                                                    viewModel.selectedJob(context)
                                                }),
                                            verticalAlignment = Alignment.Top,
                                            horizontalArrangement = Arrangement.Start
                                        ) {
                                            Column {
                                                Box(
                                                    Modifier.width(350.dp)
                                                        .height(50.dp)

                                                ) {
                                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                                        Box(
                                                            modifier = Modifier
                                                                .size(50.dp),
                                                            contentAlignment = Alignment.Center
                                                        ) {
                                                            Text(
                                                                text = "${viewModel.recentEntriesList[job].jobNumber}",
                                                                color = Color.White
                                                            )
                                                        }
                                                        Spacer(Modifier.width(10.dp))
                                                        Text(
                                                            viewModel.recentEntriesList[job].clientName
                                                                ?: "no client Name",
                                                            color = Color.White,
                                                            fontSize = 16.sp
                                                        )
                                                        Spacer(Modifier.width(30.dp))
                                                        if (viewModel.recentEntriesList[job].timeId == "") {
                                                            Box(Modifier.background(color = Color.Red, shape = CircleShape).size(10.dp)) {}
                                                        }
                                                    }

                                                }

                                                Row(
                                                    Modifier.width(350.dp).height(50.dp),
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.SpaceAround
                                                ) {
                                                    Text(
                                                        viewModel.recentEntriesList[job].description,
                                                        color = Color.White,
                                                        modifier = Modifier.width(150.dp),
                                                        maxLines = 1,
                                                        overflow = TextOverflow.Ellipsis,
                                                    )
                                                    Box(Modifier.height(50.dp).width(80.dp)) {
                                                        Column(Modifier.height(50.dp)) {
                                                            Text(
                                                                "duration : ${viewModel.recentEntriesList[job].timetake / 60} hrs",
                                                                fontSize = 10.sp,
                                                                modifier = Modifier.height(16.dp)
                                                            )
                                                            Text(
                                                                "travel : ${viewModel.recentEntriesList[job].travellingTime / 60} hrs",
                                                                fontSize = 10.sp,
                                                                modifier = Modifier.height(16.dp)
                                                            )
                                                            Text(
                                                                "over time : ${viewModel.recentEntriesList[job].overTime / 60}hrs ",
                                                                fontSize = 10.sp,
                                                                modifier = Modifier.height(16.dp)
                                                            )
                                                        }
                                                    }

                                                }
                                            }
                                            Column(
                                                verticalArrangement = Arrangement.Center,
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                modifier = Modifier.fillMaxSize()
                                            ) {
//                                                Icon(
//                                                    painter = painterResource(R.drawable.chevronrightsolid),
//                                                    contentDescription = "Menu",
//                                                    tint = MaterialTheme.colorScheme.onPrimary,
//                                                    modifier = Modifier.size(40.dp)
//                                                )
                                            }

                                            when (viewModel.dialog.value) {
                                                true -> {
                                                    Dialog(onDismissRequest = {}) {

                                                        Column(
                                                            Modifier
                                                                .height(200.dp).width(300.dp)
                                                                .background(color = Color(0XFF1e293b))
                                                                .border(
                                                                    width = 2.dp,
                                                                    color = Color.White
                                                                ),
                                                            verticalArrangement = Arrangement.Center,
                                                            horizontalAlignment = Alignment.CenterHorizontally
                                                        ) {
                                                            Text("confirm delete")
                                                            Row {
                                                                Button(onClick = {
                                                                    viewModel.deleteRecentEntry(
                                                                        context,
                                                                        viewModel.recentEntriesList[selectedTimeId.intValue].timeId,
                                                                        viewModel.recentEntriesList[selectedTimeId.intValue].description
                                                                    )
                                                                    viewModel._loaded.value = false
                                                                    viewModel.dialog.value = false
                                                                }) {
                                                                    Text("Confirm")
                                                                }
                                                                Button(onClick = {
                                                                    viewModel.recentEntriesList.clear()
                                                                    viewModel.fetchRecentEntries(
                                                                        context
                                                                    )
                                                                    viewModel._loaded.value = false
                                                                    viewModel.dialog.value = false
                                                                }) {
                                                                    Text("Cancel")
                                                                }

                                                            }
                                                        }
                                                    }
                                                }

                                                false -> {}
                                            }

//                                            when (GlobalLookUp.jobReturned.value != null) {
//                                                true -> {
//                                                    val intent =
//                                                        Intent(
//                                                            context,
//                                                            EditRecentEntryView::class.java
//                                                        )
//                                                    context.startActivity(intent)
//                                                }
//
//                                                false -> {}
//                                            }
                                        }
                                    }

                                })

                        }
                    }

                } else if (!viewModel.loaded.value) {
                    CircularProgressIndicator(color = Color.White)
                }
            } else {
                CircularProgressIndicator(color = Color.White)
                Text("loading items...", color = Color.White)
            }
        }
//    }

}