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
import uk.co.timesheets24.app.TS24.LocalDataBase.Dao.JobsDao
import uk.co.timesheets24.app.TS24.LocalDataBase.LocalUserDatabase
import uk.co.timesheets24.app.TS24.LocalDataSevice.RefreshLocalData
import uk.co.timesheets24.app.TS24.Models.LocalData.AccessTokenLocal
import uk.co.timesheets24.app.TS24.Views.EditRecentEntry.EditRecentEntriesViewModel
import kotlin.random.Random

@RunWith(AndroidJUnit4::class)
class EditTimesheetTest {

    private lateinit var db: LocalUserDatabase
    private lateinit var dao: JobsDao
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
            dao = db.jobDao()
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
    fun editTimesheetTest() = runBlocking {

        val viewModel = EditRecentEntriesViewModel()
        GlobalLookUp.timeId = "068A9B17-F6E3-489C-B44D-1F3CD9C8B551"
        viewModel.fetchEntry(context)
        delay(5000)
        val randomInt = (1..100).random()
        viewModel.description.value = "description $randomInt"
        viewModel.editRecentEntry(context)
        delay(5000)
        val board = RefreshLocalData(context, db)
        board.DoWork()
        delay(5000)
        viewModel.fetchEntry(context)
        delay(5000)
        assert(viewModel.description.value == "description $randomInt")
    }

}