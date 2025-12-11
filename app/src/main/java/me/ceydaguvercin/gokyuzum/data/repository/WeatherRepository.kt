package me.ceydaguvercin.gokyuzum.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.ceydaguvercin.gokyuzum.data.network.CurrentWeather
import me.ceydaguvercin.gokyuzum.data.network.RetrofitClient

class WeatherRepository {
    private val weatherApi = RetrofitClient.api
    private val geocodingApi = RetrofitClient.geocodingApi

    suspend fun getWeatherForCity(cityName: String): Result<CurrentWeather> {
        return withContext(Dispatchers.IO) {
            try {
                // 1. Şehir isminden koordinat bul (Geocoding)
                val searchResponse = geocodingApi.searchCity(name = cityName)
                val location = searchResponse.results?.firstOrNull()

                if (location != null) {
                    // 2. Bulunan koordinatla hava durumu çek
                    val weatherResponse = weatherApi.getWeather(location.latitude, location.longitude)
                    Result.success(weatherResponse.currentWeather)
                } else {
                    Result.failure(Exception("Şehir bulunamadı: $cityName"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
