package uk.co.timesheets24.app.TS24.Views.Dashboard

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
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
import kotlinx.coroutines.launch
import uk.co.timesheets24.app.TS24.GlobalLookUp
import uk.co.timesheets24.app.TS24.R
import uk.co.timesheets24.app.TS24.UI.theme.TSDarkBlue
import uk.co.timesheets24.app.TS24.Views.RecentEntries.RecentEntriesScreen

class DashboardView : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            DashBoardNav()
        }
    }
}

enum class HomeTabs (
    val selectedIcon : String,
    val unselectedIcon: String,
    val text : String,
    val content : @Composable () -> Unit,
) {
    DashBoard(
        "\uf200",
        "\uf200",
        "Dashboard",
        content = { AuthenticatedUserScreen()}
    ),
    Entries(
        "\uf15b",
        "\uf15b",
        "timesheets",
        content = { RecentEntriesScreen() },
    ),
//    Jobs(
//        "\uf03a",
//        "\uf03a",
//        "Jobs",
//        content = { SelectJobScreen(true, null) },
//    ),
    Settings(
        "\uf013",
        "\uf013",
        "Settings",
        content = {  }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashBoardNav() {

    val viewModel: DashboardViewModel = viewModel()
    val context: Context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { HomeTabs.entries.size }, initialPage = GlobalLookUp.selectedTab)
    val selectedTabIndex = remember {
        derivedStateOf { pagerState.currentPage }
    }

    LaunchedEffect(Unit) {
        if (viewModel.userHours.value != null) {
            viewModel.fetchJobDetails(context)
        }
    }



    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.navigationBars.asPaddingValues()),
        topBar =
            {
                TopAppBar(
                    title = {},
                    colors = TopAppBarDefaults.topAppBarColors(TSDarkBlue),
                    navigationIcon = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            androidx.compose.material3.Button(
                                onClick = {
//                                            val intent = Intent(context, ProfileView::class.java)
//                                            context.startActivity(intent)
                                },
                                modifier = Modifier.size(80.dp)
                            ) {
                                Text(
                                    "\uf007",
                                    color = Color.White,
                                    fontSize = 30.sp,
                                    fontFamily = viewModel.fontAwesomeSolid,
                                )
                            }


                            if (GlobalLookUp.hasInternetAccess(context)) {

                                IconButton(
                                    onClick = {
                                        viewModel.syncData(context)
                                    },
                                    modifier = Modifier.size(80.dp)
                                ) {
                                    Text(
                                        "\uf2f9",
                                        color = Color.White,
                                        fontSize = 30.sp,
                                        fontFamily = viewModel.fontAwesomeSolid,
                                    )
                                }
                            }
                        }
                    },

                    modifier = Modifier.height(130.dp)
                )
            },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(Color(0XFF1e293b)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                val entriesLists = HomeTabs.entries.chunked(2)

                entriesLists[0].forEach { currentTab ->
                    TabButton(currentTab, selectedTabIndex.value == currentTab.ordinal) {
                        GlobalLookUp.selectedTab = currentTab.ordinal
                        scope.launch {
                            pagerState.animateScrollToPage(currentTab.ordinal)
                        }
                    }
                }
//                Box(Modifier.size(60.dp).clip(CircleShape).background(color = Color(0XFF5046e4), shape = CircleShape).clickable {
//                    if (viewModel.permissionsCheck()) {
//                        viewModel.navigationPopUp.value = true
//                    } else {
//                        val intent = Intent(context, LogTimesheetView::class.java)
//                        context.startActivity(intent)
//                    }
//                }, contentAlignment = Alignment.Center) {
//                    Text(
//                        "\u002b",
//                        color = Color.White,
//                        fontSize = 30.sp,
//                        fontFamily = viewModel.fontAwesomeSolid,
//                    )
//                }

                entriesLists[1].forEach { currentTab ->
                    TabButton(currentTab, selectedTabIndex.value == currentTab.ordinal) {
                        GlobalLookUp.selectedTab = currentTab.ordinal
                        scope.launch {
                            pagerState.animateScrollToPage(currentTab.ordinal)
                        }
                    }
                }
            }
        },
        containerColor = MaterialTheme.colors.background,
    )
    { innerPadding ->
        Column(
            Modifier.fillMaxSize().padding(innerPadding),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxWidth().weight(1f)
            ) {
                Column (
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    HomeTabs.entries[selectedTabIndex.value].content()
                }
                if (viewModel.navigationPopUp.value) {
                    ModalBottomSheet(
                        onDismissRequest = { viewModel.navigationPopUp.value = false },
                        sheetState = sheetState,
                        containerColor = TSDarkBlue
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .padding(16.dp)
                        ) {
                            Row (horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxSize()){
                                if (GlobalLookUp.hasInternetAccess(context)) {
                                    Box(
                                        Modifier.size(180.dp).background(
                                            color = Color(0XFF1e293b),
                                            shape = RoundedCornerShape(16.dp)
                                        ).clip(RoundedCornerShape(16.dp)).clickable {
//                                            val intent = Intent(context, CreateJobView::class.java)
//                                            context.startActivity(intent)
                                        }) {
                                        Column(
                                            Modifier.fillMaxSize(),
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(
                                                "\uf007",
                                                color = Color.White,
                                                fontSize = 30.sp,
                                                fontFamily = viewModel.fontAwesomeSolid,
                                            )
                                        }
                                    }
                                }
                                Box(
                                    Modifier.size(180.dp).background(color = Color(0XFF1e293b), shape = RoundedCornerShape(16.dp)).clip(RoundedCornerShape(16.dp)).clickable{
//                                        val intent = Intent(context, LogTimesheetView::class.java)
//                                        context.startActivity(intent)
                                    }
                                ) {
                                    Column(
                                        Modifier.fillMaxSize(),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            "\uf007",
                                            color = Color.White,
                                            fontSize = 30.sp,
                                            fontFamily = viewModel.fontAwesomeSolid,
                                        )
                                    }

                                }
                            }
                            Button(onClick = { viewModel.navigationPopUp.value = false }) {
                                Text("Close")
                            }
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthenticatedUserScreen() {

    val viewModel: DashboardViewModel = viewModel()
    val context : Context = LocalContext.current
    viewModel.fetchJobDetails(context)


    Column(
        Modifier.fillMaxSize().padding(10.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(100.dp))
        Box(Modifier.size(200.dp)) {
            if (viewModel.loadingUserHours.value) {
                Row (Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically){
                    CircularProgressIndicator(color = Color.White)
                }
            } else {
                MyChartParentLoaded(viewModel)
            }
        }
        Spacer(Modifier.height(30.dp))
        Column (
            modifier = Modifier.fillMaxWidth().padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!viewModel.loadingUserHours.value) {
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
                        Text("Travel time", modifier = Modifier.height(50.dp))
                        Spacer(Modifier.height(10.dp))
                        Text("Time worked", modifier = Modifier.height(50.dp))
                        Spacer(Modifier.height(10.dp))
                        Text("Available time", modifier = Modifier.height(50.dp))
                        Spacer(Modifier.height(10.dp))
                        Text("Over time", modifier = Modifier.height(50.dp))
                    }
                    Column (modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.End){
                        Text(viewModel.convertMinutesToHoursAndMinutes(viewModel.userHours.value?.travellingTime), modifier = Modifier.height(50.dp))
                        Spacer(Modifier.height(10.dp))
                        Text(viewModel.convertMinutesToHoursAndMinutes(viewModel.userHours.value?.Timetake), modifier = Modifier.height(50.dp))
                        Spacer(Modifier.height(10.dp))
                        Text(viewModel.convertMinutesToHoursAndMinutes(viewModel.userHours.value?.availableTime), modifier = Modifier.height(50.dp))
                        Spacer(Modifier.height(10.dp))
                        Text(viewModel.convertMinutesToHoursAndMinutes(viewModel.userHours.value?.overtime), modifier = Modifier.height(50.dp))

                    }
                }
            }
        }
        Spacer(Modifier.width(10.dp))
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

@Composable
fun TabButton(
    currentTab: HomeTabs,
    selected: Boolean,
    onClick: () -> Unit
) {
    val fontAwesomeSolid = FontFamily(Font(R.font.fontawesome6freesolid900))

    Column(
        modifier = Modifier
            .clickable { onClick() }
            .padding(horizontal = 12.dp)
            .size(55.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(8.dp))
        Text(
            text = currentTab.selectedIcon,
            fontFamily = fontAwesomeSolid,
            fontSize = 20.sp,
            color = if (selected) Color.Gray else Color.White
        )
        Text(
            text = currentTab.text,
            color = if (selected) Color.Gray else Color.White,
            fontSize = 11.sp
        )
    }
}