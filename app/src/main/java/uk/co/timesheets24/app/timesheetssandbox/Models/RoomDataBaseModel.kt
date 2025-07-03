package uk.co.timesheets24.app.timesheetssandbox.Models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pending_entries_table")
data class TimeSheetEntryTable(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
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
    val travellingTime: Int,
    val tempId : String?,
    val jobNumber : Int = 0,
    val clientName : String?
)

@Entity(tableName = "live_jobs")
data class LiveJobsTable(
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

@Entity(tableName = "recent_entries")
data class RecentEntryTable (
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val jobNumber : Int,
    val jobDate : String,
    val jobGUID : String?,
    val description : String,
    val timetake : Int,
    val travellingTime : Int,
    val overTime : Int,
    val timeId : String,
    val clientName : String?,
    val tempId : String?
)

@Entity(tableName = "timesheet")
data class TimeSheetTable (
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
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
