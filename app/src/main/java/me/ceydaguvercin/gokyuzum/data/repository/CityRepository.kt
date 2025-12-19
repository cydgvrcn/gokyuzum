// şehirlerle ilgili tüm veri işlemlerini tek bir yerde topladığım repository katmanı.
// bu sınıf dao ile ui(arayüz) arasında köprü görevi görüyor; ekleme, silme ve varsayılan şehir atama işlemleri buradan geçiyor.


package me.ceydaguvercin.gokyuzum.data.repository

import android.content.SharedPreferences
import kotlinx.coroutines.flow.Flow
import me.ceydaguvercin.gokyuzum.data.local.CityDao
import me.ceydaguvercin.gokyuzum.data.local.CityEntity

class CityRepository(
    private val cityDao: CityDao,
    private val sharedPreferences: SharedPreferences
) {
    val allCities: Flow<List<CityEntity>> = cityDao.getAllCities()

    private val _defaultCityIdFlow = kotlinx.coroutines.flow.MutableStateFlow(getDefaultCityId())
    val defaultCityIdFlow: kotlinx.coroutines.flow.StateFlow<Int> = _defaultCityIdFlow

    suspend fun addCity(name: String) {
        val city = CityEntity(name = name)
        cityDao.insertCity(city)
    }

    suspend fun deleteCity(city: CityEntity) {
        cityDao.deleteCity(city)
    }

    fun setDefaultCity(cityId: Int) {
        sharedPreferences.edit().putInt("default_city_id", cityId).apply()
        _defaultCityIdFlow.value = cityId
    }

    fun getDefaultCityId(): Int {
        return sharedPreferences.getInt("default_city_id", -1)
    }
}
