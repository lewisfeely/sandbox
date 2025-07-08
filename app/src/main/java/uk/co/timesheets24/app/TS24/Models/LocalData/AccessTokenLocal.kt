package uk.co.timesheets24.app.TS24.Models.LocalData

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "AccessToken")
data class AccessTokenLocal (
    @field:PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val accessToken: String?,
    val refreshToken: String?,
)