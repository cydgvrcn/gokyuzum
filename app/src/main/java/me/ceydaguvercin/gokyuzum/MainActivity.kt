// uygulamayı başlatırken veritabanını, repository'yi ve ViewModel'i ayağa kaldırdığım ana snıf.
// arayüz tarafında şehir listesini gösterecek ekranı burada başlatıyorum.


package me.ceydaguvercin.gokyuzum

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.room.Room
import me.ceydaguvercin.gokyuzum.data.local.CityDatabase
import me.ceydaguvercin.gokyuzum.data.repository.CityRepository
import me.ceydaguvercin.gokyuzum.ui.city.CityListScreen
import me.ceydaguvercin.gokyuzum.ui.city.CityViewModel
import me.ceydaguvercin.gokyuzum.ui.theme.GokyuzumTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Room.databaseBuilder(
            applicationContext,
            CityDatabase::class.java, "city-database"
        ).build()
        
        val repository = CityRepository(db.cityDao())
        val weatherRepository = me.ceydaguvercin.gokyuzum.data.repository.WeatherRepository()
        
        val viewModel: CityViewModel by viewModels {
            CityViewModel.Factory(repository)
        }

        val weatherViewModel: me.ceydaguvercin.gokyuzum.ui.weather.WeatherViewModel by viewModels {
            me.ceydaguvercin.gokyuzum.ui.weather.WeatherViewModel.Factory(weatherRepository, repository)
        }

        setContent {
            GokyuzumTheme {
                // Temadan gelen 'arka plan' rengini kullanan bir yüzey kapsayıcısı
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CityListScreen(viewModel = viewModel, weatherViewModel = weatherViewModel)
                }
            }
        }
    }
}