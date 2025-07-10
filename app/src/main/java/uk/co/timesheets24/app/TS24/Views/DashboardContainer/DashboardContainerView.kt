package uk.co.timesheets24.app.TS24.Views.DashboardContainer

import android.content.Context
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
import uk.co.timesheets24.app.TS24.UI.theme.TS24Theme
import uk.co.timesheets24.app.TS24.UI.theme.TSDarkBlue
import uk.co.timesheets24.app.TS24.Views.Dashboard.DashboardScreen
import uk.co.timesheets24.app.TS24.Views.Jobs.SelectJobScreen
import uk.co.timesheets24.app.TS24.Views.Jobs.SelectJobView
import uk.co.timesheets24.app.TS24.Views.RecentEntries.RecentEntriesScreen

class DashboardView : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TS24Theme {
                DashBoardNav()
            }
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
        content = { DashboardScreen() }
    ),
    Entries(
        "\uf15b",
        "\uf15b",
        "timesheets",
        content = { RecentEntriesScreen() },
    ),
    Jobs(
        "\uf03a",
        "\uf03a",
        "Jobs",
        content = { SelectJobScreen(true, null) },
    ),
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

    val viewModel: DashboardContainerViewModel = viewModel()
    val context: Context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { HomeTabs.entries.size }, initialPage = GlobalLookUp.selectedTab)
    val selectedTabIndex = remember {
        derivedStateOf { pagerState.currentPage }
    }
    val lifecycleOwner : LifecycleOwner = LocalLifecycleOwner.current

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(TSDarkBlue)
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
                                if (viewModel.loading.value) {
                                    CircularProgressIndicator(color = MaterialTheme.colors.onPrimary)
                                    Text(viewModel.state.value, color = MaterialTheme.colors.onPrimary)
                                } else {
                                    IconButton(
                                        onClick = {
                                            viewModel.syncData(context, lifecycleOwner)
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
                Box(Modifier.size(60.dp).clip(CircleShape).background(color = Color(0XFF5046e4), shape = CircleShape).clickable {
                        viewModel.navigationPopUp.value = true
                }, contentAlignment = Alignment.Center) {
                    Text(
                        "\u002b",
                        color = Color.White,
                        fontSize = 30.sp,
                        fontFamily = viewModel.fontAwesomeSolid,
                    )
                }

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
                    modifier = Modifier.fillMaxSize().background(TSDarkBlue),
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
                                                "\uf133",
                                                color = Color.White,
                                                fontSize = 30.sp,
                                                fontFamily = viewModel.fontAwesomeSolid,
                                            )
                                            Text("create job", color = MaterialTheme.colors.onPrimary)
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
                                            "\uf15b",
                                            color = Color.White,
                                            fontSize = 30.sp,
                                            fontFamily = viewModel.fontAwesomeSolid,
                                        )
                                        Text("create timesheet", color = MaterialTheme.colors.onPrimary)
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