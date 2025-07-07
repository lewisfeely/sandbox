package uk.co.timesheets24.app.TS24.LocalDataBase.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import uk.co.timesheets24.app.TS24.Models.LocalData.ProfileLocal

@Dao
interface ProfileDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(profileLocal: ProfileLocal)

    @Query("DELETE FROM Profile")
    suspend fun clear()

    @Query("SELECT * FROM Profile")
    suspend fun fetch() : ProfileLocal


}