package uk.co.timesheets24.app.TS24.UI.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import uk.co.timesheets24.app.TS24.GlobalLookUp

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

@Composable
fun TS24Theme(

    content: @Composable () -> Unit

) {



    val LightColorScheme = lightColorScheme(
        primary = GlobalLookUp.primaryColor,
        secondary = GlobalLookUp.secondary,
        tertiary = GlobalLookUp.tertiary,
        background = GlobalLookUp.background,
        onPrimary = GlobalLookUp.onPrimary,
        onSecondary = GlobalLookUp.onSecondary,
        onTertiary = GlobalLookUp.onTertiary,
        error = GlobalLookUp.error,
        onBackground = Color.White,
    )



        val colorScheme = LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}