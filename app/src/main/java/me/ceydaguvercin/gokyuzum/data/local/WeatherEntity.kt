package me.ceydaguvercin.gokyuzum.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_cache")
data class WeatherEntity(
    @PrimaryKey
    val cityName: String,
    val temperature: Double,
    val windspeed: Double,
    val humidity: Int,
    val weathercode: Int,
    val time: String
)
