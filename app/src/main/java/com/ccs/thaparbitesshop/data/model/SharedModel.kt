package com.ccs.thaparbitesshop.data.model

import com.google.firebase.Timestamp

// ─────────────────────────────────────────────────────────────────────────────
// SHARED FIRESTORE SCHEMA
// Both Thapar-Bites (student) and Thapar-Bites-Shops read/write these.
// Keep field names identical in both apps.
// ─────────────────────────────────────────────────────────────────────────────

// ── Collection paths ──────────────────────────────────────────────────────────

object FirestorePaths {
    const val SHOPS    = "shops"       // shops/{shopUid}
    const val MENUS    = "menus"       // shops/{shopUid}/menus/{itemId}
    const val ORDERS   = "orders"      // orders/{orderId}
    const val USERS    = "users"       // users/{studentUid}
    const val ADMINS   = "admins"      // admins/{adminUid}
}

// ── Shop document: shops/{shopUid} ────────────────────────────────────────────

data class ShopOwner(
    val uid: String           = "",
    val ownerName: String     = "",
    val email: String         = "",
    val shopName: String      = "",
    val location: String      = "",       // "Near LT-6", "Hostel Block C", etc.
    val category: String      = ShopCategory.FOOD.name,
    val isActive: Boolean     = true,     // admin can deactivate without deleting
    val isOpen: Boolean       = false,    // shop owner toggles this
    val createdAt: Timestamp? = null,
    val createdByAdmin: String = ""       // uid of admin who created this account
) {
    fun toMap(): Map<String, Any?> = mapOf(
        "uid"            to uid,
        "ownerName"      to ownerName,
        "email"          to email,
        "shopName"       to shopName,
        "location"       to location,
        "category"       to category,
        "isActive"       to isActive,
        "isOpen"         to isOpen,
        "createdAt"      to createdAt,
        "createdByAdmin" to createdByAdmin
    )
}

// ── Menu item: shops/{shopUid}/menus/{itemId} ─────────────────────────────────
// Student app reads this to show the menu.
// Shop app writes this to manage items.

data class MenuItem(
    val itemId: String        = "",
    val shopUid: String       = "",
    val name: String          = "",
    val description: String   = "",
    val price: Double         = 0.0,
    val category: String      = "",       // "Snacks", "Beverages", etc.
    val isAvailable: Boolean  = true,
    val imageUrl: String      = ""        // Firebase Storage URL
) {
    fun toMap(): Map<String, Any?> = mapOf(
        "itemId"       to itemId,
        "shopUid"      to shopUid,
        "name"         to name,
        "description"  to description,
        "price"        to price,
        "category"     to category,
        "isAvailable"  to isAvailable,
        "imageUrl"     to imageUrl
    )
}

// ── Order document: orders/{orderId} ─────────────────────────────────────────
// Student app creates orders.
// Shop app listens for new orders and updates status.

data class Order(
    val orderId: String          = "",
    val shopUid: String          = "",       // which shop this order belongs to
    val shopName: String         = "",
    val studentUid: String       = "",
    val studentName: String      = "",
    val items: List<OrderItem>   = emptyList(),
    val totalAmount: Double      = 0.0,
    val status: String           = OrderStatus.PENDING.name,
    val placedAt: Timestamp?     = null,
    val updatedAt: Timestamp?    = null,
    val note: String             = ""        // optional note from student
)

data class OrderItem(
    val itemId: String   = "",
    val name: String     = "",
    val price: Double    = 0.0,
    val quantity: Int    = 1
)

// ── Order status (shared between both apps) ───────────────────────────────────

enum class OrderStatus(val displayName: String, val emoji: String) {
    PENDING("Pending", "⏳"),
    ACCEPTED("Accepted", "✅"),
    PREPARING("Preparing", "👨‍🍳"),
    READY("Ready for pickup", "🔔"),
    DELIVERED("Delivered", "📦"),
    CANCELLED("Cancelled", "❌");

    companion object {
        fun fromName(name: String) =
            entries.firstOrNull { it.name == name } ?: PENDING
    }
}

// ── Shop categories ───────────────────────────────────────────────────────────

enum class ShopCategory(val displayName: String, val emoji: String) {
    FOOD("Food & Snacks",  "🍱"),
    BEVERAGES("Beverages", "☕"),
    STATIONARY("Stationary", "📚"),
    PHARMACY("Pharmacy",   "💊"),
    GROCERY("Grocery",     "🛒"),
    OTHER("Other",         "🏪");

    companion object {
        fun fromName(name: String) =
            entries.firstOrNull { it.name == name } ?: OTHER
    }
}