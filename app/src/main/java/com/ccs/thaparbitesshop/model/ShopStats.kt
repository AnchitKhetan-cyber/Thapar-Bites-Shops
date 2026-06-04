package com.ccs.thaparbitesshop.model

/** Live stats computed from today's orders */
data class ShopStats(
    val todayOrders: Int = 0,
    val pendingOrders: Int = 0,
    val todayRevenue: Double = 0.0,
    val menuItemCount: Int = 0
)

// ShopInfo already exists in Models.kt — adding only new fields here via
// companion extensions to avoid touching the existing file.
// If you want a single source of truth, merge these fields into Models.kt:
//
//   val stats: ShopStats = ShopStats()