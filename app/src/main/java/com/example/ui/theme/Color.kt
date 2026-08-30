package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Core Luxury Minimal Islamic Palette
val MidnightNavyDark = Color(0xFF060D19)
val MidnightNavyCard = Color(0xFF0D1B2E)
val MidnightNavySurface = Color(0xFF14243B)
val MidnightNavyLight = Color(0xFF1E3452)

val EmeraldPrimary = Color(0xFF10B981)
val EmeraldDark = Color(0xFF064E3B)
val EmeraldLight = Color(0xFF6EE7B7)
val EmeraldGlow = Color(0xFF059669)

val SoftGold = Color(0xFFE5C378)
val SoftGoldBright = Color(0xFFF3D58C)
val SoftGoldDark = Color(0xFFB38F39)
val GoldGlow = Color(0xFFD4AF37)

val IvoryWhite = Color(0xFFFAF9F6)
val IvoryMuted = Color(0xFFE2E8F0)
val TextMuted = Color(0xFF94A3B8)
val TextDim = Color(0xFF64748B)

val GlassBorder = Color(0x33E5C378)
val GlassWhiteBorder = Color(0x22FFFFFF)
val GlassSurfaceDark = Color(0x990D1B2E)
val GlassSurfaceLight = Color(0xCC14243B)

// Time of Day Gradients
object AtmosphereGradients {
    // Fajr (Dawn)
    val FajrGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF0B132B),
            Color(0xFF1C2541),
            Color(0xFF2A3960),
            Color(0xFF3A4F7C)
        )
    )

    // Sunrise / Early Morning
    val MorningGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF0D1E36),
            Color(0xFF183B5E),
            Color(0xFF2E5E8A),
            Color(0xFF5582AA)
        )
    )

    // Dhuhr (Mid-Day Bright)
    val DhuhrGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF091A30),
            Color(0xFF113254),
            Color(0xFF1C4C7A),
            Color(0xFF2E6B9E)
        )
    )

    // Asr (Warm Afternoon Gold)
    val AsrGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF0D172A),
            Color(0xFF1F2B48),
            Color(0xFF3B3958),
            Color(0xFF5C475A)
        )
    )

    // Maghrib (Sunset / Twilight Gold-Purple)
    val MaghribGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF0B0F1F),
            Color(0xFF1C132E),
            Color(0xFF38183B),
            Color(0xFF5E273F)
        )
    )

    // Isha (Deep Night / Stars)
    val IshaGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF050811),
            Color(0xFF09101F),
            Color(0xFF0F1A2E),
            Color(0xFF15223A)
        )
    )
}
