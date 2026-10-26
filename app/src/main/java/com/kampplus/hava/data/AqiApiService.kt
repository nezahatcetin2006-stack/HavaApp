package com.kampplus.hava.data

import com.kampplus.hava.model.AirQualityResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AqiApiService {
    @GET("feed/{city}/")
    suspend fun getAirQualityByCity(@Path("city") city: String, @Query("token") token: String): AirQualityResponse
}
