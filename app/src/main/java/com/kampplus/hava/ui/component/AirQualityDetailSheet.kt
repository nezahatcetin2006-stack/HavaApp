package com.kampplus.hava.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kampplus.hava.model.AqiData
import com.kampplus.hava.ui.Strings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AirQualityDetailSheet(
    cityName: String,
    aqiData: AqiData?,
    isLoading: Boolean,
    strings: Strings,
    isEn: Boolean,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = cityName,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = strings.aqiSheetTitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = if (isEn) "Close" else "Kapat"
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (aqiData == null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = strings.aqiNoData,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                val aqi = aqiData.aqi
                val (statusText, statusDesc, statusColor) = when {
                    aqi <= 50 -> Triple(strings.aqiStatusGood, strings.aqiDescGood, Color(0xFF4CAF50))
                    aqi <= 100 -> Triple(strings.aqiStatusModerate, strings.aqiDescModerate, Color(0xFFFFB300))
                    aqi <= 150 -> Triple(strings.aqiStatusSensitive, strings.aqiDescSensitive, Color(0xFFFF9800))
                    aqi <= 200 -> Triple(strings.aqiStatusUnhealthy, strings.aqiDescUnhealthy, Color(0xFFF44336))
                    aqi <= 300 -> Triple(strings.aqiStatusVeryUnhealthy, strings.aqiDescVeryUnhealthy, Color(0xFF9C27B0))
                    else -> Triple(strings.aqiStatusHazardous, strings.aqiDescHazardous, Color(0xFF795548))
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = statusColor.copy(alpha = 0.12f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "$aqi",
                            fontSize = 60.sp,
                            fontWeight = FontWeight.Black,
                            color = statusColor
                        )
                        Text(
                            text = strings.aqiIndexLabel,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = statusColor
                        ) {
                            Text(
                                text = statusText,
                                color = Color.White,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = strings.aqiAdviceTitle,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = statusDesc,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                val iaqi = aqiData.iaqi
                if (iaqi != null) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = strings.aqiPollutantsTitle,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        iaqi.pm25?.let {
                            DetailMetricBox(
                                title = strings.aqiPm25Label,
                                value = "${it.value.toInt()}",
                                unit = "µg/m³",
                                modifier = Modifier.weight(1f)
                            )
                        }
                        iaqi.pm10?.let {
                            DetailMetricBox(
                                title = strings.aqiPm10Label,
                                value = "${it.value.toInt()}",
                                unit = "µg/m³",
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        iaqi.o3?.let {
                            DetailMetricBox(
                                title = strings.aqiO3Label,
                                value = "${it.value.toInt()}",
                                unit = "ppb",
                                modifier = Modifier.weight(1f)
                            )
                        }
                        iaqi.no2?.let {
                            DetailMetricBox(
                                title = strings.aqiNo2Label,
                                value = "${it.value.toInt()}",
                                unit = "ppb",
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailMetricBox(
    title: String,
    value: String,
    unit: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = unit,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
            }
        }
    }
}
