package me.ceydaguvercin.gokyuzum.ui.weather

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import me.ceydaguvercin.gokyuzum.data.network.CurrentWeather

@Composable
fun WeatherCard(
    uiState: WeatherUiState,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Hava Durumu",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))

            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                uiState.error != null -> {
                    Text(
                        text = uiState.error,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                uiState.weather != null && uiState.cityName != null -> {
                    WeatherContent(cityName = uiState.cityName, weather = uiState.weather)
                }
                else -> {
                    Text(text = "Varsayılan şehir seçin")
                }
            }
        }
    }
}

@Composable
private fun WeatherContent(cityName: String, weather: CurrentWeather) {
    Column {
        Text(
            text = cityName,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Text(
                    text = "${weather.temperature}°C",
                    style = MaterialTheme.typography.displayMedium
                )
                Text(
                    text = "Sıcaklık",
                    style = MaterialTheme.typography.labelMedium
                )
            }
            Column {
                Text(
                    text = "${weather.windspeed} km/s",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "Rüzgar",
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Son Güncelleme: ${weather.time}",
            style = MaterialTheme.typography.bodySmall
        )
    }
}
