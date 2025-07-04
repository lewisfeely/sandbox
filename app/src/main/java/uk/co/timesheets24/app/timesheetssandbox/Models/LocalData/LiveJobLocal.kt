package uk.co.timesheets24.app.timesheetssandbox.Models.LocalData

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "live_jobs")
data class LiveJobsLocal(
    @field:PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val poNumber: String,
    val createdDate: String,
    val description: String,
    val jobDate: String,
    val jobGUID: String,
    val jobNumber: String,
    val clientId: String,
    val clientName: String,
    val clientContactId: String,
    val clientContactName: String,
    val addressId: String,
    val customerSite: String,
    val timeTime: Int,
    val travellingTime: Int,
    val overtime: Int,
    val readyToClose: Boolean
)