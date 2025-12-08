// şehir listesinin ui(arayüz) tarafında ihtiyaç duyduğu tüm işlemleri yöneten ViewModel.
// ekleme, silme ve varsayılan şehir seçimlerini repository üzerinden burada koordine ediyorum.

package me.ceydaguvercin.gokyuzum.ui.city

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.ceydaguvercin.gokyuzum.data.local.CityEntity
import me.ceydaguvercin.gokyuzum.data.repository.CityRepository

class CityViewModel(private val repository: CityRepository) : ViewModel() {

    val cities: StateFlow<List<CityEntity>> = repository.allCities
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addCity(name: String) {
        viewModelScope.launch {
            repository.addCity(name)
        }
    }

    fun deleteCity(city: CityEntity) {
        viewModelScope.launch {
            repository.deleteCity(city)
        }
    }

    fun setDefaultCity(city: CityEntity) {
        viewModelScope.launch {
            repository.setDefaultCity(city)
        }
    }


    class Factory(private val repository: CityRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CityViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CityViewModel(repository) as T
            }
            throw IllegalArgumentException("Bilinmeyen ViewModel sınıfı")
        }
    }
}
