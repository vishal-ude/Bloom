package com.newage.bloom.ads

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import java.util.Date

/**
 * AppOpenAdManager manages loading, caching and showing Google Mobile Ads App Open Ads.
 * Test Ad Unit ID: ca-app-pub-3940256099942544/9257395921
 *
 * Implements strict policy safeguards:
 * 1. Checks 4-hour cache expiration.
 * 2. Gatekeeper prevents interrupting first-time users during onboarding.
 * 3. Observes ProcessLifecycleOwner for foreground transitions.
 * 4. Graceful handling of ad failures and activity lifecycles.
 */
class AppOpenAdManager(
    private val application: Application,
    private val isOnboardingCompleted: () -> Boolean
) : Application.ActivityLifecycleCallbacks, DefaultLifecycleObserver {

    companion object {
        private const val TAG = "BloomAppOpenAd"
        const val TEST_AD_UNIT_ID = "ca-app-pub-3940256099942544/9257395921"
        private const val FOUR_HOURS_MILLIS = 4 * 60 * 60 * 1000L
    }

    private var appOpenAd: AppOpenAd? = null
    private var isLoadingAd = false
    var isShowingAd = false
        private set
    private var loadTime: Long = 0
    private var currentActivity: Activity? = null

    init {
        application.registerActivityLifecycleCallbacks(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    /**
     * Called when the app moves to the foreground.
     */
    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        // Show ad on app open / return to foreground, only if onboarding is already completed
        currentActivity?.let { activity ->
            showAdIfAvailable(activity)
        }
    }

    /**
     * Request an ad if not already available or loading.
     */
    fun loadAd(context: Context) {
        if (isLoadingAd || isAdAvailable()) {
            return
        }

        isLoadingAd = true
        val request = AdRequest.Builder().build()
        AppOpenAd.load(
            context,
            TEST_AD_UNIT_ID,
            request,
            object : AppOpenAd.AppOpenAdLoadCallback() {
                override fun onAdLoaded(ad: AppOpenAd) {
                    Log.d(TAG, "App Open Ad loaded successfully.")
                    appOpenAd = ad
                    isLoadingAd = false
                    loadTime = Date().time
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    Log.w(TAG, "App Open Ad failed to load: ${loadAdError.message} (code: ${loadAdError.code})")
                    isLoadingAd = false
                    appOpenAd = null
                }
            }
        )
    }

    /**
     * Check if ad was loaded less than 4 hours ago.
     */
    private fun wasLoadTimeLessThan4HoursAgo(): Boolean {
        val numMillis = Date().time - loadTime
        return numMillis < FOUR_HOURS_MILLIS
    }

    /**
     * Check if ad is ready to show.
     */
    fun isAdAvailable(): Boolean {
        return appOpenAd != null && wasLoadTimeLessThan4HoursAgo()
    }

    /**
     * Show the App Open Ad if available and user is eligible (completed onboarding).
     */
    fun showAdIfAvailable(
        activity: Activity,
        onShowAdCompleteListener: (() -> Unit)? = null
    ) {
        // Prevent presenting an ad if the target activity is finishing or destroyed
        if (activity.isFinishing || activity.isDestroyed) {
            Log.d(TAG, "Suppressing App Open Ad: Activity is finishing or destroyed.")
            onShowAdCompleteListener?.invoke()
            return
        }

        // Crucial requirement: Do NOT interrupt first-time users during onboarding!
        if (!isOnboardingCompleted()) {
            Log.d(TAG, "Suppressing App Open Ad: User is still completing onboarding.")
            onShowAdCompleteListener?.invoke()
            return
        }

        if (isShowingAd) {
            Log.d(TAG, "App Open Ad is already showing.")
            return
        }

        if (!isAdAvailable()) {
            Log.d(TAG, "App Open Ad is not ready yet. Preloading for next launch.")
            onShowAdCompleteListener?.invoke()
            loadAd(activity)
            return
        }

        appOpenAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                Log.d(TAG, "App Open Ad dismissed.")
                appOpenAd = null
                isShowingAd = false
                onShowAdCompleteListener?.invoke()
                loadAd(activity)
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                Log.w(TAG, "App Open Ad failed to show: ${adError.message}")
                appOpenAd = null
                isShowingAd = false
                onShowAdCompleteListener?.invoke()
                loadAd(activity)
            }

            override fun onAdShowedFullScreenContent() {
                Log.d(TAG, "App Open Ad shown full screen.")
                isShowingAd = true
            }

            override fun onAdImpression() {
                Log.d(TAG, "[Ad Analytics] App Open Ad impression recorded.")
            }

            override fun onAdClicked() {
                Log.d(TAG, "[Ad Analytics] App Open Ad user click recorded.")
            }
        }

        isShowingAd = true
        appOpenAd?.show(activity)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
    override fun onActivityStarted(activity: Activity) {
        if (!isShowingAd) {
            currentActivity = activity
        }
    }
    override fun onActivityResumed(activity: Activity) {
        if (!isShowingAd) {
            currentActivity = activity
        }
    }
    override fun onActivityPaused(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {
        if (currentActivity === activity) {
            currentActivity = null
        }
    }
}
