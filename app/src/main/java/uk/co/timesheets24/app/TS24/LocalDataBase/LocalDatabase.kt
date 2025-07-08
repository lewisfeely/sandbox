package uk.co.timesheets24.app.TS24.LocalDataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import uk.co.timesheets24.app.TS24.LocalDataBase.Dao.AccessTokenDao
import uk.co.timesheets24.app.TS24.LocalDataBase.Dao.DashboardDao
import uk.co.timesheets24.app.TS24.LocalDataBase.Dao.JobTimeStatusDao
import uk.co.timesheets24.app.TS24.LocalDataBase.Dao.JobsDao
import uk.co.timesheets24.app.TS24.LocalDataBase.Dao.PermissionDao
import uk.co.timesheets24.app.TS24.LocalDataBase.Dao.ProfileDao
import uk.co.timesheets24.app.TS24.LocalDataBase.Dao.RecentEntries
import uk.co.timesheets24.app.TS24.Models.LocalData.AccessTokenLocal
import uk.co.timesheets24.app.TS24.Models.LocalData.DashboardLocal
import uk.co.timesheets24.app.TS24.Models.LocalData.JobTimeStatusLocal
import uk.co.timesheets24.app.TS24.Models.LocalData.LiveJobLocal
import uk.co.timesheets24.app.TS24.Models.LocalData.PermissionLocal
import uk.co.timesheets24.app.TS24.Models.LocalData.ProfileLocal
import uk.co.timesheets24.app.TS24.Models.LocalData.RecentEntryLocal
import kotlin.jvm.java

 @Database(entities = [
     LiveJobLocal::class,
     RecentEntryLocal::class,
     DashboardLocal::class,
     JobTimeStatusLocal::class,
     ProfileLocal::class,
     PermissionLocal::class,
     AccessTokenLocal::class],
     version = 6)
abstract class LocalUserDatabase : RoomDatabase() {

    abstract fun jobDao() : JobsDao
    abstract fun recentEntriesDao() : RecentEntries
    abstract fun dashboardDao() : DashboardDao
    abstract fun jobTimeStatusDao() : JobTimeStatusDao
    abstract fun profileDao() : ProfileDao
    abstract fun permissionDao() : PermissionDao
     abstract fun accessTokenDao() : AccessTokenDao

    companion object {

        val Migration_5_6 = object : Migration(5, 6) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE AccessToken ADD COLUMN timeCreated TEXT")
            }
        }

        @Volatile
        private var INSTANCE: LocalUserDatabase? = null


        fun getInstance(context: Context): LocalUserDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocalUserDatabase::class.java,
                    "user_logged"
                ).fallbackToDestructiveMigration().addMigrations(Migration_5_6).build()
                INSTANCE = instance
                instance
            }
        }


    }


}