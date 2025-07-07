package uk.co.timesheets24.app.TS24.Models.LocalData

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "JobTimeStatus")
data class JobTimeStatusLocal(
    @field:PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val jobTimeStatusId: String?,
    val description: String?
)
