package uk.co.timesheets24.app.TS24.Models





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

data class DashboardRequest(
 val timeSheetJobdateFrom: String,
 val timeSheetJobdateTo: String
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

data class CreateJob(
    val clientId : String,
    val siteId : String,
    val clientContactID : String,
    val description : String,
    val poNumber : String,
    val jobDate : String,
    val note : String,
    val quoteRef : String,
    val isQuote : Boolean,
)

data class Client(
    val clientId : String,
    val clientName : String
)

data class Search(
    val clientName : String
)

data class ClientDetails(
    val clientId : String,
    val clientName : String,
    val clientTypeId : String,
    val clientTypeDescription : String,
    val clientNumber : String,
    val addresses : List<Address>,
    val contacts : List<Contact>
)

data class Address (
    val address1 : String,
    val address2 : String,
    val address3 : String,
    val address4 : String,
    val postcode : String,
    val addressId : String
)

data class Contact (
    val contactID : String,
    val title : String,
    val firstName : String,
    val lastName: String,
    val greetingName: String,
    val emailAddress: String,
    val mobile : String,
    val contactType: String,
    val contactTypeID : String,
    val telephone: String
)