package ua.edu.mobile.smartlife.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("v1/forecast")
    suspend fun getCurrentWeather(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current") current: String =
            "temperature_2m,relative_humidity_2m,apparent_temperature,wind_speed_10m,weather_code",
        @Query("daily") daily: String =
            "temperature_2m_max,temperature_2m_min,weather_code",
        @Query("forecast_days") forecastDays: Int = 3,
        @Query("timezone") timezone: String = "auto"
    ): WeatherResponseDto
}