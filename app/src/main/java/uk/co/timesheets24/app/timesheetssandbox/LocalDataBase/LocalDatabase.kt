package uk.co.timesheets24.app.timesheetssandbox.LocalDataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import uk.co.timesheets24.app.timesheetssandbox.Models.LocalData.LiveJobsTable
import uk.co.timesheets24.app.timesheetssandbox.Models.RecentEntryTable
import uk.co.timesheets24.app.timesheetssandbox.Models.TimeSheetEntryTable
import uk.co.timesheets24.app.timesheetssandbox.Models.TimeSheetTable
import kotlin.jvm.java

 @Database(entities = [
     LiveJobsTable::class,
     RecentEntryTable::class,
     TimeSheetEntryTable::class,
     TimeSheetTable::class],
     version = 1)
abstract class LocalUserDatabase : RoomDatabase() {

    abstract fun jobDao() : JobsDao
    abstract fun recentEntriesDao() : RecentEntries
    abstract fun pendingTimeSheetEntries() : PendingEntries
    abstract fun timeSheetDao() : TimeSheetTableDao


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