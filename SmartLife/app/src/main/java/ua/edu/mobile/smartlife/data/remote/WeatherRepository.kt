package ua.edu.mobile.smartlife.data.remote

import kotlinx.coroutines.CancellationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ua.edu.mobile.smartlife.data.model.DailyForecast
import ua.edu.mobile.smartlife.data.model.Weather
import ua.edu.mobile.smartlife.data.preferences.SettingsRepository
import javax.inject.Inject
import javax.inject.Singleton

data class WeatherResult(
    val weather: Weather,
    val isFromCache: Boolean
)

@Singleton
class WeatherRepository @Inject constructor(
    private val weatherApi: WeatherApi,
    private val settingsRepository: SettingsRepository,
    private val json: Json
) {

    suspend fun getCurrentWeather(
        latitude: Double,
        longitude: Double
    ): WeatherResult {

        val response = try {
            weatherApi.getCurrentWeather(
                latitude = latitude,
                longitude = longitude
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            val cachedJson = settingsRepository.getWeatherCache()

            val cachedWeather = cachedJson?.let {
                runCatching {
                    json.decodeFromString<Weather>(it)
                }.getOrNull()
            }

            if (cachedWeather != null) {
                return WeatherResult(
                    weather = cachedWeather,
                    isFromCache = true
                )
            }

            throw e
        }

        val weather = Weather(
            temperature = response.current.temperature,
            humidity = response.current.humidity,
            apparentTemperature = response.current.apparentTemperature,
            windSpeed = response.current.windSpeed,
            weatherCode = response.current.weatherCode,
            forecast = response.daily.time.indices.map { index ->
                DailyForecast(
                    date = response.daily.time[index],
                    temperatureMax = response.daily.temperatureMax[index],
                    temperatureMin = response.daily.temperatureMin[index],
                    weatherCode = response.daily.weatherCodes[index]
                )
            }
        )

        settingsRepository.saveWeatherCache(
            json.encodeToString(weather)
        )

        return WeatherResult(
            weather = weather,
            isFromCache = false
        )
    }
}