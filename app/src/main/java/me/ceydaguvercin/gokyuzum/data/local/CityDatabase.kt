// şehir kayıtlarını saklayan room veritabanı ve ilgili dao erişimi burada tanımlı.

package me.ceydaguvercin.gokyuzum.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CityEntity::class, WeatherEntity::class], version = 4, exportSchema = false)
abstract class CityDatabase : RoomDatabase() {
    abstract fun cityDao(): CityDao
    abstract fun weatherDao(): WeatherDao
}
