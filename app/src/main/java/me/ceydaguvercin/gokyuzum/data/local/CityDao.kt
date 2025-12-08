// şehirleri listeleme, ekleme, silme ve tek bir varsayılan şehir belirleme işlemlerini burada topladım.

package me.ceydaguvercin.gokyuzum.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface CityDao {
    @Query("SELECT * FROM cities ORDER BY isDefault DESC, name ASC")
    fun getAllCities(): Flow<List<CityEntity>>

    @Insert
    suspend fun insertCity(city: CityEntity)

    @Delete
    suspend fun deleteCity(city: CityEntity)

    @Query("UPDATE cities SET isDefault = 0")
    suspend fun clearDefaults()

    @Query("UPDATE cities SET isDefault = 1 WHERE id = :cityId")
    suspend fun setCityDefault(cityId: Int)

    @Transaction
    suspend fun setDefaultCity(cityId: Int) {
        clearDefaults()
        setCityDefault(cityId)
    }
}
