package uk.co.timesheets24.app.TS24

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import uk.co.timesheets24.app.TS24.API.AuthApiClass
import uk.co.timesheets24.app.TS24.LocalDataBase.Dao.AccessTokenDao
import uk.co.timesheets24.app.TS24.LocalDataBase.Dao.RecentEntries
import uk.co.timesheets24.app.TS24.LocalDataBase.LocalUserDatabase
import uk.co.timesheets24.app.TS24.LocalDataSevice.RefreshLocalData
import uk.co.timesheets24.app.TS24.Models.LocalData.AccessTokenLocal
import uk.co.timesheets24.app.TS24.Views.CreateTimesheet.CreateTimesheetViewModel

@RunWith(AndroidJUnit4::class)
class CreateTimesheetTest {

    private lateinit var db: LocalUserDatabase
    private lateinit var dao: RecentEntries
    private lateinit var tokenDao : AccessTokenDao
    private lateinit var context: Context
    private lateinit var token : String

    @Before
    fun setup() {
        runBlocking {
            context = ApplicationProvider.getApplicationContext<Context>()
            db = Room.inMemoryDatabaseBuilder(context, LocalUserDatabase::class.java)
                .allowMainThreadQueries()
                .build()
            dao = db.recentEntriesDao()
            tokenDao = db.accessTokenDao()
            val authApi = AuthApiClass(context).authApi
            val authenticationRequest =
                authApi.authentication("mike.feely@outlook.com", "London2016#")
            token = authenticationRequest.access_token.toString()
            GlobalLookUp.token = token
            tokenDao.insert(
                AccessTokenLocal(
                    accessToken = token,
                    refreshToken = authenticationRequest.refresh_token,
                    timeCreated = "2025-07-04 14:43:00"
                )
            )
            val board = RefreshLocalData(context, db)
            board.DoWork()
        }
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun createTimesheet() = runBlocking {

        val viewModel = CreateTimesheetViewModel()

        viewModel.startingSelectedDateTime.value = "2025-07-11 09:35:00"
        viewModel.finishSelectedDateTime.value = "2025-07-11 09:35:00"
        val originalSize = dao.fetchEntries().size
        viewModel.sendTimeSheet(context)
        delay(5000)
        val board = RefreshLocalData(context, db)
        board.DoWork()
        delay(5000)
        val newSize = dao.fetchEntries().size

        assert(originalSize < newSize)

    }

}