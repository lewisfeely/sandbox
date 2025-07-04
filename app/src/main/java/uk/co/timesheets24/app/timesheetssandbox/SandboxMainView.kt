package uk.co.timesheets24.app.timesheetssandbox

import android.R
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.fragment.app.FragmentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel


class SandboxMainView : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val context : Context = this
            val viewModel : SandboxMainViewModel = viewModel()
            SyncScreen(viewModel, context)
        }
    }
}

@Composable
fun SyncScreen(viewModel: SandboxMainViewModel, context: Context) {

    Column (Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){

        if (viewModel.loading.value) {
            CircularProgressIndicator()
        } else {
            Button(onClick = {
                viewModel.loading.value = true
                viewModel.error.value = false
                viewModel.login(context)
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