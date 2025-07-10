package uk.co.timesheets24.app.TS24

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import uk.co.timesheets24.app.TS24.API.AuthApiClass
import uk.co.timesheets24.app.TS24.LocalDataBase.Dao.AccessTokenDao
import uk.co.timesheets24.app.TS24.LocalDataBase.Dao.DashboardDao
import uk.co.timesheets24.app.TS24.LocalDataBase.LocalUserDatabase
import uk.co.timesheets24.app.TS24.LocalDataSevice.RefreshLocalData
import uk.co.timesheets24.app.TS24.Models.LocalData.AccessTokenLocal
import uk.co.timesheets24.app.TS24.Views.Dashboard.DashboardViewModel

@RunWith(AndroidJUnit4::class)
class DashboardTest {
    // dashboard data is fetched and displayed,
    // current screen changes with the enum class values
    // sync button works

    private lateinit var db: LocalUserDatabase
    private lateinit var dao: DashboardDao
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
            dao = db.dashboardDao()
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
    fun fetchingDashboardData() = runBlocking {

        val viewModel = DashboardViewModel()
        viewModel.fetchJobDetails(context, db)

        withTimeout(5000) {
            while (viewModel._userHours.value == null) {
                delay(5000)
            }
        }

        TestCase.assertNotNull(viewModel._userHours.value)
    }

}