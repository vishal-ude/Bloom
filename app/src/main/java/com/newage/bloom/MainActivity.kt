package com.newage.bloom

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.newage.bloom.ui.components.BloomFlowerLogo
import com.newage.bloom.ui.home.HomeScreen
import com.newage.bloom.ui.home.HomeViewModel
import com.newage.bloom.ui.onboarding.OnboardingScreen
import com.newage.bloom.ui.onboarding.OnboardingViewModel
import com.newage.bloom.ui.theme.BloomTheme
import com.newage.bloom.ui.theme.BloomThemeTokens
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val bloomApp = application as BloomApplication
        val preferences = bloomApp.onboardingPreferences

        setContent {
            val currentThemeMode by preferences.themeModeFlow.collectAsState(initial = "SYSTEM")

            BloomTheme(themeMode = currentThemeMode) {
                val isOnboardingCompletedState = preferences.isOnboardingCompletedFlow.collectAsState(initial = null)
                val isCompleted = isOnboardingCompletedState.value

                var isSplashShowing by remember { mutableStateOf(true) }

                LaunchedEffect(Unit) {
                    // Graceful splash delay matching Figma splash screen
                    delay(800)
                    isSplashShowing = false
                }

                // Show App Open Ad for returning users on launch
                LaunchedEffect(isCompleted, isSplashShowing) {
                    if (!isSplashShowing && isCompleted == true) {
                        bloomApp.appOpenAdManager.showAdIfAvailable(this@MainActivity)
                    }
                }

                Crossfade(
                    targetState = Pair(isSplashShowing, isCompleted),
                    label = "MainNavigationCrossfade"
                ) { (splash, completed) ->
                    when {
                        splash || completed == null -> {
                            SplashScreen()
                        }
                        !completed -> {
                            val onboardingViewModel = remember(completed) { OnboardingViewModel(preferences) }
                            OnboardingScreen(
                                viewModel = onboardingViewModel,
                                onFinishOnboarding = {
                                    // Onboarding completion is saved to DataStore
                                }
                            )
                        }
                        else -> {
                            val homeViewModel = remember(completed) { HomeViewModel(preferences) }
                            HomeScreen(
                                viewModel = homeViewModel,
                                onNavigateToOnboarding = {
                                    // Flow reset from Settings
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SplashScreen() {
    val colors = BloomThemeTokens.colors
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background),
        contentAlignment = Alignment.Center
    ) {
        BloomFlowerLogo(
            size = 90.dp,
            petalColor = colors.primary,
            centerColor = colors.tertiary
        )
    }
}