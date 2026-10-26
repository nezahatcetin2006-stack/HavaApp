package com.kampplus.hava.ui.city

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonSyntaxException
import com.kampplus.hava.data.AqiRetrofitClient
import com.kampplus.hava.data.CityRepository
import com.kampplus.hava.data.OpenMeteoCityRepository
import com.kampplus.hava.model.AqiData
import com.kampplus.hava.model.City
import com.kampplus.hava.ui.LoadState
import java.io.IOException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class CityViewModel(
    private val repository: CityRepository = OpenMeteoCityRepository(),
) : ViewModel() {

    private val _query = MutableStateFlow(DEFAULT_CITY)
    val query: StateFlow<String> = _query.asStateFlow()

    private val _uiState = MutableStateFlow<LoadState<List<City>>>(LoadState.Loading)
    val uiState: StateFlow<LoadState<List<City>>> = _uiState.asStateFlow()

    private val _aqiState = MutableStateFlow<AqiData?>(null)
    val aqiState: StateFlow<AqiData?> = _aqiState.asStateFlow()

    init {
        search()
    }

    fun onQueryChange(newQuery: String) {
        _query.value = newQuery
    }

    fun search() {
        val currentQuery = _query.value.trim().ifEmpty { DEFAULT_CITY }

        viewModelScope.launch {
            _uiState.value = LoadState.Loading
            _uiState.value = try {
                val cities = repository.search(currentQuery)
                if (cities.isEmpty()) LoadState.Empty else LoadState.Success(cities)
            } catch (e: IOException) {
                Log.e(TAG, "Arama başarısız: bağlantı hatası", e)
                LoadState.Error("İnternet bağlantını kontrol et.")
            } catch (e: HttpException) {
                Log.e(TAG, "Arama başarısız: HTTP ${e.code()}", e)
                LoadState.Error("Sunucu şu an cevap vermiyor.")
            } catch (e: JsonSyntaxException) {
                Log.e(TAG, "Arama başarısız: yanıt çözümlenemedi", e)
                LoadState.Error("Bir şeyler ters gitti. Tekrar dene.")
            }
        }
    }

    fun fetchAirQuality(cityName: String) {
        viewModelScope.launch {
            try {
                val response = AqiRetrofitClient.apiService.getAirQualityByCity(
                    city = cityName.trim(),
                    token = WAQI_API_TOKEN,
                )
                if (response.status == "ok") {
                    _aqiState.value = response.data
                } else {
                    _aqiState.value = null
                }
            } catch (e: Exception) {
                Log.e(TAG, "AQI verisi alınamadı: ${e.message}", e)
                _aqiState.value = null
            }
        }
    }

    companion object {
        private const val TAG = "CityViewModel"
        private const val DEFAULT_CITY = "İstanbul"
        private const val WAQI_API_TOKEN = "f9aa67a3006dc462e707f101f7d1a1e1434097ba"
    }
}
