package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val NoorDarkColorScheme = darkColorScheme(
    primary = EmeraldPrimary,
    onPrimary = MidnightNavyDark,
    primaryContainer = EmeraldDark,
    onPrimaryContainer = EmeraldLight,
    secondary = SoftGold,
    onSecondary = MidnightNavyDark,
    secondaryContainer = SoftGoldDark,
    onSecondaryContainer = SoftGoldBright,
    tertiary = EmeraldLight,
    onTertiary = MidnightNavyDark,
    background = MidnightNavyDark,
    onBackground = IvoryWhite,
    surface = MidnightNavyCard,
    onSurface = IvoryWhite,
    surfaceVariant = MidnightNavySurface,
    onSurfaceVariant = IvoryMuted,
    outline = GlassBorder
)

private val NoorLightColorScheme = lightColorScheme(
    primary = EmeraldDark,
    onPrimary = IvoryWhite,
    primaryContainer = EmeraldLight,
    onPrimaryContainer = MidnightNavyDark,
    secondary = SoftGoldDark,
    onSecondary = IvoryWhite,
    secondaryContainer = SoftGold,
    onSecondaryContainer = MidnightNavyDark,
    tertiary = EmeraldPrimary,
    onTertiary = IvoryWhite,
    background = Color(0xFFF7F9FC),
    onBackground = MidnightNavyDark,
    surface = Color(0xFFFFFFFF),
    onSurface = MidnightNavyDark,
    surfaceVariant = Color(0xFFEEF2F6),
    onSurfaceVariant = Color(0xFF475569),
    outline = Color(0xFFCBD5E1)
)

@Composable
fun NoorTheme(
    darkTheme: Boolean = true, // Default to luxurious dark Islamic theme
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) NoorDarkColorScheme else NoorLightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = MidnightNavyDark.toArgb()
                window.navigationBarColor = MidnightNavyDark.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
                WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
