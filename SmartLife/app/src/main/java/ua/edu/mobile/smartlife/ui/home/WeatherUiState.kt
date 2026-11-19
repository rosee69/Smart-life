package ua.edu.mobile.smartlife.ui.home

import ua.edu.mobile.smartlife.data.model.Weather

sealed interface WeatherUiState {
    data object Loading : WeatherUiState

    data class Success(
        val weather: Weather,
        val placeLabel: String,
        val isFromCache: Boolean = false
    ) : WeatherUiState

    data class Error(
        val message: String
    ) : WeatherUiState
}