package com.dragonball.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// ─── Dragon Ball Color Palette ───────────────────────────────────────────────

// Primary - Dragon Ball Orange/Gold
val DragonOrange = Color(0xFFF5A623)
val DragonOrangeDark = Color(0xFFE08800)
val DragonOrangeLight = Color(0xFFFFC74D)

// Secondary - Saiyan Blue
val SaiyanBlue = Color(0xFF1565C0)
val SaiyanBlueDark = Color(0xFF003C8F)
val SaiyanBlueLight = Color(0xFF5E92F3)

// Accent - Ki Power Yellow
val KiYellow = Color(0xFFFFD600)
val KiYellowGlow = Color(0xFFFFF176)

// Background Dark
val DarkBackground = Color(0xFF0A0E1A)
val DarkSurface = Color(0xFF111827)
val DarkCard = Color(0xFF1C2537)
val DarkCardElevated = Color(0xFF243048)

// Text
val TextPrimary = Color(0xFFF5F5F5)
val TextSecondary = Color(0xFFB0BEC5)
val TextMuted = Color(0xFF607D8B)

// Status Colors
val AliveGreen = Color(0xFF4CAF50)
val DestroyedRed = Color(0xFFE53935)
val UnknownGray = Color(0xFF757575)

// Affiliation Colors
val ZFighterColor = Color(0xFF2196F3)
val VillainColor = Color(0xFFE53935)
val FriezaForceColor = Color(0xFF9C27B0)
val NeutralColor = Color(0xFF607D8B)

// Gradient Colors
val GradientStart = Color(0xFF0A0E1A)
val GradientMid = Color(0xFF1A1F35)
val GradientEnd = Color(0xFF0D1B2A)

// ─── Color Schemes ────────────────────────────────────────────────────────────

private val DarkColorScheme = darkColorScheme(
    primary = DragonOrange,
    onPrimary = Color.Black,
    primaryContainer = DragonOrangeDark,
    onPrimaryContainer = DragonOrangeLight,
    secondary = SaiyanBlue,
    onSecondary = Color.White,
    secondaryContainer = SaiyanBlueDark,
    onSecondaryContainer = SaiyanBlueLight,
    tertiary = KiYellow,
    onTertiary = Color.Black,
    background = DarkBackground,
    onBackground = TextPrimary,
    surface = DarkSurface,
    onSurface = TextPrimary,
    surfaceVariant = DarkCard,
    onSurfaceVariant = TextSecondary,
    error = DestroyedRed,
    outline = Color(0xFF37474F)
)

private val LightColorScheme = lightColorScheme(
    primary = DragonOrangeDark,
    onPrimary = Color.White,
    primaryContainer = DragonOrangeLight,
    onPrimaryContainer = Color.Black,
    secondary = SaiyanBlue,
    onSecondary = Color.White,
    background = Color(0xFFFAFAFA),
    onBackground = Color(0xFF1A1A1A),
    surface = Color.White,
    onSurface = Color(0xFF1A1A1A),
    surfaceVariant = Color(0xFFF5F5F5),
    onSurfaceVariant = Color(0xFF616161)
)

// ─── Theme ────────────────────────────────────────────────────────────────────

@Composable
fun DragonBallTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = DragonBallTypography,
        content = content
    )
}
