package uk.co.timesheets24.app.TS24

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
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
import uk.co.timesheets24.app.TS24.Views.RecentEntries.RecentEntriesViewModel

@RunWith(AndroidJUnit4::class)
class RecentEntriesTest {

    private lateinit var db: LocalUserDatabase
    private lateinit var dao: AccessTokenDao
    private lateinit var entriesDao : RecentEntries
    private lateinit var context: Context
    private lateinit var token : String

    @Before
    fun setup() {
        runBlocking {
            context = ApplicationProvider.getApplicationContext<Context>()
            db = Room.inMemoryDatabaseBuilder(
                context,
                LocalUserDatabase::class.java
            ).allowMainThreadQueries().build()
            dao = db.accessTokenDao()
            entriesDao = db.recentEntriesDao()
            val tokenApi = AuthApiClass(context).authApi
            val call = tokenApi.authentication("mike.feely@outlook.com", "London2016#")
            token = call.access_token.toString()
            GlobalLookUp.token = token
            dao.insert(
                AccessTokenLocal(
                    accessToken = token,
                    refreshToken = call.refresh_token,
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
    fun fetchEntries() = runBlocking{

        val viewModel = RecentEntriesViewModel()
        viewModel.fetchRecentEntries(context, db)

        withTimeout(5000) {
            while (viewModel.recentEntriesList.isEmpty()) {
                delay(5000)
            }
        }

        assert(!viewModel.recentEntriesList.isEmpty())
    }

    @Test
    fun deleteEntry() = runBlocking{

        val viewModel = RecentEntriesViewModel()
        viewModel.fetchRecentEntries(context, db)

        withTimeout(5000) {
            while (viewModel.recentEntriesList.isEmpty()) {
                delay(5000)
            }
        }
        val size = viewModel.recentEntriesList.size

        viewModel.deleteRecentEntry(context, viewModel.recentEntriesList[0])

        delay(5000)

        assert(size > viewModel.recentEntriesList.size)
    }

}