package uk.co.timesheets24.app.TS24.Views.Dashboard

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel

class DashboardView : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val context : Context = this
            val viewModel : DashboardViewModel = viewModel()
            val lifecycleOwner = LocalLifecycleOwner.current
            DashboardScreen(context, viewModel, lifecycleOwner)
        }
    }
}

enum class HomeTabs (
    val selectedIcon : String,
    val unselectedIcon: String,
    val text : String,
    val content : @Composable () -> Unit,
) {
//    DashBoard(
//        "\uf200",
//        "\uf200",
//        "Dashboard",
//        content = { AuthenticatedUserScreen()}
//    ),
//    Entries(
//        "\uf15b",
//        "\uf15b",
//        "timesheets",
//        content = { RecentEntriesScreen() },
//    ),
//    Jobs(
//        "\uf03a",
//        "\uf03a",
//        "Jobs",
//        content = { SelectJobScreen(true, null) },
//    ),
//    Settings(
//        "\uf013",
//        "\uf013",
//        "Settings",
//        content = {  }
//    )
}

@Composable
fun DashboardScreen(context: Context, viewModel: DashboardViewModel, lifecycleOwner: LifecycleOwner) {
    Column (Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){

        if (viewModel.loading.value) {
            CircularProgressIndicator()
            Text(viewModel.state.value, color = Color.Black)
        } else {
            Button(onClick = {
                viewModel.loading.value = true
                viewModel.error.value = false
                viewModel.syncData(context, lifecycleOwner)
            }) {
                Text("login and display entries")
            }
        }

        if (viewModel.loaded.value && viewModel.timeSheets.value != null) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                items(viewModel.timeSheets.value?.size!!) { entry ->
                    Box(modifier = Modifier.size(100.dp).background(color = Color.Black), contentAlignment = Alignment.Center) {
                        Column {
                            Text(viewModel.timeSheets.value!![entry].description, color = Color.White)
                            Text(
                                viewModel.timeSheets.value!![entry].jobNumber.toString(),
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        if (viewModel.error.value) {
            Text("Error occurred", color = Color.Red)
        }


    }
}