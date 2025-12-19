package me.ceydaguvercin.gokyuzum.data.network

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("current")
    val current: Current
)

data class Current(
    @SerializedName("temperature_2m")
    val temperature: Double,
    @SerializedName("relative_humidity_2m")
    val humidity: Int,
    @SerializedName("weather_code")
    val weatherCode: Int,
    @SerializedName("wind_speed_10m")
    val windSpeed: Double,
    val time: String
)

object WeatherCodeUtils {
    fun getDescription(code: Int): String {
        return when (code) {
            0 -> "Açık"
            1, 2, 3 -> "Parçalı Bulutlu"
            45, 48 -> "Sisli"
            51, 53, 55 -> "Çiseleme"
            56, 57 -> "Dondurucu Çiseleme"
            61, 63, 65 -> "Yağmurlu"
            66, 67 -> "Dondurucu Yağmur"
            71, 73, 75 -> "Kar Yağışlı"
            77 -> "Kar Taneleri"
            80, 81, 82 -> "Sağanak Yağışlı"
            85, 86 -> "Kar Sağanağı"
            95 -> "Fırtına"
            96, 99 -> "Dolu Fırtınası"
            else -> "Bilinmiyor"
        }
    }
}
