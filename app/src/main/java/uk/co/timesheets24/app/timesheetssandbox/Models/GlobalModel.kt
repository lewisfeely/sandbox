package uk.co.timesheets24.app.timesheetssandbox.Models

data class LiveJob(
    val poNumber: String?,
    val createdDate: String,
    val description: String,
    val jobDate: String,
    val jobGUID: String,
    val jobNumber: String,
    val clientId: String?,
    val clientName: String?,
    val clientContactId: String?,
    val clientContactName: String?,
    val addressId: String?,
    val customerSite: String?,
    val timeTime: Float,
    val travellingTime: Int,
    val overtime: Int,
    val readyToClose: Boolean
)

data class RecentEntry (
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

data class EditingTimeSheet(
    val sessionId: String = "",
    val timeSheetId : String,
    val contactName : String?,
    val contactId : String,
    var description : String,
    var materials : String,
    var overTime : Int,
    val timetake : String,
    var travellingTime : Int,
    val createdDate : String,
    val modifiedDate : String,
    val jobGUID : String?,
    var startTime : String,
    var endTime : String,
    val jobTimeStatusId : String?,
    val jobTimeStatus: String?
)

data class ReceivedTimeSheet (
    val timeId : String,
    val contactName : String?,
    val contactId : String,
    val jobDate : String,
    val description : String,
    var materials : String?,
    val overTime : Int,
    val timetake : Int,
    val travellingTime : Int,
    val createdDate : String,
    val modifiedDate : String,
    val jobGUID : String?,
    val startTime : String,
    val endTime : String,
    val jobTimeStatusId : String?,
    var jobTimeStatus : String?
)

data class TimeSheetEntry(
    val jobGUID: String?,
    val timeSheetId: String,
    val contactId: String,
    val description: String,
    val materials: String?,
    val startDate: String,
    val startTime: String,
    val endDate: String,
    val endTime: String,
    val timetake: Int,
    val overTime: Int,
    val travellingTime: Int
)

data class AuthResponse (
    val refresh_token : String,
    val token_type: String,
    val expires_in: Int,
    val ext_expires_in: Int,
    val access_token: String,
    val id_token: String
)

data class UserDetails (
    val id : String,
    val title : String?,
    var firstname: String?,
    var lastname: String?,
    val name: String?,
    var username: String?,
    val companyName: String?,
    var departmentName: String?,
    var email : String?,
    val telephone: String?,
    val mobile: String?,
    val greetingName: String,
    val defaultUrl: String,
    var avatar: String,
    val status: String?,
    val setUpCompleted: Boolean
)