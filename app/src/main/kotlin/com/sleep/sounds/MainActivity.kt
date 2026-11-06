package com.sleep.sounds

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.google.android.gms.ads.MobileAds
import com.sleep.sounds.ui.sleep.SleepScreen
import com.sleep.sounds.util.InterstitialAdManager

class MainActivity : ComponentActivity() {

    private lateinit var interstitialAdManager: InterstitialAdManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Google AdMob SDK'sını başlat
        MobileAds.initialize(this) {}
        interstitialAdManager = InterstitialAdManager(this)

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SleepScreen(
                        onTriggerAd = {
                            interstitialAdManager.showIfReady(this@MainActivity)
                        }
                    )
                }
            }
        }
    }
}
