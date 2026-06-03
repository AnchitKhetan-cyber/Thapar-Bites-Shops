package com.ccs.thaparbitesshop.ui.dashboard.model

import com.google.firebase.Timestamp

// ── Order status ──────────────────────────────────────────────
enum class OrderStatus {
    PENDING,
    ACCEPTED,
    READY,
    DELIVERED,
    CANCELLED;

    companion object {
        fun from(value: String): OrderStatus = when (value.uppercase()) {
            "PENDING"   -> PENDING
            "ACCEPTED"  -> ACCEPTED
            "READY"     -> READY
            "DELIVERED" -> DELIVERED
            else        -> CANCELLED
        }
    }
}

// ── Single item inside an order ───────────────────────────────
data class OrderItem(
    val itemId   : String = "",
    val name     : String = "",
    val price    : Int    = 0,
    val quantity : Int    = 1,
)

// ── Full order ────────────────────────────────────────────────
data class Order(
    val orderId       : String          = "",
    val shopId        : String          = "",
    val customerId    : String          = "",
    val customerName  : String          = "",
    val customerPhone : String          = "",
    val items         : List<OrderItem> = emptyList(),
    val totalAmount   : Int             = 0,
    val status        : String          = "PENDING",
    val createdAt     : Timestamp?      = null,
    val note          : String          = "",
) {
    val orderStatus get() = OrderStatus.from(status)
    val shortId     get() = "#${orderId.takeLast(4).uppercase()}"
}

// ── Shop document ─────────────────────────────────────────────
data class Shop(
    val shopId   : String  = "",
    val name     : String  = "",
    val ownerId  : String  = "",
    val email    : String  = "",
    val phone    : String  = "",
    val isOpen   : Boolean = false,
    val status   : String  = "closed",   // "open" | "closed" | "busy"
    val fcmToken : String  = "",
)

// ── Today's summary shown on dashboard ───────────────────────
data class DashboardStats(
    val totalOrders    : Int = 0,
    val pendingOrders  : Int = 0,
    val totalRevenue   : Int = 0,
    val completedOrders: Int = 0,
)