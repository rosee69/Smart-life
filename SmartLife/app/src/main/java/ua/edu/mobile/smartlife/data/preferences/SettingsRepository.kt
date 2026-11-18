package ua.edu.mobile.smartlife.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(
    name = "user_settings"
)

@Singleton
class SettingsRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object PreferencesKeys {
        val USER_NAME = stringPreferencesKey("user_name")
        val THEME = stringPreferencesKey("theme")
        val DAILY_WATER_GOAL = intPreferencesKey("daily_water_goal")
    }

    val userSettings: Flow<UserSettings> =
        context.dataStore.data.map { preferences ->
            UserSettings(
                userName = preferences[PreferencesKeys.USER_NAME] ?: "",
                theme = preferences[PreferencesKeys.THEME] ?: "SYSTEM",
                dailyWaterGoal = preferences[PreferencesKeys.DAILY_WATER_GOAL] ?: 2000
            )
        }

    suspend fun updateUserName(userName: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_NAME] = userName
        }
    }

    suspend fun updateTheme(theme: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.THEME] = theme
        }
    }

    suspend fun updateDailyWaterGoal(goal: Int) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.DAILY_WATER_GOAL] = goal
        }
    }
}
