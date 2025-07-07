package uk.co.timesheets24.app.TS24.Models.RemoteData

data class DashboardRemote (
    val contactId: String?,
    val greetingName: String?,
    val overtime: Int,
    val Timetake: Int,
    val travellingTime: Int,
    val availableTime: Int
)
