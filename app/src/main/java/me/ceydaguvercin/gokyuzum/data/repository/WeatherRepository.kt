package me.ceydaguvercin.gokyuzum.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.ceydaguvercin.gokyuzum.data.network.Current
import me.ceydaguvercin.gokyuzum.data.network.RetrofitClient
import android.content.SharedPreferences
import me.ceydaguvercin.gokyuzum.data.local.WeatherDao
import me.ceydaguvercin.gokyuzum.data.local.WeatherEntity

class WeatherRepository(
    private val weatherDao: WeatherDao,
    private val sharedPreferences: SharedPreferences
) {
    private val weatherApi = RetrofitClient.api
    private val geocodingApi = RetrofitClient.geocodingApi

    suspend fun getWeatherForCity(cityName: String): Result<Current> {
        return withContext(Dispatchers.IO) {
            try {
                // 1. Şehir isminden koordinat bul (Geocoding)
                val searchResponse = geocodingApi.searchCity(name = cityName)
                val location = searchResponse.results?.firstOrNull()

                if (location != null) {
                    // 2. Bulunan koordinatla hava durumu çek
                    val weatherResponse = weatherApi.getWeather(location.latitude, location.longitude)
                    
                    // Veritabanına kaydet (OfflineCache)
                    val entity = WeatherEntity(
                        cityName = cityName,
                        temperature = weatherResponse.current.temperature,
                        windspeed = weatherResponse.current.windSpeed,
                        humidity = weatherResponse.current.humidity,
                        weathercode = weatherResponse.current.weatherCode,
                        time = weatherResponse.current.time
                    )
                    weatherDao.insertWeather(entity)
                    
                    // Son güncelleme zamanını SharedPref'e yaz
                    sharedPreferences.edit().putLong("last_update_$cityName", System.currentTimeMillis()).apply()

                    Result.success(weatherResponse.current)
                } else {
                    Result.failure(Exception("Şehir bulunamadı: $cityName"))
                }
            } catch (e: Exception) {
                // İnternet yoksa veya hata varsa veritabanından çek
                val cached = weatherDao.getWeather(cityName)
                if (cached != null) {
                    val weather = Current(
                        temperature = cached.temperature,
                        windSpeed = cached.windspeed,
                        humidity = cached.humidity,
                        weatherCode = cached.weathercode,
                        time = cached.time
                    )
                    Result.success(weather)
                } else {
                    Result.failure(e)
                }
            }
        }
    }
}
