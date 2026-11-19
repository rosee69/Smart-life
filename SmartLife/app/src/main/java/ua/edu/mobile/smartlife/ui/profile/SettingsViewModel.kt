package ua.edu.mobile.smartlife.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ua.edu.mobile.smartlife.data.preferences.SettingsRepository
import ua.edu.mobile.smartlife.data.preferences.UserSettings
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val userSettings: StateFlow<UserSettings> =
        settingsRepository.userSettings.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserSettings()
        )

    fun updateUserName(userName: String) {
        viewModelScope.launch {
            settingsRepository.updateUserName(userName)
        }
    }

    fun updateTheme(theme: String) {
        viewModelScope.launch {
            settingsRepository.updateTheme(theme)
        }
    }

    fun updateDailyWaterGoal(goal: Int) {
        viewModelScope.launch {
            settingsRepository.updateDailyWaterGoal(goal)
        }
    }
}