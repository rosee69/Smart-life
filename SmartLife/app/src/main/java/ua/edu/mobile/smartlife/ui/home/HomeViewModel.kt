package ua.edu.mobile.smartlife.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ua.edu.mobile.smartlife.data.remote.WeatherRepository
import ua.edu.mobile.smartlife.data.remote.toUserMessage
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private val _weatherState =
        MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)

    val weatherState: StateFlow<WeatherUiState> =
        _weatherState.asStateFlow()

    fun loadWeather() {
        viewModelScope.launch {
            _weatherState.value = WeatherUiState.Loading

            try {
                val result = weatherRepository.getCurrentWeather(
                    latitude = ODESA_LATITUDE,
                    longitude = ODESA_LONGITUDE
                )

                _weatherState.value = WeatherUiState.Success(
                    weather = result.weather,
                    placeLabel = "Одеса",
                    isFromCache = result.isFromCache
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _weatherState.value = WeatherUiState.Error(
                    e.toUserMessage()
                )
            }
        }
    }

    companion object {
        private const val ODESA_LATITUDE = 46.48
        private const val ODESA_LONGITUDE = 30.72
    }
}