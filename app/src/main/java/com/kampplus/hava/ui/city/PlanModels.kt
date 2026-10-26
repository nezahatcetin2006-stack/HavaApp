package com.kampplus.hava.ui.city

import com.kampplus.hava.model.City

// Kullanıcının seçeceği aktivite türleri
enum class ActivityType(val title: String, val icon: String) {
    SKI("Kayak Tatili", "⛷️"),
    SWIM("Yüzme / Deniz", "🏊"),
    PICNIC("Piknik & Park", "🧺"),
    HIKE("Doğa Yürüyüşü", "🥾")
}

// Önerilecek rota ve mekan modeli
data class PlanDestination(
    val city: City, // Repodaki orijinal City modeliyle eşleşir
    val spotName: String,
    val description: String,
    val activities: List<ActivityType>
)

// Öneri havuzu: Türkiye'nin farklı bölgelerinden popüler mekanlar
val POPULAR_PLAN_DESTINATIONS = listOf(
    PlanDestination(
        city = City(
            id = 750269,
            name = "Bursa",
            latitude = 40.1956,
            longitude = 29.0601,
            countryCode = "TR",
            country = "Türkiye",
            admin1 = "Bursa"
        ),
        spotName = "Uludağ Kayak Merkezi",
        description = "Geniş pistleri ve kaliteli kar örtüsüyle kış tatili için ideal.",
        activities = listOf(ActivityType.SKI)
    ),
    PlanDestination(
        city = City(
            id = 315368,
            name = "Erzurum",
            latitude = 39.9086,
            longitude = 41.2769,
            countryCode = "TR",
            country = "Türkiye",
            admin1 = "Erzurum"
        ),
        spotName = "Palandöken Dağı",
        description = "Türkiye'nin en uzun pistlerine sahip, toz karda kayak keyfi.",
        activities = listOf(ActivityType.SKI)
    ),
    PlanDestination(
        city = City(
            id = 311004,
            name = "Kayseri",
            latitude = 38.7322,
            longitude = 35.4853,
            countryCode = "TR",
            country = "Türkiye",
            admin1 = "Kayseri"
        ),
        spotName = "Erciyes Kayak Merkezi",
        description = "Gelişmiş teleferik hatları ve modern tesisleriyle kış sporu rotası.",
        activities = listOf(ActivityType.SKI)
    ),
    PlanDestination(
        city = City(
            id = 323776,
            name = "Antalya",
            latitude = 36.8841,
            longitude = 30.7056,
            countryCode = "TR",
            country = "Türkiye",
            admin1 = "Antalya"
        ),
        spotName = "Kaş & Kaputaş Plajı",
        description = "Turkuaz renkli deniz ve güneşli kıyılarda yüzme keyfi.",
        activities = listOf(ActivityType.SWIM)
    ),
    PlanDestination(
        city = City(
            id = 304183,
            name = "Muğla",
            latitude = 37.2181,
            longitude = 28.3665,
            countryCode = "TR",
            country = "Türkiye",
            admin1 = "Muğla"
        ),
        spotName = "Akyaka & Azmak Çayı",
        description = "Serin suları, kumu ve doğasıyla harika bir sahil rotası.",
        activities = listOf(ActivityType.SWIM)
    ),
    PlanDestination(
        city = City(
            id = 749748,
            name = "Bolu",
            latitude = 40.7358,
            longitude = 31.6061,
            countryCode = "TR",
            country = "Türkiye",
            admin1 = "Bolu"
        ),
        spotName = "Abant & Gölcük Tabiat Parkı",
        description = "Göl kenarında çam kokuları eşliğinde yürüyüş ve piknik alanı.",
        activities = listOf(ActivityType.PICNIC, ActivityType.HIKE)
    ),
    PlanDestination(
        city = City(
            id = 740430,
            name = "Rize",
            latitude = 41.0208,
            longitude = 40.5219,
            countryCode = "TR",
            country = "Türkiye",
            admin1 = "Rize"
        ),
        spotName = "Ayder & Pokut Yaylası",
        description = "Yeşilin her tonu, bulut denizi ve serin yayla havası.",
        activities = listOf(ActivityType.HIKE)
    ),
    PlanDestination(
        city = City(
            id = 315202,
            name = "Eskişehir",
            latitude = 39.7767,
            longitude = 30.5206,
            countryCode = "TR",
            country = "Türkiye",
            admin1 = "Eskişehir"
        ),
        spotName = "Sazova Bilim, Sanat ve Kültür Parkı",
        description = "Geniş çim alanlar, masal şatosu ve açık havada keyifli bir gün.",
        activities = listOf(ActivityType.PICNIC)
    )
)
