package com.kampplus.hava

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kampplus.hava.ui.HavaNavHost
import com.kampplus.hava.ui.SettingsManager
import com.kampplus.hava.ui.ThemeMode
import com.kampplus.hava.ui.theme.HavaTheme

/**
 * Uygulamanın tek Activity'si; kullanıcının girdiği kapı.
 *
 * Activity bir **pencereyi** yönetir. Pencerenin içine ne çizileceğini ise
 * Compose söyler: sahne Activity'dir, dekoru Compose kurar.
 */
class MainActivity : ComponentActivity() {

    /**
     * Ekran oluşturulurken Android bu fonksiyonu **kendisi** çağırır; biz çağırmayız.
     * Yaşam döngüsünün ilk adımıdır.
     *
     * Logcat'i açıp uygulamayı arka plana alıp geri dönerek `onCreate` / `onStart` /
     * `onResume` sırasını gözlemleyebilirsin.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")

        // İçeriği durum ve navigasyon çubuklarının altına kadar yayar.
        enableEdgeToEdge()

        // Süslü parantezin içi Compose dünyasıdır.
        setContent {
            // Ayarlardaki güncel tema durumunu dinliyoruz
            val settings by SettingsManager.settings.collectAsStateWithLifecycle()

            // Seçilen tema moduna göre karanlık temanın aktif olup olmayacağını belirliyoruz
            val isDarkTheme = when (settings.themeMode) {
                ThemeMode.SYSTEM -> isSystemInDarkTheme()
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
            }

            // Temayı kullanıcının seçimine göre tetikliyoruz
            HavaTheme(darkTheme = isDarkTheme) {
                HavaNavHost()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
    }

    private companion object {
        /** Logcat'te filtrelemek için: arama kutusuna `Lifecycle` yaz. */
        const val TAG = "Lifecycle"
    }
}
