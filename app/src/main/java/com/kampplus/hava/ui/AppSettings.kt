package com.kampplus.hava.ui

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class ThemeMode(val titleTr: String, val titleEn: String) {
    SYSTEM("Sistem Varsayılanı", "System Default"),
    LIGHT("Açık Mod", "Light Mode"),
    DARK("Karanlık Mod", "Dark Mode")
}

enum class TempUnit(val symbol: String) {
    CELSIUS("°C"),
    FAHRENHEIT("°F")
}

enum class AppLanguage(val code: String, val displayName: String) {
    TURKISH("tr", "Türkçe"),
    ENGLISH("en", "English")
}

data class SettingsState(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val tempUnit: TempUnit = TempUnit.CELSIUS,
    val language: AppLanguage = AppLanguage.TURKISH
)

object SettingsManager {
    private val _settings = MutableStateFlow(SettingsState())
    val settings: StateFlow<SettingsState> = _settings.asStateFlow()

    fun updateTheme(themeMode: ThemeMode) {
        _settings.update { it.copy(themeMode = themeMode) }
    }

    fun updateTempUnit(unit: TempUnit) {
        _settings.update { it.copy(tempUnit = unit) }
    }

    fun updateLanguage(language: AppLanguage) {
        _settings.update { it.copy(language = language) }
    }

    // Sıcaklık dönüşüm yardımcısı
    fun formatTemperature(celsius: Double, unit: TempUnit): String {
        return if (unit == TempUnit.CELSIUS) {
            "${Math.round(celsius)}°C"
        } else {
            val fahrenheit = (celsius * 1.8) + 32
            "${Math.round(fahrenheit)}°F"
        }
    }
}
