package com.ccs.thaparbitesshop.navigation

sealed class Screen(val route: String) {

    object Splash : Screen("splash")

    object Login : Screen("login")

    object Register : Screen("register")

    object Dashboard : Screen("dashboard")

    object Orders : Screen("orders")

    object Menu : Screen("menu")

    object Profile : Screen("profile")

    object OrderDetail : Screen("order_detail/{orderId}") {

        fun createRoute(orderId: String): String {
            return "order_detail/$orderId"
        }
    }

    object AddFood : Screen("add_food")
}