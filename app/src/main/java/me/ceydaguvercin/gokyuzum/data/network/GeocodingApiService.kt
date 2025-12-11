package me.ceydaguvercin.gokyuzum.data.network

import retrofit2.http.GET
import retrofit2.http.Query

interface GeocodingApiService {
    @GET("v1/search")
    suspend fun searchCity(
        @Query("name") name: String,
        @Query("count") count: Int = 1,
        @Query("language") language: String = "tr",
        @Query("format") format: String = "json"
    ): GeocodingResponse
}
