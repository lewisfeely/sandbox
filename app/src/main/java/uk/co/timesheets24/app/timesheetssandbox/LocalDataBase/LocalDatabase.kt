package uk.co.timesheets24.app.timesheetssandbox.LocalDataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import uk.co.timesheets24.app.timesheetssandbox.Models.LocalData.DashboardLocal
import uk.co.timesheets24.app.timesheetssandbox.Models.LocalData.JobTimeStatusLocal
import uk.co.timesheets24.app.timesheetssandbox.Models.LocalData.LiveJobLocal
import uk.co.timesheets24.app.timesheetssandbox.Models.LocalData.PermissionLocal
import uk.co.timesheets24.app.timesheetssandbox.Models.LocalData.ProfileLocal
import uk.co.timesheets24.app.timesheetssandbox.Models.LocalData.RecentEntryLocal
import uk.co.timesheets24.app.timesheetssandbox.Models.TimeSheetEntryTable
import uk.co.timesheets24.app.timesheetssandbox.Models.TimeSheetTable
import kotlin.jvm.java

 @Database(entities = [
     LiveJobLocal::class,
     RecentEntryLocal::class,
     TimeSheetEntryTable::class,
     TimeSheetTable::class,
     DashboardLocal::class,
     JobTimeStatusLocal::class,
     ProfileLocal::class,
     PermissionLocal::class],
     version = 3)
abstract class LocalUserDatabase : RoomDatabase() {

    abstract fun jobDao() : JobsDao
    abstract fun recentEntriesDao() : RecentEntries
    abstract fun pendingTimeSheetEntries() : PendingEntries
    abstract fun timeSheetDao() : TimeSheetTableDao
    abstract fun dashboardDao() : DashboardDao
    abstract fun jobTimeStatusDao() : JobTimeStatusDao
    abstract fun profileDao() : ProfileDao
    abstract fun permissionDao() : PermissionDao

    companion object {

//        val Migration_13_14 = object : Migration(13, 14) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL("ALTER TABLE pending_entries_table ADD COLUMN clientName TEXT")
//                database.execSQL("ALTER TABLE pending_entries_table ADD COLUMN jobNumber INTEGER NOT NULL DEFAULT 0")
//            }
//        }

        @Volatile
        private var INSTANCE: LocalUserDatabase? = null


        fun getInstance(context: Context): LocalUserDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocalUserDatabase::class.java,
                    "user_logged"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }


    }


}