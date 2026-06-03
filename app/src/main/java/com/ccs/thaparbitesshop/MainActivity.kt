package com.ccs.thaparbitesshop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ccs.thaparbitesshop.ui.splash.SplashScreen
import com.ccs.thaparbitesshop.ui.theme.ThaparBitesShopTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Keep native splash gone before Compose takes over
        installSplashScreen()

        setContent {
            ThaparBitesShopTheme {
                val navController = rememberNavController()

                NavHost(
                    navController    = navController,
                    startDestination = "splash",
                ) {
                    composable("splash") {
                        SplashScreen(
                            onNavigateToLogin = {
                                navController.navigate("login") {
                                    popUpTo("splash") { inclusive = true }
                                }
                            },
                            onNavigateToHome = {
                                navController.navigate("home") {
                                    popUpTo("splash") { inclusive = true }
                                }
                            },
                        )
                    }
                    composable("login") { /* LoginScreen() */ }
                    composable("home")  { /* DashboardScreen() */ }
                }
            }
        }
    }
}

