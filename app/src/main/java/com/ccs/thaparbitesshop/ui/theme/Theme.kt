package com.ccs.thaparbitesshop.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary          = ShopOrange,
    onPrimary        = ShopSurface,
    primaryContainer = ShopOrangeLight,
    onPrimaryContainer = ShopTextPrimary,

    secondary        = ShopGold,
    onSecondary      = ShopTextPrimary,
    secondaryContainer = ShopGoldLight,
    onSecondaryContainer = ShopTextPrimary,

    background       = ShopBackground,
    onBackground     = ShopTextPrimary,

    surface          = ShopSurface,
    onSurface        = ShopTextPrimary,
    surfaceVariant   = ShopSurfaceVariant,
    onSurfaceVariant = ShopTextSecondary,

    outline          = ShopBorder,
    outlineVariant   = ShopDivider,

    error            = StatusClosed,
    onError          = ShopSurface,
)

private val DarkColorScheme = darkColorScheme(
    primary          = ShopOrangeLight,
    onPrimary        = ShopDarkBackground,
    primaryContainer = ShopOrangeDark,
    onPrimaryContainer = ShopDarkTextPrimary,

    secondary        = ShopGold,
    onSecondary      = ShopDarkBackground,
    secondaryContainer = Color(0xFF5C3A00),
    onSecondaryContainer = ShopGoldLight,

    background       = ShopDarkBackground,
    onBackground     = ShopDarkTextPrimary,

    surface          = ShopDarkSurface,
    onSurface        = ShopDarkTextPrimary,
    surfaceVariant   = ShopDarkSurfaceVariant,
    onSurfaceVariant = ShopDarkTextSecondary,

    outline          = Color(0xFF5A3E30),
    outlineVariant   = Color(0xFF3A2416),

    error            = Color(0xFFFF6B6B),
    onError          = ShopDarkBackground,
)

// Workaround for Color import in dark scheme
private val Color = androidx.compose.ui.graphics.Color

@Composable
fun ThaparBitesShopTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,   // keep brand colors consistent
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else      -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography  = ShopTypography,
        content     = content
    )
}