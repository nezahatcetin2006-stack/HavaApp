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
}
