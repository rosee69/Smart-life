package ua.edu.mobile.smartlife.data.model

import kotlinx.serialization.Serializable
@Serializable
data class Weather(
    val temperature: Double,
    val humidity: Int,
    val apparentTemperature: Double,
    val windSpeed: Double,
    val weatherCode: Int,
    val forecast: List<DailyForecast>
)
@Serializable
data class DailyForecast(
    val date: String,
    val temperatureMax: Double,
    val temperatureMin: Double,
    val weatherCode: Int
)