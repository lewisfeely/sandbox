package uk.co.timesheets24.app.TS24.Views.Jobs

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import uk.co.timesheets24.app.TS24.LocalDataBase.LocalUserDatabase
import uk.co.timesheets24.app.TS24.Models.LocalData.LiveJobLocal
import uk.co.timesheets24.app.TS24.Models.RemoteData.LiveJobRemote

class JobViewModel : ViewModel() {


    val loading = mutableStateOf(true)

    val navigationPopUp = mutableStateOf(false)

    var liveJobsList = mutableListOf<LiveJobLocal>()

    fun getJobs(context: Context) {
        val jobDao = LocalUserDatabase.getInstance(context.applicationContext).jobDao()
        viewModelScope.launch {
            try {
                val jobs = jobDao.fetchJobs()
                liveJobsList = jobs.toMutableList()
                loading.value = false
            } catch (e : Exception) {
                println("RESPONSE $e")
            }
        }

    }

//    fun permissionsCheck() : Boolean {
//
//
//
//    }

    fun convertToLiveJob(entity: LiveJobLocal): LiveJobRemote {
        return LiveJobRemote(
            poNumber = entity.poNumber,
            createdDate = entity.createdDate,
            description = entity.description,
            jobDate = entity.jobDate,
            jobGUID = entity.jobGUID,
            jobNumber = entity.jobNumber,
            clientId = entity.clientId,
            clientName = entity.clientName,
            clientContactId = entity.clientContactId,
            clientContactName = entity.clientContactName,
            addressId = entity.addressId,
            customerSite = entity.customerSite,
            timeTime = entity.timeTime.toFloat(),
            travellingTime = entity.travellingTime,
            overtime = entity.overtime,
            readyToClose = entity.readyToClose
        )
    }


}