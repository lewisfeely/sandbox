package uk.co.timesheets24.app.TS24.Views.Dashboard

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import uk.co.timesheets24.app.TS24.SandboxMainViewModel
import uk.co.timesheets24.app.TS24.SyncScreen

class DashboardView : ComponentActivity() {
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