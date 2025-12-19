package me.ceydaguvercin.gokyuzum.ui.weather

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import me.ceydaguvercin.gokyuzum.data.network.Current
import me.ceydaguvercin.gokyuzum.data.network.WeatherCodeUtils

import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Grain
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Water
import androidx.compose.ui.Alignment
import me.ceydaguvercin.gokyuzum.ui.theme.SkyBlueStart
import me.ceydaguvercin.gokyuzum.ui.theme.SkyBlueEnd
import me.ceydaguvercin.gokyuzum.ui.theme.SkyTextWhite

@Composable
fun WeatherCard(
    uiState: WeatherUiState,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent) // Transparent to show gradient
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(SkyBlueStart, SkyBlueEnd)
                    )
                )
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Hava Durumu",
                    style = MaterialTheme.typography.titleMedium,
                    color = SkyTextWhite.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.height(16.dp))

                when {
                    uiState.isLoading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = SkyTextWhite
                        )
                    }
                    uiState.error != null -> {
                        Text(
                            text = uiState.error,
                            color = SkyTextWhite,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    uiState.weather != null && uiState.cityName != null -> {
                        WeatherContent(cityName = uiState.cityName, weather = uiState.weather)
                    }
                    else -> {
                        Text(text = "Varsayılan şehir seçin", color = SkyTextWhite)
                    }
                }
            }
        }
    }
}



@Composable
private fun WeatherContent(cityName: String, weather: Current) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = cityName,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = SkyTextWhite
                )
                Text(
                    text = WeatherCodeUtils.getDescription(weather.weatherCode),
                    style = MaterialTheme.typography.titleMedium,
                    color = SkyTextWhite.copy(alpha = 0.9f)
                )
            }
            // Dinamik İkon
            Icon(
                imageVector = getWeatherIcon(weather.weatherCode),
                contentDescription = null,
                tint = SkyTextWhite,
                modifier = Modifier.size(64.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "${weather.temperature}°",
                    style = MaterialTheme.typography.displayLarge, // Daha büyük
                    fontWeight = FontWeight.Thin, // Daha ince ve zarif
                    color = SkyTextWhite
                )
                Text(
                    text = "Sıcaklık",
                    style = MaterialTheme.typography.labelLarge,
                    color = SkyTextWhite.copy(alpha = 0.7f)
                )
            }
            
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "%${weather.humidity}",
                    style = MaterialTheme.typography.headlineMedium,
                    color = SkyTextWhite
                )
                Text(
                    text = "Nem",
                    style = MaterialTheme.typography.labelLarge,
                    color = SkyTextWhite.copy(alpha = 0.7f)
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "${weather.windSpeed}",
                    style = MaterialTheme.typography.headlineMedium,
                    color = SkyTextWhite
                )
                Text(
                    text = "Rüzgar (km/s)",
                    style = MaterialTheme.typography.labelLarge,
                    color = SkyTextWhite.copy(alpha = 0.7f)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Son Güncelleme: ${weather.time.substringAfter("T")}", // Sadece saati gösterelim
            style = MaterialTheme.typography.bodySmall,
            color = SkyTextWhite.copy(alpha = 0.6f),
            modifier = Modifier.align(Alignment.End)
        )
    }
}

private fun getWeatherIcon(code: Int): androidx.compose.ui.graphics.vector.ImageVector {
    return when (code) {
        0 -> Icons.Default.WbSunny
        1, 2, 3 -> Icons.Default.Cloud
        45, 48 -> Icons.Default.Cloud // Fog ikonu yoksa Cloud
        51, 53, 55, 61, 63, 65, 80, 81, 82 -> Icons.Default.Grain // Rain
        71, 73, 75, 77, 85, 86 -> Icons.Default.AcUnit // Snow
        95, 96, 99 -> Icons.Default.Warning // Thunderstorm yerine warning veya varsa Flash
        else -> Icons.Default.Cloud
    }
}
