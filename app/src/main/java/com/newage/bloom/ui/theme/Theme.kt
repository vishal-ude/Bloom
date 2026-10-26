package com.newage.bloom.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

data class BloomColors(
    val background: Color,
    val surface: Color,
    val surfaceVariant: Color,
    val border: Color,
    val primary: Color,
    val primaryContainer: Color,
    val primaryDisabled: Color,
    val secondary: Color,
    val secondaryContainer: Color,
    val tertiary: Color,
    val tertiaryContainer: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textMuted: Color,
    val progressTrack: Color,
    val tipBackground: Color,
    val isDark: Boolean
)

val LightBloomColors = BloomColors(
    background = BloomBackgroundLight,
    surface = BloomSurfaceLight,
    surfaceVariant = BloomSurfaceVariantLight,
    border = BloomBorderLight,
    primary = BloomRoseLight,
    primaryContainer = BloomRoseContainerLight,
    primaryDisabled = BloomRoseDisabledLight,
    secondary = BloomSageGreenLight,
    secondaryContainer = BloomSageGreenContainerLight,
    tertiary = BloomHoneyGoldLight,
    tertiaryContainer = BloomHoneyGoldContainerLight,
    textPrimary = BloomTextPrimaryLight,
    textSecondary = BloomTextSecondaryLight,
    textMuted = BloomTextMutedLight,
    progressTrack = BloomProgressTrackLight,
    tipBackground = BloomCardTipBackgroundLight,
    isDark = false
)

val DarkBloomColors = BloomColors(
    background = BloomBackgroundDark,
    surface = BloomSurfaceDark,
    surfaceVariant = BloomSurfaceVariantDark,
    border = BloomBorderDark,
    primary = BloomRoseDark,
    primaryContainer = BloomRoseContainerDark,
    primaryDisabled = BloomRoseDisabledDark,
    secondary = BloomSageGreenDark,
    secondaryContainer = BloomSageGreenContainerDark,
    tertiary = BloomHoneyGoldDark,
    tertiaryContainer = BloomHoneyGoldContainerDark,
    textPrimary = BloomTextPrimaryDark,
    textSecondary = BloomTextSecondaryDark,
    textMuted = BloomTextMutedDark,
    progressTrack = BloomProgressTrackDark,
    tipBackground = BloomCardTipBackgroundDark,
    isDark = true
)

val LocalBloomColors = staticCompositionLocalOf { LightBloomColors }

object BloomThemeTokens {
    val colors: BloomColors
        @Composable
        @ReadOnlyComposable
        get() = LocalBloomColors.current
}

private val LightColorScheme = lightColorScheme(
    primary = BloomRoseLight,
    onPrimary = Color.White,
    primaryContainer = BloomRoseContainerLight,
    onPrimaryContainer = BloomRoseDarkLight,
    secondary = BloomSageGreenLight,
    onSecondary = Color.White,
    secondaryContainer = BloomSageGreenContainerLight,
    onSecondaryContainer = BloomTextPrimaryLight,
    tertiary = BloomHoneyGoldLight,
    onTertiary = Color.White,
    tertiaryContainer = BloomHoneyGoldContainerLight,
    onTertiaryContainer = BloomTextPrimaryLight,
    background = BloomBackgroundLight,
    onBackground = BloomTextPrimaryLight,
    surface = BloomSurfaceLight,
    onSurface = BloomTextPrimaryLight,
    surfaceVariant = BloomSurfaceVariantLight,
    onSurfaceVariant = BloomTextSecondaryLight,
    outline = BloomBorderLight
)

private val DarkColorScheme = darkColorScheme(
    primary = BloomRoseDark,
    onPrimary = BloomTextPrimaryLight,
    primaryContainer = BloomRoseContainerDark,
    onPrimaryContainer = BloomTextPrimaryDark,
    secondary = BloomSageGreenDark,
    onSecondary = BloomTextPrimaryLight,
    secondaryContainer = BloomSageGreenContainerDark,
    onSecondaryContainer = BloomTextPrimaryDark,
    tertiary = BloomHoneyGoldDark,
    onTertiary = BloomTextPrimaryLight,
    tertiaryContainer = BloomHoneyGoldContainerDark,
    onTertiaryContainer = BloomTextPrimaryDark,
    background = BloomBackgroundDark,
    onBackground = BloomTextPrimaryDark,
    surface = BloomSurfaceDark,
    onSurface = BloomTextPrimaryDark,
    surfaceVariant = BloomSurfaceVariantDark,
    onSurfaceVariant = BloomTextSecondaryDark,
    outline = BloomBorderDark
)

@Composable
fun BloomTheme(
    themeMode: String = "SYSTEM", // "SYSTEM", "LIGHT", "DARK"
    content: @Composable () -> Unit
) {
    val isSystemDark = isSystemInDarkTheme()
    val isDark = when (themeMode) {
        "LIGHT" -> false
        "DARK" -> true
        else -> isSystemDark
    }

    val bloomColors = if (isDark) DarkBloomColors else LightBloomColors
    val materialColorScheme = if (isDark) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = bloomColors.background.toArgb()
                window.navigationBarColor = bloomColors.surface.toArgb()
                val controller = WindowCompat.getInsetsController(window, view)
                controller.isAppearanceLightStatusBars = !isDark
                controller.isAppearanceLightNavigationBars = !isDark
            }
        }
    }

    CompositionLocalProvider(LocalBloomColors provides bloomColors) {
        MaterialTheme(
            colorScheme = materialColorScheme,
            typography = Typography,
            content = content
        )
    }
}