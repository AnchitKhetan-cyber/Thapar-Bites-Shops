package com.ccs.thaparbitesshop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ccs.thaparbitesshop.ui.screens.LoginScreen
import com.ccs.thaparbitesshop.ui.screens.ShopDashboardScreen
import com.example.thaparbites.shops.ui.screens.SplashScreen
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

/**
 * MainActivity
 *
 * Entry point. Sets up the NavHost with three destinations:
 *
 *   splash → login → dashboard
 *
 * If the shop owner is already signed in (persistent Firebase session),
 * we skip straight to the dashboard on launch.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ThaparBitesShopsApp()
        }
    }
}

@Composable
private fun ThaparBitesShopsApp() {
    val navController = rememberNavController()

    // If a session is already active, start directly on the dashboard.
    val alreadyLoggedIn = Firebase.auth.currentUser != null
    val startDest = if (alreadyLoggedIn) "dashboard" else "splash"

    NavHost(
        navController    = navController,
        startDestination = startDest
    ) {

        // ── Splash ────────────────────────────────────────────────────────────
        composable("splash") {
            SplashScreen(
                onSplashFinished = {
                    navController.navigate("login") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }

        // ── Login ─────────────────────────────────────────────────────────────
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("dashboard") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onForgotPassword = {
                    navController.navigate("forgot_password")
                }
            )
        }

        // ── Placeholder destinations (replace with your actual screens) ───────
        composable("dashboard") {
            ShopDashboardScreen(
                onNavigateToOrders   = { navController.navigate("orders") },
                onNavigateToMenu     = { navController.navigate("menu") },
                onNavigateToProfile  = { navController.navigate("profile") },
                onNavigateToAnalytics= { navController.navigate("analytics") },
                onLogout             = {
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable("forgot_password") {
            // TODO: replace with your ForgotPasswordScreen()
            androidx.compose.material3.Text("Forgot Password — coming soon")
        }
    }
}