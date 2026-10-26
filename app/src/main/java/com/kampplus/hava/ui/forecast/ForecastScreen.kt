package com.kampplus.hava.ui.forecast

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kampplus.hava.R
import com.kampplus.hava.model.CurrentWeather
import com.kampplus.hava.model.DailyForecast
import com.kampplus.hava.model.Forecast
import com.kampplus.hava.model.HourlyForecast
import com.kampplus.hava.model.WeatherCondition
import com.kampplus.hava.ui.AppLanguage
import com.kampplus.hava.ui.AppStrings
import com.kampplus.hava.ui.LoadState
import com.kampplus.hava.ui.SettingsManager
import com.kampplus.hava.ui.Strings
import com.kampplus.hava.ui.TempUnit
import com.kampplus.hava.ui.TrStrings
import com.kampplus.hava.ui.component.ErrorView
import com.kampplus.hava.ui.theme.HavaTheme
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale
import kotlin.math.roundToInt

/**
 * Tahmin ekranının bağlayıcı katmanı; ViewModel'i edinir ve state'i aktarır.
 */
@Composable
fun ForecastRoute(onBack: () -> Unit, modifier: Modifier = Modifier, viewModel: ForecastViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ForecastScreen(
        cityName = viewModel.cityName,
        subtitle = viewModel.subtitle,
        uiState = uiState,
        onBack = onBack,
        onRetry = viewModel::onRetry,
        modifier = modifier
    )
}

/**
 * Bir şehrin hava tahmini.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForecastScreen(
    cityName: String,
    subtitle: String,
    uiState: LoadState<Forecast>,
    onBack: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    val settings by SettingsManager.settings.collectAsStateWithLifecycle()
    val strings = AppStrings.get(settings.language)

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(cityName.ifEmpty { strings.appName }) },
                // Üst barın arka planını şeffaf yaparak gradyanın yukarı taşmasını sağlar
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                ),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = if (settings.language == AppLanguage.ENGLISH) "Back" else "Geri"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            when (uiState) {
                LoadState.Loading -> CircularProgressIndicator()
                LoadState.Empty -> ErrorView(message = strings.emptyCityError)
                is LoadState.Error -> ErrorView(message = uiState.message, onRetry = onRetry)
                is LoadState.Success -> ForecastContent(
                    subtitle = subtitle,
                    forecast = uiState.data,
                    strings = strings,
                    language = settings.language,
                    tempUnit = settings.tempUnit
                )
            }
        }
    }
}

@Composable
private fun ForecastContent(
    subtitle: String,
    forecast: Forecast,
    strings: Strings,
    language: AppLanguage,
    tempUnit: TempUnit,
    modifier: Modifier = Modifier
) {
    // 1. Sıcaklık ve hava durumuna göre kıyafet tavsiyesi (seçili dile göre)
    val advice = remember(forecast.current.temperatureC, forecast.current.weatherCode, language) {
        getClothingAdvice(forecast.current.temperatureC, forecast.current.weatherCode, language)
    }

    // 2. Hava durumu koduna göre dinamik arka plan gradyanı
    val weatherGradient = remember(forecast.current.weatherCode) {
        getWeatherGradient(forecast.current.weatherCode)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(weatherGradient) // Arka plan gradyanı
            .verticalScroll(rememberScrollState())
            .padding(vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CurrentWeatherHeader(
            subtitle = subtitle,
            current = forecast.current,
            tempUnit = tempUnit,
            strings = strings,
            language = language,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        // Buzlu cam efektli kıyafet tavsiyesi kartı
        ClothingAdviceCard(
            advice = advice,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        SectionTitle(strings.hourlyTitle)
        HourlyRow(hours = forecast.hourly, tempUnit = tempUnit)

        SectionTitle(strings.dailyTitle)
        Card(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = Color.White.copy(alpha = 0.45f),
                    shape = RoundedCornerShape(24.dp)
                ),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White.copy(alpha = 0.22f)
            )
        ) {
            forecast.daily.forEachIndexed { index, day ->
                if (index > 0) {
                    HorizontalDivider(color = Color.White.copy(alpha = 0.25f))
                }
                DailyRow(
                    day = day,
                    isToday = index == 0,
                    tempUnit = tempUnit,
                    strings = strings,
                    language = language
                )
            }
        }
    }
}

/** Ortalanmış Hero Sıcaklık Alanı ve 3'lü Cam Metrik Kapsülleri */
@Composable
private fun CurrentWeatherHeader(
    subtitle: String,
    current: CurrentWeather,
    tempUnit: TempUnit,
    strings: Strings,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    val condition = WeatherCondition.fromWeatherCode(current.weatherCode)
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = condition.emoji, fontSize = 54.sp)
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = degrees(current.temperatureC, tempUnit),
                fontSize = 68.sp,
                fontWeight = FontWeight.Light,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Text(
            text = condition.getLabel(language),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 3'lü Cam Kapsül Yapısı
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            current.apparentTemperatureC?.let {
                MetricCapsule(
                    label = strings.feelsLike,
                    value = degrees(it, tempUnit),
                    icon = "🌡️",
                    modifier = Modifier.weight(1f)
                )
            }
            current.humidityPercent?.let {
                MetricCapsule(
                    label = strings.humidity,
                    value = "%$it",
                    icon = "💧",
                    modifier = Modifier.weight(1f)
                )
            }
            current.windSpeedKmh?.let {
                val speedUnit = if (strings == TrStrings) "km/sa" else "km/h"
                MetricCapsule(
                    label = strings.wind,
                    value = "${it.roundToInt()} $speedUnit",
                    icon = "💨",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

/** Tekil Cam Metrik Kapsülü */
@Composable
private fun MetricCapsule(label: String, value: String, icon: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.border(
            width = 1.dp,
            color = Color.White.copy(alpha = 0.45f),
            shape = RoundedCornerShape(16.dp)
        ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.22f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = icon, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/** Cam Efektli Saatlik Tahmin Kartları */
@Composable
private fun HourlyRow(hours: List<HourlyForecast>, tempUnit: TempUnit, modifier: Modifier = Modifier) {
    val now = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS)
    val upcoming = hours.filter { !it.time.isBefore(now) }.take(HOURLY_COUNT)
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items = upcoming, key = { it.time.toString() }) { hour ->
            Card(
                modifier = Modifier
                    .width(76.dp)
                    .border(
                        width = 1.dp,
                        color = Color.White.copy(alpha = 0.45f),
                        shape = RoundedCornerShape(18.dp)
                    ),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.22f)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = hour.time.format(HOUR_FORMAT),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = WeatherCondition.fromWeatherCode(hour.weatherCode).emoji,
                        fontSize = 26.sp
                    )
                    Text(
                        text = degrees(hour.temperatureC, tempUnit),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = percent(hour.precipitationProbability) ?: " ",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

/** Cam Efektli Günlük Tahmin Satırı */
@Composable
private fun DailyRow(
    day: DailyForecast,
    isToday: Boolean,
    tempUnit: TempUnit,
    strings: Strings,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    val dayFormatter = DateTimeFormatter.ofPattern("EEEE", Locale.forLanguageTag(language.code))

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = if (isToday) strings.today else day.date.format(dayFormatter).replaceFirstChar(Char::titlecase),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = percent(day.precipitationProbability).orEmpty(),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.width(44.dp),
            textAlign = TextAlign.End
        )
        Text(
            text = WeatherCondition.fromWeatherCode(day.weatherCode).emoji,
            fontSize = 22.sp,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        Text(
            text = degrees(day.minTemperatureC, tempUnit),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.width(44.dp),
            textAlign = TextAlign.End
        )
        Text(
            text = degrees(day.maxTemperatureC, tempUnit),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.width(44.dp),
            textAlign = TextAlign.End
        )
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(horizontal = 16.dp)
    )
}

/** Seçilen birime (°C veya °F) göre derecenin formatlanması */
private fun degrees(celsius: Double, unit: TempUnit = TempUnit.CELSIUS): String {
    return SettingsManager.formatTemperature(celsius, unit)
}

private fun percent(value: Int?) = value?.takeIf { it > 0 }?.let { "%$it" }

private const val HOURLY_COUNT = 24
private val HOUR_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

@Preview(showBackground = true)
@Composable
private fun ForecastScreenPreview() {
    HavaTheme {
        ForecastScreen(
            cityName = "Ankara",
            subtitle = "Ankara, Türkiye",
            uiState = LoadState.Success(
                Forecast(
                    current = CurrentWeather(
                        temperatureC = 21.0,
                        weatherCode = 0,
                        observedAt = LocalDateTime.of(2026, 1, 15, 14, 0),
                        apparentTemperatureC = 20.0,
                        humidityPercent = 45,
                        windSpeedKmh = 12.0
                    ),
                    hourly = List(8) {
                        HourlyForecast(
                            time = LocalDateTime.now().plusHours(it.toLong()),
                            temperatureC = 18.0 + it,
                            weatherCode = 2,
                            precipitationProbability = it * 5
                        )
                    },
                    daily = List(7) {
                        DailyForecast(
                            date = LocalDate.of(2026, 1, 15).plusDays(it.toLong()),
                            minTemperatureC = 9.0 + it,
                            maxTemperatureC = 19.0 + it,
                            weatherCode = 61,
                            precipitationProbability = 40
                        )
                    }
                )
            ),
            onBack = {},
            onRetry = {}
        )
    }
}
