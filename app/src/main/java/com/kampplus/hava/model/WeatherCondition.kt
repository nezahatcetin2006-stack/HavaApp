package com.kampplus.hava.model

import com.kampplus.hava.ui.AppLanguage

/**
 * Hava durumunun ekrandaki karşılığı: bir emoji, Türkçe ve İngilizce etiketler.
 *
 * Open-Meteo, sıcaklığın yanında bir de `weather_code` gönderir. Bu kod WMO 4677
 * standardındadır ve tek başına bir sayıdır (0, 61, 95...). Kullanıcıya sayı
 * gösteremeyiz; [fromWeatherCode] ile anlaşılır bir görünüme çeviririz.
 */
enum class WeatherCondition(
    val emoji: String,
    val labelTr: String,
    val labelEn: String
) {
    CLEAR("☀️", "Açık", "Clear"),
    MAINLY_CLEAR("🌤️", "Az Bulutlu", "Mainly Clear"),
    PARTLY_CLOUDY("⛅", "Parçalı Bulutlu", "Partly Cloudy"),
    OVERCAST("☁️", "Kapalı", "Overcast"),
    FOG("🌫️", "Sisli", "Fog"),
    DRIZZLE("🌦️", "Çisenti", "Drizzle"),
    RAIN("🌧️", "Yağmurlu", "Rainy"),
    SNOW("🌨️", "Karlı", "Snowy"),
    RAIN_SHOWERS("🌦️", "Sağanak Yağış", "Rain Showers"),
    SNOW_SHOWERS("🌨️", "Kar Sağanağı", "Snow Showers"),
    THUNDERSTORM("⛈️", "Gök Gürültülü Fırtına", "Thunderstorm"),
    UNKNOWN("🌡️", "Bilinmiyor", "Unknown");

    /** Eski kodlarla uyumluluk için varsayılan Türkçe label */
    val label: String
        get() = labelTr

    /** Seçilen dile (Türkçe / İngilizce) göre doğru etiketi döndürür */
    fun getLabel(lang: AppLanguage): String {
        return if (lang == AppLanguage.ENGLISH) labelEn else labelTr
    }

    companion object {
        /**
         * WMO 4677 kod tablosu. `when` ifadesi birden çok kodu tek bir görünüme eşler
         * (örneğin 61, 63 ve 65 hafif/orta/şiddetli yağmurdur; üçü de "Yağmurlu").
         *
         * Tanınmayan bir kod gelirse uygulama çökmez, [UNKNOWN] ile devam eder.
         */
        fun fromWeatherCode(code: Int): WeatherCondition = when (code) {
            0 -> CLEAR
            1 -> MAINLY_CLEAR
            2 -> PARTLY_CLOUDY
            3 -> OVERCAST
            45, 48 -> FOG
            51, 53, 55, 56, 57 -> DRIZZLE
            61, 63, 65, 66, 67 -> RAIN
            71, 73, 75, 77 -> SNOW
            80, 81, 82 -> RAIN_SHOWERS
            85, 86 -> SNOW_SHOWERS
            95, 96, 99 -> THUNDERSTORM
            else -> UNKNOWN
        }
    }
}
