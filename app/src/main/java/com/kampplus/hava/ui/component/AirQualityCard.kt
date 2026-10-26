package com.kampplus.hava.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kampplus.hava.model.AqiData
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface

@Composable
fun AirQualityCard(aqiData: AqiData?, modifier: Modifier = Modifier) {
    if (aqiData == null) return

    val aqi = aqiData.aqi
    val (statusText, statusDesc, statusColor) = when {
        aqi <= 50 -> Triple(
            "İyi",
            "Hava kalitesi tatmin edici ve hava kirliliği çok az risk taşıyor veya hiç risk taşımıyor.",
            Color(0xFF4CAF50)
        )
        aqi <= 100 -> Triple(
            "Orta",
            "Hava kalitesi kabul edilebilir; ancak hassas bireyler için hafif bir sağlık kaygısı oluşabilir.",
            Color(0xFFFFB300)
        )
        aqi <= 150 -> Triple(
            "Hassas Gruplar İçin Sağlıksız",
            "Hassas gruplar sağlık etkileri yaşayabilir. Genel halkın etkilenmesi olası değildir.",
            Color(0xFFFF9800)
        )
        aqi <= 200 -> Triple(
            "Sağlıksız",
            "Herkes sağlık etkileri yaşamaya başlayabilir; hassas gruplar daha ciddi etkiler yaşayabilir.",
            Color(0xFFF44336)
        )
        aqi <= 300 -> Triple(
            "Çok Sağlıksız",
            "Sağlık uyarısı: Herkes daha ciddi sağlık etkileri yaşayabilir.",
            Color(0xFF9C27B0)
        )
        else -> Triple(
            "Tehlikeli",
            "Sağlık alarmı: Herkesin etkilenmesi muhtemel acil durum koşulları.",
            Color(0xFF795548)
        )
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = aqiData.city?.name?.substringBefore(",") ?: "Hava Kalitesi",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "İstasyon Ölçümü",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = statusColor.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = statusText,
                        color = statusColor,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "$aqi",
                    fontSize = 46.sp,
                    fontWeight = FontWeight.Black,
                    color = statusColor
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "AQI",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = statusDesc,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Kirleticilerin detay ızgarası (PM2.5, PM10, O3, NO2)
            val iaqi = aqiData.iaqi
            if (iaqi != null) {
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Kirletici Partikül Değerleri",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    iaqi.pm25?.let { MetricItem("PM2.5", "${it.value.toInt()} µg/m³") }
                    iaqi.pm10?.let { MetricItem("PM10", "${it.value.toInt()} µg/m³") }
                    iaqi.o3?.let { MetricItem("Ozon (O3)", "${it.value.toInt()} ppb") }
                    iaqi.no2?.let { MetricItem("NO2", "${it.value.toInt()} ppb") }
                }
            }
        }
    }
}

@Composable
private fun MetricItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
