# Hava - Modern Hava Durumu ve Planlama Uygulaması

**Hava**, modern Android geliştirme standartları (Kotlin ve Jetpack Compose) kullanılarak geliştirilmiş; anlık hava durumu takibi, hava kalitesi indeksi (AQI) analizleri, dinamik giyim tavsiyeleri ve planlama araçları sunan bir mobil uygulamadır.

---

## 🌟 Öne Çıkan Özellikler

### 📍 Şehir Arama ve Hızlı Erişim
- **Gerçek Zamanlı Arama:** Minimum 2 karakter girişiyle devreye giren debounced arama altyapısı.
- **Hızlı Erişim Kartları:** Hatay, İstanbul ve Ankara gibi varsayılan şehirlerin yanı sıra kullanıcının dinamik olarak yeni şehirler ekleyebileceği yatay kaydırmalı erişim listesi.
- **Detaylı Bölge Bilgisi:** Arama sonuçlarında şehir adı, idari bölge (admin1) ve ülke bilgileriyle net lokasyon ayrımı.

### 🌤️ Hava Durumu Tahmini ve Dinamik Görsellik
- **Saatlik ve 7 Günlük Tahmin:** Gün içi sıcaklık değişimleri ve haftalık hava trendleri.
- **Detaylı Metrikler:** Hissedilen sıcaklık, nem oranı ve rüzgâr hızı verileri.
- **Dinamik Gradyan ve Temalandırma:** Fırtına, kar, yağmur veya açık hava durum kodlarına göre gerçek zamanlı değişen arka plan gradyanları (`getWeatherGradient`).

### 🍃 Hava Kalitesi (AQI) ve Kirletici Raporu
- **Hava Kalitesi İndeksi:** Şehirlere göre gerçek zamanlı AQI seviyesi sınıflandırması (*İyi, Orta, Hassas Gruplar İçin Sağlıksız, Sağlıksız, Çok Sağlıksız, Tehlikeli*).
- **Ayrıntılı Kirletici Dökümü:** PM2.5 (ince toz), PM10 (kaba toz), Ozon (O3) ve Azot Dioksit (NO2) değerlerinin takibi.
- **Sağlık Tavsiyeleri:** Mevcut hava kirliliği seviyesine göre açık hava aktiviteleri ve hassas gruplara özel uyarılar.

### 👔 Akıllı Giyim Tavsiyeleri (Clothing Advice)
- Hava kodu (yağmur, kar, fırtına) ve anlık sıcaklık derecesine göre kullanıcıya ne giymesi gerektiğini söyleyen dinamik tavsiye motoru (*"Dondurucu soğuk", "Serin hava", "Güneşli ve sıcak"* vb.).

### ⚙️ Kişiselleştirme ve Ayarlar
- **Çoklu Dil Desteği:** Türkçe ve İngilizce tam yerelleştirme (Localization).
- **Sıcaklık Birimi:** Santigrat (°C) ve Fahrenheit (°F) arasında tek dokunuşla geçiş.
- **Tema:** Sistem moduna tam uyumlu Koyu (Dark) ve Açık (Light) tema entegrasyonu.

---

## 🛠️ Mimari ve Kullanılan Teknolojiler

- **Dil:** Kotlin
- **Kullanıcı Arayüzü (UI):** Jetpack Compose, Material 3
- **Mimari Desen:** MVVM (Model-View-ViewModel) + Single Source of Truth
- **Reaktif Durum Yönetimi:** Kotlin Coroutines, StateFlow, `collectAsStateWithLifecycle`
- **Ağ İstemcisi:** Retrofit, OkHttp
- **Asenkron Arama:** Coroutines `delay(300)` / `delay(350)` ile optimize debounce mekanizması
- **Kod Kalitesi ve Biçimlendirme:** Spotless + Ktlint (Maksimum 140 karakter satır sınırı, açık import yönetimi)
- **Sürekli Entegrasyon (CI):** GitHub Actions (`spotlessCheck` ve otomatik derleme/test süreçleri)

---

## 📂 Proje Dizin Yapısı

```text
app/src/main/java/com/kampplus/hava/
├── data/
│   ├── ApiClient.kt             # Retrofit istemci konfigürasyonları
│   ├── AqiApiService.kt         # Hava kalitesi API servisleri
│   ├── CityApi.kt               # Şehir arama servisleri
│   └── ForecastApi.kt           # Tahmin verisi servisleri
├── model/
│   ├── AqiData.kt               # AQI ve kirletici veri modelleri
│   ├── City.kt                  # Şehir modelleri
│   └── ForecastResponse.kt      # Hava durumu yanıt modelleri
├── ui/
│   ├── city/
│   │   ├── CityScreen.kt        # Ana arama ve hızlı erişim ekranı
│   │   ├── CityViewModel.kt     # Şehir ve AQI durum yönetimi
│   │   └── PlanBottomSheet.kt   # Planlama alt sayfası
│   ├── component/
│   │   ├── AirQualityCard.kt
│   │   ├── AirQualityDetailSheet.kt # AQI detay modalı
│   │   ├── EmptyView.kt         # Boş durum ekranı
│   │   └── ErrorView.kt         # Hata durum ekranı
│   ├── forecast/
│   │   ├── ClothingAdvice.kt    # Giyim öneri motoru ve gradyanlar
│   │   └── ForecastScreen.kt    # Tahmin detay ekranı
│   ├── theme/                   # Material 3 renk, tipografi ve tema tanımları
│   ├── AppLanguage.kt           # Dil enum tanımları
│   ├── AppStrings.kt            # Yerelleştirilmiş metin kaynakları (Tr/En)
│   ├── LoadState.kt             # Yükleme/Başarı/Hata durum sarmalayıcısı
│   └── SettingsBottomSheet.kt   # Ayarlar alt sayfası
└── MainActivity.kt              # Uygulama giriş noktası ve NavHost
```

---

## 🚀 Kurulum ve Çalıştırma

Projeyi yerel makinenizde derlemek ve çalıştırmak için aşağıdaki adımları izleyebilirsiniz:

### Gereksinimler
- **Android Studio:** Ladybug (2024.2.1) veya daha güncel bir sürüm
- **JDK:** Android Studio Embedded JDK (Java 17 veya 21 önerilir)
- **Min SDK:** 26 (Android 8.0 Oreo)
- **Target / Compile SDK:** 34 veya 35

### Adım Adım Kurulum

1. **Depoyu Klonlayın:**
   ```bash
   git clone [https://github.com/kullanici-adiniz/havadurumuapp.git](https://github.com/kullanici-adiniz/havadurumuapp.git)
   cd havadurumuapp
   ```

2. **Android Studio ile Açın:**
   Android Studio'yu açıp `Open` seçeneğiyle indirdiğiniz proje klasörünü seçin. Gradle senkronizasyonunun (Sync) tamamlanmasını bekleyin.

3. **Kod Biçimlendirme Kontrolü (Spotless):**
   Projede kod standartlarını doğrulamak veya otomatik düzeltmek için terminalden veya Android Studio Gradle menüsünden şu komutu çalıştırabilirsiniz:
   ```bash
   ./gradlew spotlessApply
   ```
   *(Windows PowerShell için: `.\gradlew spotlessApply`)*

4. **Uygulamayı Çalıştırın:**
   Bir Android sanal cihazı (Emulator) veya USB hata ayıklaması açık bir fiziksel cihaz bağlayarak Android Studio üzerinden `Run 'app'` (yeşil oynat butonu) ile uygulamayı başlatın.

---

## 🧪 Kod Standartları ve Kalite Kontrolü

Proje genelinde kod kalitesi ve tutarlılığı **Spotless (Ktlint)** ile denetlenmektedir:
- **Wildcard Import Yasağı:** Yıldızlı (`*`) import kullanılmaz, tüm sınıflar tek tek açıkça import edilir.
- **Satır Sınırı:** Hiçbir kod satırı 140 karakteri aşamaz (`max-line-length`).
- **CI Doğrulaması:** GitHub deposuna gönderilen her `commit` ve açılan her `Pull Request` için GitHub Actions otomatik olarak `:spotlessCheck` ve birim testlerini çalıştırarak kalite kontrolü sağlar.

---

## 📄 Lisans

Bu proje kişisel gelişim ve eğitim amacıyla açık kaynak olarak geliştirilmiştir.
