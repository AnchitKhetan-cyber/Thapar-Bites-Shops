package com.ccs.thaparbitesshop.navigation

sealed class Screen(val route: String) {
    object Splash      : Screen("splash")
    object Login       : Screen("login")
    object Register    : Screen("register")
    object Dashboard   : Screen("dashboard")
    object Orders      : Screen("orders")
    object OrderDetail : Screen("order_detail/{orderId}") {
        fun createRoute(orderId: String) = "order_detail/$orderId"
    }
    object Menu        : Screen("menu")
    object AddEditItem : Screen("add_edit_item/{itemId}") {
        fun createRoute(itemId: String = "new") = "add_edit_item/$itemId"
    }
    object Profile     : Screen("profile")
    object Analytics   : Screen("analytics")
    object Settings    : Screen("settings")
}