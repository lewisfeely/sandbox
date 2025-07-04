package uk.co.timesheets24.app.timesheetssandbox.LocalDataBase.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import uk.co.timesheets24.app.timesheetssandbox.Models.LocalData.RecentEntryLocal

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