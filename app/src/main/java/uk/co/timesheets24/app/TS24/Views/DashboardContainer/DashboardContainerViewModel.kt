package uk.co.timesheets24.app.TS24.Views.DashboardContainer

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import uk.co.timesheets24.app.TS24.API.AuthApiClass
import uk.co.timesheets24.app.TS24.GlobalLookUp
import uk.co.timesheets24.app.TS24.LocalDataBase.LocalUserDatabase
import uk.co.timesheets24.app.TS24.LocalDataSevice.RefreshLocalData
import uk.co.timesheets24.app.TS24.Models.LocalData.DashboardLocal
import uk.co.timesheets24.app.TS24.Models.RemoteData.RefreshTokenRemote
import uk.co.timesheets24.app.TS24.R

class DashboardContainerViewModel : ViewModel() {

    val error = mutableStateOf(false)

    val loading = mutableStateOf(false)

    val state = mutableStateOf("")

    val navigationPopUp = mutableStateOf(false)

    val fontAwesomeSolid = FontFamily(
        Font(R.font.fontawesome6freesolid900)  // refer to your font resource here
    )

    fun syncData(context : Context, lifecycleOwner: LifecycleOwner) {
        viewModelScope.launch {
            try {
                loading.value = true
                val board = RefreshLocalData(context)
                board.DoWork()
                board.refreshDescription.observe(lifecycleOwner) {newValue ->
                    state.value = newValue
                }
                loading.value = false
            } catch (e: Exception) {
                println("RESPONSE $e Inside Login ViewModel")
                error.value = true
                loading.value = false
            }
        }
    }
}