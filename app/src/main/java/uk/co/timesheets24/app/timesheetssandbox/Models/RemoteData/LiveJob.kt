package uk.co.timesheets24.app.timesheetssandbox.Models.RemoteData

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