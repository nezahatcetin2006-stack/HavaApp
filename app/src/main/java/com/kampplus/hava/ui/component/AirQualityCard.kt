package com.kampplus.hava.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kampplus.hava.model.AqiData
import com.kampplus.hava.ui.Strings

@Composable
fun AirQualityCard(
    aqiData: AqiData?,
    strings: Strings,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = strings.aqiSectionTitle,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = "🍃",
                    fontSize = 20.sp,
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (aqiData == null) {
                Text(
                    text = strings.aqiNoData,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            } else {
                val aqi = aqiData.aqi
                val (statusText, statusColor) = getAqiStatus(aqi, strings)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .clip(CircleShape)
                            .background(statusColor),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "$aqi - $statusText",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    AqiMetricItem(label = strings.aqiPm25Label, value = aqiData.iaqi?.pm25?.value)
                    AqiMetricItem(label = strings.aqiPm10Label, value = aqiData.iaqi?.pm10?.value)
                    AqiMetricItem(label = strings.aqiO3Label, value = aqiData.iaqi?.o3?.value)
                    AqiMetricItem(label = strings.aqiNo2Label, value = aqiData.iaqi?.no2?.value)
                }
            }
        }
    }
}

@Composable
private fun AqiMetricItem(
    label: String,
    value: Double?,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        color = Color.Transparent,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value?.let { "%.1f".format(it) } ?: "-",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

private fun getAqiStatus(
    aqi: Int,
    strings: Strings,
): Pair<String, Color> {
    return when {
        aqi <= 50 -> strings.aqiStatusGood to Color(0xFF4CAF50)
        aqi <= 100 -> strings.aqiStatusModerate to Color(0xFFFFEB3B)
        aqi <= 150 -> strings.aqiStatusSensitive to Color(0xFFFF9800)
        aqi <= 200 -> strings.aqiStatusUnhealthy to Color(0xFFF44336)
        aqi <= 300 -> strings.aqiStatusVeryUnhealthy to Color(0xFF9C27B0)
        else -> strings.aqiStatusHazardous to Color(0xFF7E0023)
    }
}
