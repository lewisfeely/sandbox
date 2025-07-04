package uk.co.timesheets24.app.timesheetssandbox.LocalDataBase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import uk.co.timesheets24.app.timesheetssandbox.Models.LocalData.LiveJobLocal
import uk.co.timesheets24.app.timesheetssandbox.Models.LocalData.RecentEntryLocal
import uk.co.timesheets24.app.timesheetssandbox.Models.TimeSheetEntryTable
import uk.co.timesheets24.app.timesheetssandbox.Models.TimeSheetTable

//@Dao
//interface UserDao {
//
//    @Insert()
//    suspend fun insert(user: User)
//
//    @Query("SELECT * FROM user_logged")
//    suspend fun getAllUsers(): List<User>
//
//    @Query("DELETE FROM user_logged WHERE access_token = :token")
//    suspend fun deleteByAccessToken(token: String)
//
//    @Query("SELECT * FROM user_logged WHERE access_token = :token")
//    suspend fun loginOffline(token : String) : User
//
//    @Query("DELETE FROM user_logged")
//    suspend fun clear()
//
//}

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

@Dao
interface RecentEntries {

    @Insert()
    suspend fun insert(recentEntry : RecentEntryLocal)

    @Query("SELECT * FROM recent_entries")
    suspend fun fetchEntries() : List<RecentEntryLocal>

    @Query("DELETE FROM recent_entries")
    suspend fun clearEntries()

    @Query("DELETE FROM recent_entries WHERE description = :description")
    suspend fun deleteRecentEntry(description : String)

    @Query("SELECT * FROM recent_entries WHERE timeId = :timeId")
    suspend fun fetchById(timeId : String) : RecentEntryLocal

    @Query("SELECT * FROM recent_entries WHERE timeId = :timeId AND description = :description")
    suspend fun fetchByIdAndDescription(timeId : String, description : String) : RecentEntryLocal

    @Update
    suspend fun editRecentEntry(recentEntry : RecentEntryLocal)

}

@Dao
interface PendingEntries {

    @Insert()
    suspend fun insert(pendingEntry : TimeSheetEntryTable)

    @Query("DELETE FROM pending_entries_table WHERE description = :description")
    suspend fun deletePendingRecentEntry(description : String)

    @Query("SELECT * FROM pending_entries_table WHERE description = :description")
    suspend fun fetchByDescription(description : String) : TimeSheetEntryTable

    @Query("SELECT * FROM pending_entries_table")
    suspend fun fetchAllPending() : List<TimeSheetEntryTable>

    @Query("DELETE FROM pending_entries_table")
    suspend fun deleteAll()

}

//@Dao
//interface UserDetailsDao {
//
//    @Insert()
//    suspend fun insert(userDetails : UserDetailsTable)
//
//    @Query("SELECT * FROM user_details WHERE access_token = :token")
//    suspend fun getDetails(token : String) : UserDetailsTable
//
//    @Query("DELETE FROM user_details")
//    suspend fun clear()
//
//}

@Dao
interface TimeSheetTableDao {

    @Query("DELETE FROM timesheet")
    suspend fun clear()

    @Insert()
    suspend fun insert(timeSheet : TimeSheetTable)

    @Query("SELECT * FROM timesheet")
    suspend fun selectAllTimesheets() : List<TimeSheetTable>

    @Query("SELECT * FROM timesheet WHERE description = :description")
    suspend fun selectTimeSheetByDescription(description: String) : TimeSheetTable

    @Update
    suspend fun editTimeSheet(timeSheet: TimeSheetTable)

}