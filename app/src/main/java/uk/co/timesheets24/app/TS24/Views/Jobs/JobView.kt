package uk.co.timesheets24.app.TS24.Views.Jobs

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uk.co.timesheets24.app.TS24.GlobalLookUp
import uk.co.timesheets24.app.TS24.UI.theme.TS24Theme
import uk.co.timesheets24.app.TS24.UI.theme.TSDarkBlue
import uk.co.timesheets24.app.TS24.Views.CreateTimesheet.LogTimesheetView
import uk.co.timesheets24.app.TS24.Views.DashboardContainer.DashboardView
import kotlin.collections.isNotEmpty
import kotlin.jvm.java

class SelectJobView : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val edit = true
            TS24Theme {
                SelectJobScreen(false, "true")
            }
        }
    }
}

@Composable
fun SelectJobScreen(viewing : Boolean, editing : String?) {

    val viewModel = remember {JobViewModel()}
    val context : Context = LocalContext.current

    LaunchedEffect(Unit) {
        if(!viewModel.liveJobsList.isNotEmpty()) {
            viewModel.getJobs(context)
        }
    }

    Column (
        Modifier.fillMaxSize().background(color = TSDarkBlue),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
            Column {
                Row(
                    Modifier.background(Color(0XFF1e293b))
                        .fillMaxWidth()
                        .height(100.dp)
                        .clickable(onClick = {
                            if (!viewing || editing != null) {
                                GlobalLookUp.selectedJob = null
                                val intent = Intent(context, LogTimesheetView::class.java)
                                context.startActivity(intent)
                            }
                        }),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Column {

                        Row(verticalAlignment = Alignment.CenterVertically) {

                            Spacer(Modifier.width(10.dp))
                            Text(
                                "submit to no job",
                                color = Color.White,
                                fontSize = 16.sp
                            )
                        }


                    }
                }
            }
            Spacer(Modifier.height(10.dp))
        if (!viewModel.loading.value) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth().height(if (!viewing || editing != null) 800.dp else 700.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
            items(viewModel.liveJobsList.size) { liveJob ->
                Spacer(Modifier.height(10.dp))
                Column {
                    Row(
                        Modifier.background(Color(0XFF1e293b))
                            .fillMaxWidth()
                            .height(100.dp)
                            .clickable(onClick = {
                                if (!viewing || editing != null) {
                                    GlobalLookUp.selectedJob = viewModel.liveJobsList[liveJob]
                                    val intent = Intent(context, LogTimesheetView::class.java)
                                    context.startActivity(intent)
                                }
                            }),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Column {
                            Box(
                                Modifier.fillMaxWidth()
                                    .height(50.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(50.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = viewModel.liveJobsList[liveJob].jobNumber,
                                            color = Color.White
                                        )
                                    }
                                    Spacer(Modifier.width(10.dp))
                                    Text(
                                        viewModel.liveJobsList[liveJob].clientName ?: "no client Name",
                                        color = Color.White,
                                        fontSize = 22.sp
                                    )
                                }
                            }

                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                Text(
                                    viewModel.liveJobsList[liveJob].description,
                                    color = Color.White,
                                    modifier = Modifier.width(150.dp),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )
                                Box(Modifier.height(50.dp).width(80.dp)) {
                                    Column(Modifier.height(50.dp)) {
                                        Text(
                                            "duration : ${viewModel.liveJobsList[liveJob].timeTime} hrs",
                                            fontSize = 10.sp,
                                            modifier = Modifier.height(16.dp),
                                            color = Color.White
                                        )
                                        Text(
                                            "travel : ${viewModel.liveJobsList[liveJob].travellingTime} hrs",
                                            fontSize = 10.sp,
                                            modifier = Modifier.height(16.dp),
                                            color = Color.White
                                        )
                                        Text(
                                            "over time : ${viewModel.liveJobsList[liveJob].overtime}hrs ",
                                            fontSize = 10.sp,
                                            modifier = Modifier.height(16.dp),
                                            color = Color.White
                                        )
                                    }
                                }

                            }
                        }
                    }
                }
                }
            }
        }else {
            CircularProgressIndicator(color = Color.White)
        }
        if (editing != null) {
            Button(onClick = {
                val intent = Intent(context, DashboardView::class.java)
                context.startActivity(intent)
            }) {
                Text("back to Dash")
            }
        }
        }
    }
