package com.ccs.thaparbitesshop.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ccs.thaparbitesshop.ui.orderdetail.OrderDetailScreen
import com.ccs.thaparbitesshop.ui.orders.OrdersScreen
import com.ccs.thaparbitesshop.ui.screens.HomeScreen
import com.ccs.thaparbitesshop.ui.screens.LoginScreen
import com.ccs.thaparbitesshop.ui.screens.RegisterScreen
import com.ccs.thaparbitesshop.ui.screens.SplashScreen

@Composable
fun ShopNavGraph(
    navController: NavHostController
) {

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {

        composable(Screen.Splash.route) {

            SplashScreen(
                onNavigateToLogin = {
                    navController.navigate(
                        Screen.Login.route
                    ) {
                        popUpTo(
                            Screen.Splash.route
                        ) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(Screen.Login.route) {

            LoginScreen(
                onLoginSuccess = {

                    navController.navigate(
                        Screen.Dashboard.route
                    ) {
                        popUpTo(
                            Screen.Login.route
                        ) {
                            inclusive = true
                        }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(
                        Screen.Register.route
                    )
                }
            )
        }

        composable(Screen.Register.route) {

            RegisterScreen(
                onRegisterSuccess = {

                    navController.navigate(
                        Screen.Dashboard.route
                    ) {
                        popUpTo(
                            Screen.Login.route
                        ) {
                            inclusive = true
                        }
                    }
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Dashboard.route) {

            HomeScreen(
                onNavigateToOrders = {
                    navController.navigate(
                        Screen.Orders.route
                    )
                },
                onNavigateToMenu = {
                    navController.navigate(
                        Screen.Menu.route
                    )
                },
                onNavigateToProfile = {
                    navController.navigate(
                        Screen.Profile.route
                    )
                },
                onOrderClick = { orderId ->

                    navController.navigate(
                        Screen.OrderDetail.createRoute(
                            orderId
                        )
                    )
                }
            )
        }

        composable(Screen.Orders.route) {

            OrdersScreen(
                viewModel = hiltViewModel(),
                onOrderClick = { orderId ->

                    navController.navigate(
                        Screen.OrderDetail.createRoute(
                            orderId
                        )
                    )
                }
            )
        }

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
    }
}