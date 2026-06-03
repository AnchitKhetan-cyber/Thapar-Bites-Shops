package com.ccs.thaparbitesshop.ui.theme

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

private val LightColors = lightColorScheme(
    primary = ThaparRed,
    onPrimary = Color.White,

    secondary = ThaparDarkRed,
    onSecondary = Color.White,

    background = LightBackground,
    onBackground = BlackPrimary,

    surface = LightSurface,
    onSurface = BlackPrimary,

    surfaceVariant = Color(0xFFF0F0F0),
    onSurfaceVariant = BlackPrimary,

    primaryContainer = Color(0xFFFFDAD6),
    onPrimaryContainer = Color(0xFF410002)
)

private val DarkColors = darkColorScheme(
    primary = ThaparRed,
    onPrimary = Color.White,

    secondary = ThaparDarkRed,
    onSecondary = Color.White,

    background = BlackPrimary,
    onBackground = Color.White,

    surface = DarkSurface,
    onSurface = Color.White,

    surfaceVariant = Color(0xFF2A2A2A),
    onSurfaceVariant = Color.White,

    primaryContainer = Color(0xFF93001A),
    onPrimaryContainer = Color.White
)

@Composable
fun ThaparBitesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {

    val colorScheme =
        if (darkTheme) DarkColors
        else LightColors

    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window

            window.statusBarColor =
                colorScheme.primary.toArgb()

            WindowCompat
                .getInsetsController(window, view)
                .isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = ShopTypography,
        content = content
    )
}