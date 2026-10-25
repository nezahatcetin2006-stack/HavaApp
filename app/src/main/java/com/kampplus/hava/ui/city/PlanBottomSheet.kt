package com.kampplus.hava.ui.city

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kampplus.hava.model.City
import com.kampplus.hava.ui.AppLanguage
import com.kampplus.hava.ui.SettingsManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanBottomSheet(
    onDismiss: () -> Unit,
    onNavigateToCity: (City) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Seçili dili dinliyoruz
    val settings by SettingsManager.settings.collectAsStateWithLifecycle()
    val isEn = settings.language == AppLanguage.ENGLISH

    // Adım takibi: 1 = Gün seçimi, 2 = Aktivite seçimi, 3 = Yola çıkış şehri, 4 = Sonuç kartı
    var currentStep by remember { mutableIntStateOf(1) }
    var selectedDay by remember { mutableStateOf(if (isEn) "Today" else "Bugün") }
    var selectedActivity by remember { mutableStateOf<ActivityType?>(null) }
    var departureCity by remember { mutableStateOf("") }
    var resultDestination by remember { mutableStateOf<PlanDestination?>(null) }

    val days = if (isEn) listOf("Today", "Tomorrow", "This Weekend") else listOf("Bugün", "Yarın", "Bu Hafta Sonu")

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (currentStep) {
                // 1. SORU: Ne zaman gideceksin?
                1 -> {
                    Text(
                        text = if (isEn) "🗓️ When is your day off?" else "🗓️ İzin günün ne zaman?",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    days.forEach { day ->
                        OutlinedButton(
                            onClick = {
                                selectedDay = day
                                currentStep = 2 // 2. adıma geç
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(text = day, fontSize = 16.sp)
                        }
                    }
                }

                // 2. SORU: Ne yapmak istersin?
                2 -> {
                    Text(
                        text = if (isEn) "🎯 What would you like to do?" else "🎯 Ne yapmak istersin?",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    ActivityType.entries.forEach { activity ->
                        val activityTitle = when (activity) {
                            ActivityType.SKI -> if (isEn) "Ski Vacation" else activity.title
                            ActivityType.SWIM -> if (isEn) "Swimming / Beach" else activity.title
                            ActivityType.PICNIC -> if (isEn) "Picnic & Park" else activity.title
                            ActivityType.HIKE -> if (isEn) "Nature Hiking" else activity.title
                        }

                        Button(
                            onClick = {
                                selectedActivity = activity
                                currentStep = 3 // 3. adıma geç
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(text = "${activity.icon}  $activityTitle", fontSize = 16.sp)
                        }
                    }
                }

                // 3. SORU: Nereden yola çıkacaksın?
                3 -> {
                    Text(
                        text = if (isEn) "📍 Where are you departing from?" else "📍 Nereden yola çıkacaksın?",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = departureCity,
                        onValueChange = { departureCity = it },
                        placeholder = { Text(if (isEn) "e.g., Eskişehir, Ankara, Hatay..." else "Örn: Eskişehir, Ankara, Hatay...") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        enabled = departureCity.trim().isNotBlank(),
                        onClick = {
                            // Aktiviteye uyan rotalardan birini bul
                            resultDestination = POPULAR_PLAN_DESTINATIONS.firstOrNull { dest ->
                                dest.activities.contains(selectedActivity)
                            } ?: POPULAR_PLAN_DESTINATIONS.first()
                            currentStep = 4 // Sonuç adımına geç
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(if (isEn) "Find Suitable Route" else "Bana Uygun Rotayı Bul")
                    }
                }

                // 4. ADIM: Sonuç ve Rota Kartı
                4 -> {
                    resultDestination?.let { destination ->
                        Text(
                            text = if (isEn) "✨ Your Recommended Trip!" else "✨ İşte Sana Özel Rota Önerisi!",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Place,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "${destination.city.name} · ${destination.spotName}",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = destination.description,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = if (isEn) "Scheduled: $selectedDay" else "Planlanan Zaman: $selectedDay",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Tıklanınca o şehrin hava durumu detay ekranına yönlendirir
                        Button(
                            onClick = {
                                onDismiss()
                                onNavigateToCity(destination.city)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                if (isEn)
                                    "Check ${destination.city.name} Weather 🌤️"
                                else
                                    "${destination.city.name} Hava Durumuna Bak 🌤️"
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
