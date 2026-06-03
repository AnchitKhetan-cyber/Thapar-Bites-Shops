package com.ccs.thaparbitesshop.ui.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ccs.thaparbitesshop.ui.theme.Crimson500
import com.ccs.thaparbitesshop.ui.theme.Crimson700
import com.ccs.thaparbitesshop.ui.theme.Gold200
import com.ccs.thaparbitesshop.ui.theme.Poppins
import com.ccs.thaparbitesshop.ui.theme.White
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// ─────────────────────────────────────────────────────────────
// SplashScreen
//
// Flow:
//   1. Crimson background fades in
//   2. Logo icon bounces in (scale + alpha)
//   3. App name slides up + fades in
//   4. Tagline fades in
//   5. After 2.5 s → check Firebase auth state → navigate
//
// Usage in NavGraph:
//   composable("splash") {
//       SplashScreen(
//           onNavigateToLogin     = { navController.navigate("login") { popUpTo("splash") { inclusive = true } } },
//           onNavigateToHome      = { navController.navigate("home")  { popUpTo("splash") { inclusive = true } } },
//       )
//   }
// ─────────────────────────────────────────────────────────────

@Composable
fun SplashScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToHome : () -> Unit,
    viewModel        : SplashViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
) {
    val authState by viewModel.authState.collectAsState()

    // ── Animation states ──────────────────────────────────────

    // 1. Background
    val bgAlpha = remember { Animatable(0f) }

    // 2. Logo icon — bouncy scale-in
    val logoScale = remember { Animatable(0.3f) }
    val logoAlpha = remember { Animatable(0f) }

    // 3. App name — slide up + fade
    val nameOffsetY = remember { Animatable(40f) }
    val nameAlpha   = remember { Animatable(0f) }

    // 4. Tagline — fade only
    val tagAlpha = remember { Animatable(0f) }

    // 5. "SHOP" badge
    val badgeAlpha = remember { Animatable(0f) }
    val badgeScale = remember { Animatable(0.6f) }

    LaunchedEffect(Unit) {
        // Step 1 — bg
        bgAlpha.animateTo(1f, tween(300))

        // Step 2 — logo bounces in
        launch {
            logoAlpha.animateTo(1f, tween(400))
        }
        logoScale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness    = Spring.StiffnessMedium,
            )
        )

        delay(100)

        // Step 3 — app name slides up
        launch { nameAlpha.animateTo(1f, tween(400)) }
        nameOffsetY.animateTo(0f, tween(400, easing = FastOutSlowInEasing))

        delay(80)

        // Step 4 — tagline
        tagAlpha.animateTo(1f, tween(350))

        delay(80)

        // Step 5 — SHOP badge
        launch { badgeAlpha.animateTo(1f, tween(300)) }
        badgeScale.animateTo(1f, spring(dampingRatio = Spring.DampingRatioLowBouncy))

        // Wait, then navigate based on auth
        delay(900)
    }

    // Navigate when auth state is resolved
    LaunchedEffect(authState) {
        when (authState) {
            AuthState.LoggedIn  -> onNavigateToHome()
            AuthState.LoggedOut -> onNavigateToLogin()
            AuthState.Loading   -> { /* wait */ }
        }
    }

    // ── UI ────────────────────────────────────────────────────
    Box(
        modifier = Modifier
            .fillMaxSize()
            .alpha(bgAlpha.value)
            .background(Crimson500),
        contentAlignment = Alignment.Center,
    ) {

        // Subtle bottom decoration strip
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .align(Alignment.BottomCenter)
                .background(Crimson700),
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {

            // ── Logo icon ─────────────────────────────────────
            // Replace this Box with your actual Image(painterResource(R.drawable.ic_logo))
            Box(
                modifier = Modifier
                    .scale(logoScale.value)
                    .alpha(logoAlpha.value)
                    .size(100.dp)
                    .background(
                        color        = White.copy(alpha = 0.15f),
                        shape        = androidx.compose.foundation.shape.CircleShape,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text  = "🍽",
                    fontSize = 48.sp,
                )
            }

            Spacer(Modifier.height(28.dp))

            // ── App name ──────────────────────────────────────
            Box(
                modifier = Modifier
                    .alpha(nameAlpha.value)
                    .offset(y = nameOffsetY.value.dp),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text       = "Thapar Bites",
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        fontSize   = 34.sp,
                        color      = White,
                    )
                    Spacer(Modifier.width(10.dp))
                    // ── SHOP badge ────────────────────────────
                    Box(
                        modifier = Modifier
                            .alpha(badgeAlpha.value)
                            .scale(badgeScale.value)
                            .background(
                                color  = Gold200,
                                shape  = androidx.compose.foundation.shape.RoundedCornerShape(6.dp),
                            )
                            .padding(horizontal = 8.dp, vertical = 3.dp),
                    ) {
                        Text(
                            text       = "SHOP",
                            fontFamily = Poppins,
                            fontWeight = FontWeight.Bold,
                            fontSize   = 13.sp,
                            color      = Crimson700,
                            letterSpacing = 1.sp,
                        )
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            // ── Tagline ───────────────────────────────────────
            Text(
                modifier   = Modifier.alpha(tagAlpha.value),
                text       = "Manage your shop. Feed the campus.",
                fontFamily = Poppins,
                fontWeight = FontWeight.Normal,
                fontSize   = 14.sp,
                color      = White.copy(alpha = 0.75f),
                textAlign  = TextAlign.Center,
            )
        }

        // ── Version tag ───────────────────────────────────────
        Text(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
                .alpha(tagAlpha.value),
            text       = "v1.0.0",
            fontFamily = Poppins,
            fontWeight = FontWeight.Normal,
            fontSize   = 12.sp,
            color      = White.copy(alpha = 0.4f),
        )
    }
}