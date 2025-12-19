// uygulamada sakladığım şehirlerin veritabanı karşılığını tutan entity sınıfı.
// şehir adı ve varsayılan olup olmadığını buradan kontrol ediyorum.

package me.ceydaguvercin.gokyuzum.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cities")
data class CityEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String
)
