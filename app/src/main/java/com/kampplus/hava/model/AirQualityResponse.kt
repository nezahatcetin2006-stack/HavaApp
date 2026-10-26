package com.kampplus.hava.model

import com.google.gson.annotations.SerializedName

data class AirQualityResponse(
    val status: String,
    val data: AqiData?
)

data class AqiData(
    val aqi: Int,
    val idx: Int,
    val city: CityInfo?,
    val dominentpol: String?,
    val iaqi: IaqiMetrics?
)

data class CityInfo(
    val name: String,
    val geo: List<Double>?
)

data class IaqiMetrics(
    val pm25: PollutantValue?,
    val pm10: PollutantValue?,
    val o3: PollutantValue?,
    val no2: PollutantValue?,
    val so2: PollutantValue?,
    val co: PollutantValue?
)

data class PollutantValue(
    @SerializedName("v") val value: Double
)
