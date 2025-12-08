// şehirlerle ilgili tüm veri işlemlerini tek bir yerde topladığım repository katmanı.
// bu sınıf dao ile ui(arayüz) arasında köprü görevi görüyor; ekleme, silme ve varsayılan şehir atama işlemleri buradan geçiyor.


package me.ceydaguvercin.gokyuzum.data.repository

import kotlinx.coroutines.flow.Flow
import me.ceydaguvercin.gokyuzum.data.local.CityDao
import me.ceydaguvercin.gokyuzum.data.local.CityEntity

class CityRepository(private val cityDao: CityDao) {
    val allCities: Flow<List<CityEntity>> = cityDao.getAllCities()

    suspend fun addCity(name: String) {
        // ileride eklenen ilk şehir otomatik varsayılan olarak tanımlanabilir,
        // ancak şimdilik, sadece bir varsayılan olabilir ve bu manuel seçilebilir.
        val city = CityEntity(name = name, isDefault = false)
        cityDao.insertCity(city)
    }

    suspend fun deleteCity(city: CityEntity) {
        cityDao.deleteCity(city)
    }

    suspend fun setDefaultCity(city: CityEntity) {
        cityDao.setDefaultCity(city.id)
    }
}
