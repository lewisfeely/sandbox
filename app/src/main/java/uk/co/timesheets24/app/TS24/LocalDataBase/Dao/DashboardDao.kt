package uk.co.timesheets24.app.TS24.LocalDataBase.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import uk.co.timesheets24.app.TS24.Models.LocalData.DashboardLocal

@Dao
interface DashboardDao{
    @Insert()
    suspend fun insert(dashboard: DashboardLocal)

    @Query("DELETE FROM Dashboard")
    suspend fun clear()

    @Query("SELECT * FROM Dashboard")
    suspend fun fetch() : DashboardLocal
}