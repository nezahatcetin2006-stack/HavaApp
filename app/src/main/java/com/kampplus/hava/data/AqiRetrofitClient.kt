package com.kampplus.hava.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AqiRetrofitClient {
    private const val BASE_URL = "https://api.waqi.info/"

    val apiService: AqiApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AqiApiService::class.java)
    }
}
