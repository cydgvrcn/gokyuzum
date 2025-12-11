// uygulamadaki şehir yönetimi arayüzünü topladığım ekran.
// şehir listesini gösterme, yeni şehir ekleme ve varsayılan/silme işlemlerini compose ile burada çözüyorum.


package me.ceydaguvercin.gokyuzum.ui.city

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import me.ceydaguvercin.gokyuzum.data.local.CityEntity
import me.ceydaguvercin.gokyuzum.ui.weather.WeatherCard
import me.ceydaguvercin.gokyuzum.ui.weather.WeatherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityListScreen(
    viewModel: CityViewModel,
    weatherViewModel: me.ceydaguvercin.gokyuzum.ui.weather.WeatherViewModel, // Tam yol veya import eklenebilir
    modifier: Modifier = Modifier
) {
    // ViewModel üzerinden gelen şehir listesini dinliyoruz (StateFlow -> State)
    val cities by viewModel.cities.collectAsState()
    val weatherUiState by weatherViewModel.uiState.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Şehirlerim") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Şehir Ekle")
            }
        }
    ) { innerPadding ->
        Column(modifier = modifier.padding(innerPadding)) {
            me.ceydaguvercin.gokyuzum.ui.weather.WeatherCard(
                uiState = weatherUiState
            )
            
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
            items(cities) { city ->
                CityItem(
                    city = city,
                    onSetDefault = { viewModel.setDefaultCity(city) },
                    onDelete = { viewModel.deleteCity(city) }
                )
            }
        }

            }
        }

        if (showAddDialog) {
            AddCityDialog(
                onDismiss = { showAddDialog = false },
                onConfirm = { name ->
                    viewModel.addCity(name)
                    showAddDialog = false
                }
            )
        }
    }

@Composable
fun CityItem(
    city: CityEntity,
    onSetDefault: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = city.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onSetDefault) {
                    Icon(
                        imageVector = if (city.isDefault) Icons.Filled.Star else Icons.Outlined.Star,
                        contentDescription = "Varsayılan Yap",
                        tint = if (city.isDefault) Color(0xFFFFD700) else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Sil",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
fun AddCityDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Yeni Şehir Ekle") },
        text = {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Şehir Adı") },
                singleLine = true
            )
        },
        confirmButton = {
            TextButton(
                onClick = { 
                    if (text.isNotBlank()) onConfirm(text) 
                }
            ) {
                Text("Ekle")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("İptal")
            }
        }
    )
}
