package uk.co.timesheets24.app.TS24.LocalDataBase.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import uk.co.timesheets24.app.TS24.Models.LocalData.AccessTokenLocal


@Dao
interface AccessTokenDao{
    @Insert()
    suspend fun insert(accessToken: AccessTokenLocal)

    @Query("DELETE FROM AccessToken")
    suspend fun clear()

    @Query("SELECT * FROM AccessToken")
    suspend fun fetch() : AccessTokenLocal
}