package uk.co.timesheets24.app.TS24.Models.LocalData
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Permission")
data class PermissionLocal(
    @field:PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val permissionID: String?,
    val permissionDescription: String?,
    val permissionGroupId: String?,
    val permissionGroupDescription: String?,
    val internalReference: String?
)



