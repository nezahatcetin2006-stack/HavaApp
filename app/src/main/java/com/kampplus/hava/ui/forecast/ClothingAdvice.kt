package com.kampplus.hava.ui.forecast

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kampplus.hava.ui.AppLanguage

data class ClothingAdvice(
    val title: String,
    val description: String,
    val icon: String
)

/**
 * Sıcaklık, hava kodu ve seçilen dile (TR / EN) göre tavsiye üretir.
 */
fun getClothingAdvice(
    temperature: Double,
    weatherCode: Int,
    lang: AppLanguage = AppLanguage.TURKISH
): ClothingAdvice {
    val isRainy = weatherCode in listOf(51, 53, 55, 61, 63, 65, 80, 81, 82)
    val isSnowy = weatherCode in listOf(71, 73, 75, 77, 85, 86)
    val isThunder = weatherCode in listOf(95, 96, 99)
    val isEn = lang == AppLanguage.ENGLISH

    return when {
        isThunder -> ClothingAdvice(
            title = if (isEn) "Storm Warning!" else "Fırtına Uyarısı!",
            description = if (isEn) "Thunderstorms expected. Wear waterproof clothes and hold your umbrella tight!" else "Gök gürültülü sağanak var. Su geçirmez giyin ve şemsiyeni sağlam tut!",
            icon = "⛈️"
        )
        isSnowy -> ClothingAdvice(
            title = if (isEn) "It's Snowing!" else "Kar Yağıyor!",
            description = if (isEn) "Snowy weather. Grab your heavy coat, boots, beanie, and gloves." else "Hava karlı. Kalın kaban, bot, bere ve eldivenlerini hazırla.",
            icon = "❄️"
        )
        isRainy -> ClothingAdvice(
            title = if (isEn) "Rain Ahead!" else "Yağmur Var!",
            description = if (isEn) "Wear a raincoat and don't forget to take an umbrella with you." else "Islanmamak için yağmurluk giy ve şemsiyeni yanına almayı sakın unutma.",
            icon = "☔"
        )
        temperature < 5.0 -> ClothingAdvice(
            title = if (isEn) "It's Freezing!" else "Hava Çok Soğuk!",
            description = if (isEn) "Bone-chilling cold outside. Don't leave without a thick sweater, coat, and scarf." else "Dondurucu bir hava var. Kalın kazak, mont ve atkısız dışarı çıkma.",
            icon = "🧣"
        )
        temperature in 5.0..14.9 -> ClothingAdvice(
            title = if (isEn) "Chilly Weather" else "Serin Bir Hava",
            description = if (isEn) "Weather can be deceiving. A thick cardigan or seasonal jacket will do." else "Hava aldatıcı olabilir. Kalın bir hırka veya mevsimlik ceket iyi gider.",
            icon = "🧥"
        )
        temperature in 15.0..22.0 -> ClothingAdvice(
            title = if (isEn) "Pleasant Day" else "Hava Çok Tatlı",
            description = if (isEn) "Mild and comfortable. A long-sleeve t-shirt or casual shirt is ideal." else "Ilık bir gün. Uzun kollu tişört veya rahat bir gömlek ideal.",
            icon = "👔"
        )
        else -> ClothingAdvice(
            title = if (isEn) "Sunny & Hot!" else "Güneşli ve Sıcak!",
            description = if (isEn) "It's hot. Put on a light t-shirt, sunglasses, and wear sunscreen!" else "Hava sıcak. Rahat bir tişört giy, güneş kremini ve gözlüğünü unutma!",
            icon = "🕶️"
        )
    }
}

@Composable
fun ClothingAdviceCard(
    advice: ClothingAdvice,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.45f),
                shape = RoundedCornerShape(20.dp)
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.22f)
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = advice.icon,
                fontSize = 34.sp
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = advice.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = advice.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

fun getWeatherGradient(weatherCode: Int): Brush {
    val isRainy = weatherCode in listOf(51, 53, 55, 61, 63, 65, 80, 81, 82)
    val isSnowy = weatherCode in listOf(71, 73, 75, 77, 85, 86)
    val isThunder = weatherCode in listOf(95, 96, 99)

    val colors = when {
        isThunder -> listOf(
            Color(0xFF2C3E50),
            Color(0xFF1F1C2C)
        )
        isSnowy -> listOf(
            Color(0xFFB0C4DE),
            Color(0xFFE6ECF5)
        )
        isRainy -> listOf(
            Color(0xFF5D6D7E),
            Color(0xFF34495E),
            Color(0xFF212F3D)
        )
        weatherCode in listOf(0, 1) -> listOf(
            Color(0xFF4FA3E3),
            Color(0xFF85C1E9),
            Color(0xFFEBF5FB)
        )
        else -> listOf(
            Color(0xFF78909C),
            Color(0xFFB0BEC5),
            Color(0xFFECEFF1)
        )
    }

    return Brush.verticalGradient(colors = colors)
}
