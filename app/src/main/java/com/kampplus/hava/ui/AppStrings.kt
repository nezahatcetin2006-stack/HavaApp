package com.kampplus.hava.ui

object AppStrings {
    fun get(lang: AppLanguage): Strings = when (lang) {
        AppLanguage.TURKISH -> TrStrings
        AppLanguage.ENGLISH -> EnStrings
    }
}

interface Strings {
    val appName: String
    val searchHint: String
    val welcomeTitle: String
    val welcomeSubtitle: String
    val planButton: String

    // Ayarlar
    val settingsTitle: String
    val appearanceTitle: String
    val tempUnitTitle: String
    val languageTitle: String
    val celsius: String
    val fahrenheit: String

    // Detay Ekranı
    val hourlyTitle: String
    val dailyTitle: String
    val feelsLike: String
    val humidity: String
    val wind: String
    val today: String
    val emptyCityError: String

    // Hava Kalitesi (AQI)
    val aqiSectionTitle: String
    val aqiSearchPlaceholder: String
    val aqiSheetTitle: String
    val aqiIndexLabel: String
    val aqiNoData: String
    val aqiAdviceTitle: String
    val aqiPollutantsTitle: String
    val aqiPm25Label: String
    val aqiPm10Label: String
    val aqiO3Label: String
    val aqiNo2Label: String
    val aqiStatusGood: String
    val aqiDescGood: String
    val aqiStatusModerate: String
    val aqiDescModerate: String
    val aqiStatusSensitive: String
    val aqiDescSensitive: String
    val aqiStatusUnhealthy: String
    val aqiDescUnhealthy: String
    val aqiStatusVeryUnhealthy: String
    val aqiDescVeryUnhealthy: String
    val aqiStatusHazardous: String
    val aqiDescHazardous: String
}

object TrStrings : Strings {
    override val appName = "Hava Durumu"
    override val searchHint = "Şehir bul"
    override val welcomeTitle = "🌧️Beni köyümün yağmurlarında yıkasınlar mı?"
    override val welcomeSubtitle = "Oralarda hava montluk mu tişörtlük mü? Bir şehir yaz da bakalım."
    override val planButton = "✨ Bir Planım Var"

    override val settingsTitle = "⚙️ Ayarlar"
    override val appearanceTitle = "Görünüm (Tema)"
    override val tempUnitTitle = "Sıcaklık Birimi"
    override val languageTitle = "Dil / Language"
    override val celsius = "Santigrat (°C)"
    override val fahrenheit = "Fahrenheit (°F)"

    override val hourlyTitle = "Saatlik"
    override val dailyTitle = "7 Günlük"
    override val feelsLike = "Hissedilen"
    override val humidity = "Nem"
    override val wind = "Rüzgâr"
    override val today = "Bugün"
    override val emptyCityError = "Bu şehir için tahmin bulunamadı."

    // Hava Kalitesi (AQI)
    override val aqiSectionTitle = "Hava Kalitesi Ölçümü"
    override val aqiSearchPlaceholder = "Hava raporu için şehir ara..."
    override val aqiSheetTitle = "Hava Kalitesi ve Kirletici Raporu"
    override val aqiIndexLabel = "Hava Kalitesi İndeksi (AQI)"
    override val aqiNoData = "Bu şehir için hava kalitesi istasyon verisi bulunamadı."
    override val aqiAdviceTitle = "Açıklama ve Tavsiye"
    override val aqiPollutantsTitle = "Ayrıntılı Kirletici Seviyeleri"
    override val aqiPm25Label = "PM2.5 (İnce Toz)"
    override val aqiPm10Label = "PM10 (Kaba Toz)"
    override val aqiO3Label = "Ozon (O3)"
    override val aqiNo2Label = "Azot Dioksit (NO2)"
    override val aqiStatusGood = "İyi"
    override val aqiDescGood =
        "Hava kalitesi tatmin edici ve kirlilik sağlık açısından çok az risk taşır veya hiç risk taşımaz."
    override val aqiStatusModerate = "Orta"
    override val aqiDescModerate =
        "Hava kalitesi kabul edilebilir. Ancak hava kirliliğine karşı hassas bireyler hafif rahatsızlık yaşayabilir."
    override val aqiStatusSensitive = "Hassas Gruplar İçin Sağlıksız"
    override val aqiDescSensitive =
        "Akciğer rahatsızlığı, astım veya alerjisi olanlar risk altındadır. Genel halk genellikle etkilenmez."
    override val aqiStatusUnhealthy = "Sağlıksız"
    override val aqiDescUnhealthy =
        "Herkes sağlık etkileri hissetmeye başlayabilir. Hassas grupların açık havada uzun süre kalmaması önerilir."
    override val aqiStatusVeryUnhealthy = "Çok Sağlıksız"
    override val aqiDescVeryUnhealthy =
        "Ciddi sağlık uyarısı: Nüfusun genelinde hissedilir etkiler görülebilir."
    override val aqiStatusHazardous = "Tehlikeli"
    override val aqiDescHazardous =
        "Acil sağlık durumu uyarısı! Tüm bireylerin dış ortam aktivitelerinden kaçınması gerekir."
}

object EnStrings : Strings {
    override val appName = "Weather App"
    override val searchHint = "Find a city"
    override val welcomeTitle = "🌧️ Which town's rain is this?"
    override val welcomeSubtitle = "Is it sweater weather or t-shirt weather? Type a city to find out."
    override val planButton = "✨ I Have a Plan"

    override val settingsTitle = "⚙️ Settings"
    override val appearanceTitle = "Appearance (Theme)"
    override val tempUnitTitle = "Temperature Unit"
    override val languageTitle = "Language"
    override val celsius = "Celsius (°C)"
    override val fahrenheit = "Fahrenheit (°F)"

    override val hourlyTitle = "Hourly Forecast"
    override val dailyTitle = "7-Day Forecast"
    override val feelsLike = "Feels Like"
    override val humidity = "Humidity"
    override val wind = "Wind"
    override val today = "Today"
    override val emptyCityError = "No forecast found for this city."

    // Hava Kalitesi (AQI)
    override val aqiSectionTitle = "Air Quality Check"
    override val aqiSearchPlaceholder = "Search city for air report..."
    override val aqiSheetTitle = "Air Quality & Pollutant Report"
    override val aqiIndexLabel = "Air Quality Index (AQI)"
    override val aqiNoData = "No air quality monitoring station data found for this city."
    override val aqiAdviceTitle = "Advice & Overview"
    override val aqiPollutantsTitle = "Detailed Pollutant Levels"
    override val aqiPm25Label = "PM2.5 (Fine Dust)"
    override val aqiPm10Label = "PM10 (Coarse Dust)"
    override val aqiO3Label = "Ozone (O3)"
    override val aqiNo2Label = "Nitrogen Dioxide (NO2)"
    override val aqiStatusGood = "Good"
    override val aqiDescGood =
        "Air quality is satisfactory, and air pollution poses little or no risk."
    override val aqiStatusModerate = "Moderate"
    override val aqiDescModerate =
        "Air quality is acceptable. However, unusually sensitive individuals may experience minor symptoms."
    override val aqiStatusSensitive = "Unhealthy for Sensitive Groups"
    override val aqiDescSensitive =
        "Members of sensitive groups may experience health effects. The general public is less likely to be affected."
    override val aqiStatusUnhealthy = "Unhealthy"
    override val aqiDescUnhealthy =
        "Everyone may begin to experience health effects. Sensitive groups should avoid prolonged outdoor exposure."
    override val aqiStatusVeryUnhealthy = "Very Unhealthy"
    override val aqiDescVeryUnhealthy =
        "Health alert: The risk of health effects is increased for everyone."
    override val aqiStatusHazardous = "Hazardous"
    override val aqiDescHazardous =
        "Health warning of emergency conditions: Everyone should avoid all outdoor physical activity."
}
