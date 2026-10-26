package com.newage.bloom.ads

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.newage.bloom.ui.theme.BloomThemeTokens

/**
 * Adaptive Banner Ad component with lifecycle handling and fallback support.
 * Official Google Test Banner Ad Unit ID: ca-app-pub-3940256099942544/9214589741
 */
@Composable
fun AdaptiveBannerAd(
    modifier: Modifier = Modifier,
    adUnitId: String = "ca-app-pub-3940256099942544/9214589741"
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val colors = BloomThemeTokens.colors

    var isAdLoaded by remember { mutableStateOf(false) }
    var adLoadError by remember { mutableStateOf<String?>(null) }

    val adSize = remember(screenWidth) {
        AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, screenWidth)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(colors.surface)
            .border(1.dp, colors.border, RoundedCornerShape(12.dp))
            .padding(vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        AndroidView(
            modifier = Modifier.fillMaxWidth(),
            factory = { ctx ->
                AdView(ctx).apply {
                    setAdSize(adSize)
                    this.adUnitId = adUnitId
                    adListener = object : AdListener() {
                        override fun onAdLoaded() {
                            super.onAdLoaded()
                            Log.d("BloomBannerAd", "Adaptive banner ad loaded successfully.")
                            isAdLoaded = true
                            adLoadError = null
                        }

                        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                            super.onAdFailedToLoad(loadAdError)
                            Log.w("BloomBannerAd", "Banner ad failed to load: ${loadAdError.message}")
                            isAdLoaded = false
                            adLoadError = loadAdError.message
                        }

                        override fun onAdImpression() {
                            super.onAdImpression()
                            Log.d("BloomBannerAd", "[Ad Analytics] Banner ad impression recorded.")
                        }

                        override fun onAdClicked() {
                            super.onAdClicked()
                            Log.d("BloomBannerAd", "[Ad Analytics] Banner ad user click recorded.")
                        }

                        override fun onAdOpened() {
                            super.onAdOpened()
                            Log.d("BloomBannerAd", "[Ad Analytics] Banner ad opened full screen overlay.")
                        }
                    }
                    val request = AdRequest.Builder().build()
                    loadAd(request)
                }
            },
            update = { adView ->
                // AdView is updated if needed
            },
            onRelease = { adView ->
                adView.destroy()
            }
        )

        // If ad failed to load (e.g. offline device / test emulator without network),
        // show non-intrusive sponsor placeholder indicator so layout remains stable and clean
        if (adLoadError != null && !isAdLoaded) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(adSize.height.dp.coerceAtLeast(50.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Bloom Partner Ad (Test Unit)",
                    fontSize = 11.sp,
                    color = colors.textMuted,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
