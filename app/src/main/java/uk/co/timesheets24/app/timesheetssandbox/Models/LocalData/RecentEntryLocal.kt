package uk.co.timesheets24.app.timesheetssandbox.Models.LocalData

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recent_entries")
data class RecentEntryLocal (
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