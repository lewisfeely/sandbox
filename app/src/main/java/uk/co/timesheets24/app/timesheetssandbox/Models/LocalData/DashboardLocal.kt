package uk.co.timesheets24.app.timesheetssandbox.Models.LocalData

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Dashboard")
data class DashboardLocal (
    @field:PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val contactId: String?,
    val greetingName: String?,
    val overtime: Int,
    val Timetake: Int,
    val travellingTime: Int,
    val availableTime: Int
)

