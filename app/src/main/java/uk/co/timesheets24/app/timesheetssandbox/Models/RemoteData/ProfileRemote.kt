package uk.co.timesheets24.app.timesheetssandbox.Models.RemoteData

data class ProfileRemote(
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
