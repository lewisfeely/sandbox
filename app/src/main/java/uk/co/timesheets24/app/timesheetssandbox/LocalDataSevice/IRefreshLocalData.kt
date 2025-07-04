package uk.co.timesheets24.app.timesheetssandbox.LocalDataSevice

interface IRefreshLocalData {
    suspend fun DoWork() : Boolean

}