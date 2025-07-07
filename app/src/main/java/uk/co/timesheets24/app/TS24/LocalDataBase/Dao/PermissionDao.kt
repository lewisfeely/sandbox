package uk.co.timesheets24.app.TS24.LocalDataBase.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import uk.co.timesheets24.app.TS24.Models.LocalData.PermissionLocal

@Dao
interface PermissionDao {

    @Insert()
    suspend fun insert(permissionLocal: PermissionLocal)

    @Query("DELETE FROM Permission")
    suspend fun clear()

    @Query("SELECT * FROM Permission")
    suspend fun fetch() : List<PermissionLocal>


}