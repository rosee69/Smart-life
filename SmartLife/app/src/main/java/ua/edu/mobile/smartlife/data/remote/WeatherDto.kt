package ua.edu.mobile.smartlife.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherResponseDto(
    val current: CurrentWeatherDto,
    val daily: DailyWeatherDto
)

@Serializable
data class CurrentWeatherDto(
    @SerialName("temperature_2m")
    val temperature: Double,

    @SerialName("relative_humidity_2m")
    val humidity: Int,

    @SerialName("apparent_temperature")
    val apparentTemperature: Double,

    @SerialName("wind_speed_10m")
    val windSpeed: Double,

    @SerialName("weather_code")
    val weatherCode: Int
)

@Serializable
data class DailyWeatherDto(
    val time: List<String>,

    @SerialName("temperature_2m_max")
    val temperatureMax: List<Double>,

    @SerialName("temperature_2m_min")
    val temperatureMin: List<Double>,

    @SerialName("weather_code")
    val weatherCodes: List<Int>
)