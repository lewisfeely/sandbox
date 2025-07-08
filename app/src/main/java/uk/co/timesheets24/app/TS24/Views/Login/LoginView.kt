package uk.co.timesheets24.app.TS24.Views.Login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity

import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import uk.co.timesheets24.app.TS24.UI.theme.TSBlue
import uk.co.timesheets24.app.TS24.UI.theme.TS24Theme
import uk.co.timesheets24.app.TS24.R

import kotlin.jvm.java

class LoginView : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel : LoginScreenViewModel = viewModel()
            val context : Context = this
            val lifecycleOwner : LifecycleOwner = LocalLifecycleOwner.current

            TS24Theme {
                LoginPage(viewModel, context, lifecycleOwner)
            }
        }
    }
}

@Composable

fun LoginPage(viewModel: LoginScreenViewModel, context: Context, lifecycleOwner: LifecycleOwner) {

    val theme = MaterialTheme.colorScheme
    val email = remember { mutableStateOf("mike.feely@outlook.com") }
    val password = remember { mutableStateOf("London2016#") }
    val loadingMessageState = remember { mutableStateOf("") }


    LaunchedEffect(Unit) {
        viewModel.checkRefreshToken(context)
    }


    Column (modifier = Modifier.fillMaxSize().background(theme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top)
    {
        Spacer(Modifier.height(180.dp))
        Image(
            painter = painterResource(R.drawable.timesheets_blue),
            contentDescription = "ts24 logo",
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(180.dp)
        )
        Spacer(Modifier.height(50.dp))
        Text("Welcome!", color = theme.onPrimary, fontSize = 30.sp)

        Spacer(Modifier.height(15.dp))
            TextField(
                value = email.value,
                onValueChange = { email.value = it },
                placeholder = { Text("email") },
                modifier = Modifier.width(250.dp).border(width = 1.dp, color = theme.onPrimary).testTag("email_Field"),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = theme.background,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = theme.onPrimary,
                    cursorColor = Color.Blue,
                    focusedLabelColor = Color.Blue,
                    unfocusedPlaceholderColor = Color.White
                )
            )
            Spacer(modifier = Modifier.height(10.dp))
            TextField(
                value = password.value,
                onValueChange = { password.value = it },
                placeholder = { Text("password") },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = theme.background,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = theme.onPrimary,
                    cursorColor = Color.Blue,
                    focusedLabelColor = Color.Blue,
                    unfocusedPlaceholderColor = Color.White
                ),
                modifier = Modifier.width(250.dp).border(width = 1.dp, color = theme.onPrimary).testTag("password_Field")

            )
        Spacer(Modifier.height(20.dp))
        if (viewModel.loading.value != true) {
            Button(
                onClick = {
                    viewModel.processLogin(context, email.value, password.value)
                },
                colors = ButtonDefaults.buttonColors(
                    contentColor = theme.onPrimary,
                    containerColor = theme.primary,
                    disabledContainerColor = TSBlue
                ),
                modifier = Modifier.border(width = 2.dp, color = theme.onPrimary).testTag("login_Button")
            ) {
                Text("Submit")
            }
            if(viewModel._error.value) {
                Text("error has occured please try again", color = theme.error)
            }
        } else if (viewModel.loading.value) {
            CircularProgressIndicator(color = Color.White)
            Text(loadingMessageState.value, color = Color.White)
            viewModel.state.observe(lifecycleOwner) { loadingState ->
                loadingMessageState.value = loadingState
            }

        }

        Spacer(Modifier.height(20.dp))

    }

}
