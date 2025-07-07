package uk.co.timesheets24.app.TS24

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import uk.co.timesheets24.app.TS24.API.AuthApiClass
import uk.co.timesheets24.app.TS24.LocalDataSevice.RefreshLocalData

class SandboxMainViewModel : ViewModel() {

    val timeSheets = mutableStateOf(GlobalLookUp.recentEntries)

    val error = mutableStateOf(false)

    val loading = mutableStateOf(false)

    val loaded = mutableStateOf(false)

    fun login(context : Context) {
        viewModelScope.launch {
            try {

                val authApi = AuthApiClass(context).authApi
                val board = RefreshLocalData(context)
                val response = authApi.authentication("mike.feely@outlook.com", "London2016#")
                GlobalLookUp.token = response.access_token
                board.DoWork()
                loading.value = false

            } catch (e: Exception) {
                println("RESPONSE $e Inside Login ViewModel")
                error.value = true
                loading.value = false
            }
        }

//        loading.value = true
//
//        val workManager = WorkManager.getInstance(context)
//
//        val uploadRequest = OneTimeWorkRequestBuilder<ConnectService>()
//            .setInputData(workDataOf("login" to true, "password" to "London2016#", "email" to "mike.feely@outlook.com"))
//            .build()
//
//        workManager.enqueue(uploadRequest)
//
//        workManager.getWorkInfoByIdLiveData(uploadRequest.id).observeForever { workInfo ->
//            if (workInfo?.state == WorkInfo.State.SUCCEEDED) {
//
//                fetchRecentEntries(context)
//
//            } else if (workInfo?.state == WorkInfo.State.FAILED) {
//
//                error.value = true
//
//            }
//        }
    }

//    fun fetchRecentEntries(context : Context) {
//
//        val workManager = WorkManager.getInstance(context)
//
//        val uploadRequest = OneTimeWorkRequestBuilder<ConnectService>()
//            .setInputData(workDataOf("fetch_entries" to true))
//            .build()
//
//        workManager.enqueue(uploadRequest)
//
//        workManager.getWorkInfoByIdLiveData(uploadRequest.id).observeForever { workInfo ->
//            if (workInfo?.state == WorkInfo.State.SUCCEEDED) {
//
//                loaded.value = true
//                loading.value = false
//
//            } else if (workInfo?.state == WorkInfo.State.FAILED) {
//
//                error.value = true
//
//            }
//        }
//
//    }

}