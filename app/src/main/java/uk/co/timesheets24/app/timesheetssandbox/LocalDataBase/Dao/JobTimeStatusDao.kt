package uk.co.timesheets24.app.timesheetssandbox.LocalDataBase.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import uk.co.timesheets24.app.timesheetssandbox.Models.LocalData.JobTimeStatusLocal

@Dao
interface JobTimeStatusDao {

    @Insert()
    suspend fun insert(jobTimeStatusLocal: JobTimeStatusLocal)

    @Query("DELETE FROM JobTimeStatus")
    suspend fun clear()

    @Query("SELECT * FROM JobTimeStatus")
    suspend fun fetch() : List<JobTimeStatusLocal>
}