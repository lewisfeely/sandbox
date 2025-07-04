package uk.co.timesheets24.app.timesheetssandbox.LocalDataBase.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import uk.co.timesheets24.app.timesheetssandbox.Models.LocalData.LiveJobLocal

@Dao
interface JobsDao {

    @Insert()
    suspend fun insert(liveJob: LiveJobLocal)

    @Query("DELETE FROM live_jobs WHERE jobGUID = :jobId")
    suspend fun deleteJobs(jobId : String)

    @Query("SELECT * FROM live_jobs")
    suspend fun fetchJobs() : List<LiveJobLocal>

    @Query("DELETE FROM live_jobs")
    suspend fun clear()
}