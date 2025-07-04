package uk.co.timesheets24.app.timesheetssandbox.Models.RemoteData

data class RecentEntryRemote (
    val jobNumber : Int,
    val jobDate : String,
    val jobGUID : String?,
    val description : String,
    val timetake : Int,
    val travellingTime : Int,
    val overTime : Int,
    val timeId : String,
    val clientName : String?
)
