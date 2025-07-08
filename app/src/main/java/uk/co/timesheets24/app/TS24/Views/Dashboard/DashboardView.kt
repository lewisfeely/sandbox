package uk.co.timesheets24.app.TS24.Views.Dashboard

import android.content.Context
import androidx.compose.foundation.background
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
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.tehras.charts.piechart.PieChart
import com.github.tehras.charts.piechart.PieChartData
import com.github.tehras.charts.piechart.PieChartData.Slice
import com.github.tehras.charts.piechart.animation.simpleChartAnimation
import com.github.tehras.charts.piechart.renderer.SimpleSliceDrawer
import uk.co.timesheets24.app.TS24.UI.theme.TSDarkBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen() {

    val viewModel: DashboardViewModel = viewModel()
    val context : Context = LocalContext.current
    val lifecycleOwner : LifecycleOwner = LocalLifecycleOwner.current
    val theme = MaterialTheme.colors

    LaunchedEffect(Unit) {
        viewModel.syncData(context, lifecycleOwner)
    }

    Column(
        Modifier.fillMaxSize().background(color = TSDarkBlue),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(100.dp))
            if (viewModel.loading.value || viewModel.userHours.value == null) {
                Column (Modifier.fillMaxWidth(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
                    CircularProgressIndicator(color = theme.onPrimary)
                    Text(viewModel.state.value, color = theme.onPrimary)
                }
            } else {
                Box(Modifier.size(200.dp)) {
                    MyChartParentLoaded(viewModel)
                }
                Spacer(Modifier.height(30.dp))
                Column (
                    modifier = Modifier.fillMaxWidth().padding(20.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row (Modifier.fillMaxWidth().height(250.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween){
                        Column (modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                "\uf1b9",
                                color = Color(0XFF67c26e),
                                fontSize = 30.sp,
                                fontFamily = viewModel.fontAwesomeSolid,
                                modifier = Modifier.size(50.dp)
                            )
                            Spacer(Modifier.height(10.dp))
                            Text(
                                "\uf64a",
                                color = Color(0XFF7f8ddb),
                                fontSize = 30.sp,
                                fontFamily = viewModel.fontAwesomeSolid,
                                modifier = Modifier.size(50.dp)
                            )
                            Spacer(Modifier.height(10.dp))
                            Text(
                                "\uf183",
                                color = Color(0XFFdee8dc),
                                fontSize = 30.sp,
                                fontFamily = viewModel.fontAwesomeSolid,
                                modifier = Modifier.height(50.dp).width(40.dp)
                            )
                            Spacer(Modifier.height(10.dp))
                            Text(
                                "\uf0e7",
                                color = Color(0XFFb33715),
                                fontSize = 30.sp,
                                fontFamily = viewModel.fontAwesomeSolid,
                                modifier = Modifier.size(50.dp)
                            )
                        }
                        Column (modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.Start) {
                            Text("Travel time", modifier = Modifier.height(50.dp), color = theme.onPrimary)
                            Spacer(Modifier.height(10.dp))
                            Text("Time worked", modifier = Modifier.height(50.dp), color = theme.onPrimary)
                            Spacer(Modifier.height(10.dp))
                            Text("Available time", modifier = Modifier.height(50.dp), color = theme.onPrimary)
                            Spacer(Modifier.height(10.dp))
                            Text("Over time", modifier = Modifier.height(50.dp), color = theme.onPrimary)
                        }
                        Column (modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.End){
                            Text(viewModel.convertMinutesToHoursAndMinutes(viewModel.userHours.value?.travellingTime), modifier = Modifier.height(50.dp), color = theme.onPrimary)
                            Spacer(Modifier.height(10.dp))
                            Text(viewModel.convertMinutesToHoursAndMinutes(viewModel.userHours.value?.Timetake), modifier = Modifier.height(50.dp), color = theme.onPrimary)
                            Spacer(Modifier.height(10.dp))
                            Text(viewModel.convertMinutesToHoursAndMinutes(viewModel.userHours.value?.availableTime), modifier = Modifier.height(50.dp), color = theme.onPrimary)
                            Spacer(Modifier.height(10.dp))
                            Text(viewModel.convertMinutesToHoursAndMinutes(viewModel.userHours.value?.overtime), modifier = Modifier.height(50.dp), color = theme.onPrimary)

                        }
                    }
                }
                Spacer(Modifier.width(10.dp))
            }
        }
}

@Composable
fun MyChartParentLoaded(viewModel : DashboardViewModel) {
    if (viewModel.userHours.value != null) {
        PieChart(
            pieChartData = PieChartData(
                listOf(
                    Slice(
                        viewModel.userHours.value!!.availableTime.toFloat(),
                        Color(0XFFdee8dc)
                    ), Slice(
                        viewModel.userHours.value!!.travellingTime.toFloat(),
                        Color(0XFF67c26e)
                    ),
                    Slice(
                        viewModel.userHours.value!!.Timetake.toFloat(),
                        Color(0XFF7f8ddb)
                    ),
                    Slice(
                        viewModel.userHours.value!!.overtime.toFloat(),
                        Color(0XFFb33715)
                    )
                )
            ),
            animation = simpleChartAnimation(),
            sliceDrawer = SimpleSliceDrawer(sliceThickness = 30F)
        )
    }
}