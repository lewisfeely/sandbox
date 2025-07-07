package uk.co.timesheets24.app.TS24.LocalDataSevice

interface IRefreshLocalData {
    suspend fun DoWork() : Boolean

}