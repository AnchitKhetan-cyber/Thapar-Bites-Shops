package com.ccs.thaparbitesshop.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import com.ccs.thaparbitesshop.R
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SplashScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToDashboard: () -> Unit
) {

    // Animate scale of logo
    val scale = remember { Animatable(0.4f) }
    // Animate visibility of text
    var textVisible by remember { mutableStateOf(false) }
    // Animate visibility of tagline
    var taglineVisible by remember { mutableStateOf(false) }

    val primaryColor = Color(0xFF7A1F3D)
    val secondaryColor = Color(0xFF4A1025)
    val accentColor = Color(0xFFC9A227)

    LaunchedEffect(Unit) {
        // Logo pop-in
        scale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio  = Spring.DampingRatioMediumBouncy,
                stiffness     = Spring.StiffnessLow
            )
        )
        delay(200)
        textVisible = true
        delay(300)
        taglineVisible = true
        delay(1800)

        val currentUser =
            FirebaseAuth.getInstance().currentUser

        if (currentUser != null) {
            onNavigateToDashboard()
        } else {
            onNavigateToLogin()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        secondaryColor,
                        primaryColor,
                        accentColor.copy(alpha = 0.9f)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo Circle
            Box(
                modifier = Modifier
                    .scale(scale.value)
                    .size(140.dp)
                    .clip(CircleShape)
                    .background(
                        accentColor.copy(alpha = 0.15f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.thapar_bites_logo),
                        contentDescription = null,
                        modifier = Modifier
                            .size(120.dp)
                            .clip(RoundedCornerShape(70.dp))
                    )
                }
            }

            Spacer(Modifier.height(28.dp))

            // App name
            AnimatedVisibility(
                visible = textVisible,
                enter   = fadeIn() + slideInVertically(initialOffsetY = { it / 2 })
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text       = "Thapar Bites",
                        color      = Color.White,
                        fontSize   = 36.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = (-0.5).sp
                    )
                    Text(
                        text       = "SHOP",
                        color      = Color.White.copy(alpha = 0.85f),
                        fontSize   = 14.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 8.sp
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // Tagline
            AnimatedVisibility(
                visible = taglineVisible,
                enter   = fadeIn(animationSpec = tween(600))
            ) {
                Text(
                    text = "Good Food. Great Campus.",
                    color = Color.White.copy(alpha = 0.75f),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // Loading indicator at bottom
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 48.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            AnimatedVisibility(
                visible = taglineVisible,
                enter   = fadeIn(animationSpec = tween(400))
            ) {
                CircularProgressIndicator(
                    color = accentColor,
                    strokeWidth = 2.5.dp,
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    }
}