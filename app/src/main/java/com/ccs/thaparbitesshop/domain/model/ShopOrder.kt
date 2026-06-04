package com.ccs.thaparbitesshop.domain.model

import com.google.firebase.Timestamp

/**
 * Firestore document shape (collection: shops/{shopId}/orders/{orderId}):
 *
 * {
 *   tokenNumber: 42,
 *   customerName: "Rahul",
 *   customerPhone: "9876543210",
 *   items: ["2x Chole Bhature", "1x Chai"],
 *   itemDetails: [{ name, qty, price }, ...],
 *   totalAmount: 185.0,
 *   status: "NEW",
 *   paymentMethod: "UPI",
 *   note: "Less spicy",
 *   placedAt: <Timestamp>
 * }
 */
data class ShopOrder(
    val id: String = "",
    val tokenNumber: Int = 0,
    val customerName: String = "",
    val customerPhone: String = "",
    /** Human-readable item strings e.g. "2x Chole Bhature" */
    val items: List<String> = emptyList(),
    val itemDetails: List<OrderItemDetail> = emptyList(),
    val totalAmount: Double = 0.0,
    val status: OrderStatus = OrderStatus.NEW,
    val paymentMethod: String = "",
    val note: String = "",
    val placedAt: Timestamp? = null,
) {
    /** Derived display string, e.g. "2 min ago" — computed in ViewModel */
    val timeAgo: String get() = ""   // kept for compatibility; UI uses placedAt
}

data class OrderItemDetail(
    val name: String = "",
    val qty: Int = 0,
    val price: Double = 0.0
)