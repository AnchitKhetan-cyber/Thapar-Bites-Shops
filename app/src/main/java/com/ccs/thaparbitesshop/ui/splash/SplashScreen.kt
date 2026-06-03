package com.example.thaparbites.shops.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

// Brand colors — keep in sync with your theme
private val OrangePrimary = Color(0xFFFF6B00)
private val OrangeDark   = Color(0xFFCC5500)
private val BgDark       = Color(0xFF1A1A1A)
private val TextWhite    = Color(0xFFFAFAFA)
private val TextMuted    = Color(0xFFB0B0B0)

/**
 * SplashScreen
 *
 * Shown for ~2 s on cold launch. Animates the logo icon, app name, and
 * tagline in with a scale + fade sequence, then calls [onSplashFinished]
 * so the NavHost can navigate to LoginScreen.
 *
 * Usage in NavHost:
 *   composable("splash") {
 *       SplashScreen(onSplashFinished = { navController.navigate("login") {
 *           popUpTo("splash") { inclusive = true }
 *       }})
 *   }
 */
@Composable
fun SplashScreen(onSplashFinished: () -> Unit) {

    // ── Animations ────────────────────────────────────────────────────────────

    // Logo: scale from 0.4 → 1.0, alpha 0 → 1
    val logoScale = remember { Animatable(0.4f) }
    val logoAlpha = remember { Animatable(0f) }

    // App name: fades in after logo settles
    val titleAlpha = remember { Animatable(0f) }

    // Tagline: fades in last
    val taglineAlpha = remember { Animatable(0f) }

    // "Shop Partner" badge: slides up + fades in
    val badgeOffsetY = remember { Animatable(20f) }
    val badgeAlpha  = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        // 1. Logo pop
        logoScale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness    = Spring.StiffnessMedium
            )
        )
        logoAlpha.animateTo(1f, tween(300))

        // 2. Title
        delay(100)
        titleAlpha.animateTo(1f, tween(400))

        // 3. Tagline + badge together
        delay(150)
        taglineAlpha.animateTo(1f, tween(400))
        badgeAlpha.animateTo(1f, tween(350))
        badgeOffsetY.animateTo(0f, tween(350, easing = EaseOutCubic))

        // 4. Hold then exit
        delay(900)
        onSplashFinished()
    }

    // ── UI ────────────────────────────────────────────────────────────────────

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDark),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // ── Logo icon (emoji placeholder — swap with your actual drawable) ──
            Text(
                text = "🍽️",
                fontSize = 72.sp,
                modifier = Modifier
                    .scale(logoScale.value)
                    .alpha(logoAlpha.value)
            )

            Spacer(Modifier.height(20.dp))

            // ── App name ──────────────────────────────────────────────────────
            Text(
                text = "Thapar Bites",
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                color = OrangePrimary,
                letterSpacing = 1.sp,
                modifier = Modifier.alpha(titleAlpha.value)
            )

            Spacer(Modifier.height(6.dp))

            // ── Tagline ───────────────────────────────────────────────────────
            Text(
                text = "Campus food, simplified.",
                fontSize = 14.sp,
                color = TextMuted,
                letterSpacing = 0.5.sp,
                modifier = Modifier.alpha(taglineAlpha.value)
            )

            Spacer(Modifier.height(24.dp))

            // ── "Shop Partner" badge ──────────────────────────────────────────
            Box(
                modifier = Modifier
                    .alpha(badgeAlpha.value)
                    .offset(y = badgeOffsetY.value.dp)
                    .background(
                        color = OrangeDark.copy(alpha = 0.18f),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(50)
                    )
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "Shop Partner Portal",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = OrangePrimary,
                    letterSpacing = 0.8.sp,
                    textAlign = TextAlign.Center
                )
            }
        }

        // ── Bottom credit ─────────────────────────────────────────────────────
        Text(
            text = "Thapar Institute of Engineering & Technology",
            fontSize = 11.sp,
            color = TextMuted.copy(alpha = 0.5f),
            letterSpacing = 0.3.sp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 28.dp)
                .alpha(taglineAlpha.value)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SplashScreenPreview() {
    SplashScreen(onSplashFinished = {})
}