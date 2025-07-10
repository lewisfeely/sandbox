package uk.co.timesheets24.app.TS24.Views.CreateJob

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import uk.co.timesheets24.app.TS24.API.ClientApiClass
import uk.co.timesheets24.app.TS24.API.JobsApiClass
import uk.co.timesheets24.app.TS24.GlobalLookUp
import uk.co.timesheets24.app.TS24.LocalDataBase.LocalUserDatabase
import uk.co.timesheets24.app.TS24.Models.ClientDetails
import uk.co.timesheets24.app.TS24.Models.CreateJob
import java.util.Calendar
import kotlin.collections.forEach
import kotlin.collections.isNotEmpty
import kotlin.jvm.java
import kotlin.to

class CreateJobViewModel : ViewModel() {

    val description = mutableStateOf("")
    val notes = mutableStateOf("")
    val quoteRef = mutableStateOf("")
    val isQuote = mutableStateOf(false)
    val expanded = mutableStateOf(false)
    val selected = mutableStateOf<ClientDetails?>(null)

    var clientSelected = true
    var dateSelected = true

    val calendar = Calendar.getInstance()
    val selectedDate = mutableStateOf("select a date")

    var clientsList = mutableListOf<ClientDetails>()

    val loading = mutableStateOf(true)

    fun createJob(job : CreateJob, context : Context) {
        val jobDao = LocalUserDatabase.getInstance(context.applicationContext).jobDao()
        val jobApi = JobsApiClass(context).jobs

        viewModelScope.launch {
            try {
//                jobApi.postJob("Bearer ${GlobalLookUp.token}", CreateJob)


            } catch (e : Exception) {
                println("RESPONSE $e")
                loading.value = false
            }
        }


    }

    fun fetchClients(context : Context) {
        val clientApi = ClientApiClass(context).Clients
        viewModelScope.launch {
            try {

                val clients = clientApi.fetchClients("Bearer ${GlobalLookUp.token}", "")
                clientsList = clients.toMutableList()
                loading.value = false

            } catch (e : Exception) {
                println("RESPONSE $e error inside fetching clients")
                loading.value = false
            }
        }


    }

}