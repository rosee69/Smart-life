
package ua.edu.mobile.smartlife.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ua.edu.mobile.smartlife.data.model.Weather
import ua.edu.mobile.smartlife.data.model.DailyForecast

private val WeatherAccent = Color(0xFFF600BE)

@Composable
fun WeatherCard(
    state: WeatherUiState,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Погода",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            when (state) {
                is WeatherUiState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = WeatherAccent)
                    }
                }

                is WeatherUiState.Error -> {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                is WeatherUiState.Success -> {
                    CurrentWeather(
                        weather = state.weather,
                        placeLabel = state.placeLabel
                    )

                    if (state.isFromCache) {
                        Text(
                            text = "Показано збережені дані — немає з'єднання з інтернетом",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    HorizontalDivider()

                    Text(
                        text = "Прогноз на 3 дні",
                        fontWeight = FontWeight.SemiBold
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        state.weather.forecast.take(3).forEach { day ->
                            ForecastItem(day)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CurrentWeather(
    weather: Weather,
    placeLabel: String
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = placeLabel,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = weatherEmoji(weather.weatherCode),
                style = MaterialTheme.typography.displayMedium
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = "${weather.temperature.toInt()}°C",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = weatherDescription(weather.weatherCode),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Text("Відчувається як: ${weather.apparentTemperature.toInt()}°C")
        Text("Вологість: ${weather.humidity}%")
        Text("Вітер: ${weather.windSpeed} км/год")
    }
}

@Composable
private fun ForecastItem(day: DailyForecast) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Text(
            text = day.date.takeLast(5).reversed()
                .replace("-", ".")
        )

        Text(
            text = weatherEmoji(day.weatherCode),
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = "${day.temperatureMax.toInt()}°",
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "${day.temperatureMin.toInt()}°",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

private fun weatherEmoji(code: Int): String {
    return when (code) {
        0 -> "☀️"
        1, 2 -> "🌤️"
        3 -> "☁️"
        45, 48 -> "🌫️"
        in 51..67 -> "🌧️"
        in 71..77 -> "❄️"
        in 80..82 -> "🌦️"
        in 85..86 -> "🌨️"
        in 95..99 -> "⛈️"
        else -> "🌡️"
    }
}

private fun weatherDescription(code: Int): String {
    return when (code) {
        0 -> "Ясно"
        1 -> "Переважно ясно"
        2 -> "Мінлива хмарність"
        3 -> "Хмарно"
        45, 48 -> "Туман"
        in 51..57 -> "Мряка"
        in 61..67 -> "Дощ"
        in 71..77 -> "Сніг"
        in 80..82 -> "Зливи"
        in 85..86 -> "Снігові зливи"
        in 95..99 -> "Гроза"
        else -> "Поточна погода"
    }
}
