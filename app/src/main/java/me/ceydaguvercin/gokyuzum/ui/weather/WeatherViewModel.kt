package me.ceydaguvercin.gokyuzum.ui.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import me.ceydaguvercin.gokyuzum.data.network.Current
import me.ceydaguvercin.gokyuzum.data.repository.CityRepository
import me.ceydaguvercin.gokyuzum.data.repository.WeatherRepository

data class WeatherUiState(
    val cityName: String? = null,
    val weather: Current? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

class WeatherViewModel(
    private val weatherRepository: WeatherRepository,
    private val cityRepository: CityRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    init {
        observeDefaultCity()
    }

    private fun observeDefaultCity() {
        viewModelScope.launch {
            combine(
                cityRepository.allCities,
                cityRepository.defaultCityIdFlow
            ) { cities, defaultId ->
                cities.find { it.id == defaultId }
            }.collectLatest { defaultCity ->
                if (defaultCity != null) {
                    // Varsayılan şehir değişti veya ilk kez yüklendi
                    if (_uiState.value.cityName != defaultCity.name) {
                        fetchWeather(defaultCity.name)
                    }
                } else {
                    // Hiç şehir yok veya varsayılan yok 
                    // (veya silinmiş bir ID kaldıysa)
                     _uiState.value = WeatherUiState(error = "Varsayılan şehir seçili değil")
                }
            }
        }
    }

    private fun fetchWeather(cityName: String) {
        viewModelScope.launch {
            _uiState.value = WeatherUiState(cityName = cityName, isLoading = true)
            val result = weatherRepository.getWeatherForCity(cityName)
            
            result.onSuccess { weather ->
                _uiState.value = WeatherUiState(
                    cityName = cityName,
                    weather = weather,
                    isLoading = false
                )
            }.onFailure { exception ->
                _uiState.value = WeatherUiState(
                    cityName = cityName,
                    isLoading = false,
                    error = exception.message ?: "Hava durumu alınamadı"
                )
            }
        }
    }

    // ViewModel için Factory (Üretici) sınıfı
    class Factory(
        private val weatherRepository: WeatherRepository,
        private val cityRepository: CityRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return WeatherViewModel(weatherRepository, cityRepository) as T
            }
            throw IllegalArgumentException("Bilinmeyen ViewModel sınıfı")
        }
    }
}
