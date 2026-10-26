package com.newage.bloom

import android.app.Application
import com.google.android.gms.ads.MobileAds
import com.newage.bloom.ads.AppOpenAdManager
import com.newage.bloom.data.OnboardingPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class BloomApplication : Application() {

    lateinit var onboardingPreferences: OnboardingPreferences
        private set

    lateinit var appOpenAdManager: AppOpenAdManager
        private set

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    @Volatile
    var isOnboardingCompletedCached: Boolean = false
        private set

    override fun onCreate() {
        super.onCreate()

        onboardingPreferences = OnboardingPreferences(this)

        // Observe DataStore to keep cached boolean state updated synchronously for Ad Open checks
        applicationScope.launch(Dispatchers.IO) {
            onboardingPreferences.isOnboardingCompletedFlow.collect { completed ->
                isOnboardingCompletedCached = completed
            }
        }

        // Initialize Google Mobile Ads SDK on background thread
        CoroutineScope(Dispatchers.IO).launch {
            MobileAds.initialize(this@BloomApplication) { status ->
                // Mobile Ads initialized
            }
        }

        // Setup AppOpenAdManager with strict first-time user gatekeeper
        appOpenAdManager = AppOpenAdManager(
            application = this,
            isOnboardingCompleted = { isOnboardingCompletedCached }
        )

        // Preload App Open Ad
        appOpenAdManager.loadAd(this)
    }
}
