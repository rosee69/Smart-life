package ua.edu.mobile.smartlife

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import ua.edu.mobile.smartlife.ui.navigation.SmartLifeApp
import ua.edu.mobile.smartlife.ui.theme.SmartlifeTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.hilt.navigation.compose.hiltViewModel
import ua.edu.mobile.smartlife.ui.profile.SettingsViewModel
import androidx.compose.runtime.getValue

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val settingsViewModel: SettingsViewModel = hiltViewModel()
            val userSettings by settingsViewModel.userSettings.collectAsStateWithLifecycle()

            val darkTheme = when (userSettings.theme) {
                "DARK" -> true
                "LIGHT" -> false
                else -> isSystemInDarkTheme()
            }

            SmartlifeTheme(
                darkTheme = darkTheme,
                dynamicColor = false
            ) {
                SmartLifeApp()
            }
        }
    }
}