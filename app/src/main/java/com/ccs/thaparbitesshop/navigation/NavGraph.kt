package com.ccs.thaparbitesshop.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ccs.thaparbitesshop.ui.menu.AddFoodScreen
import com.ccs.thaparbitesshop.ui.menu.MenuViewModel
import com.ccs.thaparbitesshop.ui.orderdetail.OrderDetailScreen
import com.ccs.thaparbitesshop.ui.orders.OrdersScreen
import com.ccs.thaparbitesshop.ui.screens.HomeScreen
import com.ccs.thaparbitesshop.ui.screens.LoginScreen
import com.ccs.thaparbitesshop.ui.screens.MenuScreen
import com.ccs.thaparbitesshop.ui.screens.ProfileScreen
import com.ccs.thaparbitesshop.ui.screens.RegisterScreen
import com.ccs.thaparbitesshop.ui.screens.SplashScreen
import com.ccs.thaparbitesshop.utils.GoogleAuthHelper
import kotlinx.coroutines.launch

@Composable
fun ShopNavGraph(
    navController: NavHostController
) {

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {

        // Splash
        composable(Screen.Splash.route) {

            SplashScreen(

                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Splash.route) {
                            inclusive = true
                        }
                    }
                },

                onNavigateToDashboard = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Splash.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        // Login
        composable(Screen.Login.route) {

            val loginViewModel = hiltViewModel<com.ccs.thaparbitesshop.ui.login.LoginViewModel>()

            val context = LocalContext.current
            val scope = rememberCoroutineScope()

            LoginScreen(
                viewModel = loginViewModel,

                onLoginSuccess = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) {
                            inclusive = true
                        }
                    }
                },

                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },

                onGoogleSignIn = {

                    scope.launch {

                        try {

                            val token = GoogleAuthHelper(
                                context
                            ).signIn()

                            if (token != null) {
                                loginViewModel.signInWithGoogle(token)
                            }

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            )
        }

        // Register
        composable(Screen.Register.route) {

            RegisterScreen(
                onRegisterSuccess = {

                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) {
                            inclusive = true
                        }
                    }
                },

                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // Dashboard
        composable(Screen.Dashboard.route) {

            HomeScreen(

                onNavigateToOrders = {
                    navController.navigate(Screen.Orders.route)
                },

                onNavigateToMenu = {
                    navController.navigate(Screen.Menu.route)
                },

                onNavigateToProfile = {
                    navController.navigate(Screen.Profile.route)
                },

                onOrderClick = { orderId ->

                    navController.navigate(
                        Screen.OrderDetail.createRoute(orderId)
                    )
                }
            )
        }

        // Orders
        composable(Screen.Orders.route) {

            OrdersScreen(
                viewModel = hiltViewModel(),

                onOrderClick = { orderId ->

                    navController.navigate(
                        Screen.OrderDetail.createRoute(orderId)
                    )
                }
            )
        }

        // Order Detail
        composable(
            route = Screen.OrderDetail.route,
            arguments = listOf(
                navArgument("orderId") {
                    type = NavType.StringType
                }
            )
        ) {

            OrderDetailScreen(
                viewModel = hiltViewModel(),

                onBack = {
                    navController.popBackStack()
                }
            )
        }

        // Menu
        composable(Screen.Menu.route) {

            MenuScreen(
                onBack = {
                    navController.popBackStack()
                },
                onNavigateToAddFood = {
                    navController.navigate(
                        Screen.AddFood.route
                    )
                }
            )
        }

        // Profile
        composable(Screen.Profile.route) {

            ProfileScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.AddFood.route) {

            val menuViewModel: MenuViewModel =
                viewModel()

            AddFoodScreen(
                viewModel = menuViewModel,
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}