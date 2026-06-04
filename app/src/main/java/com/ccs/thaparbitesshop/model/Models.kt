package com.ccs.thaparbitesshop.model

// ── Shop / Outlet ───────────────────────────────────────────────

// ── Menu Item ───────────────────────────────────────────────────
data class MenuItem(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val category: String,
    val imageUrl: String = "",
    val isAvailable: Boolean = true,
    val isVeg: Boolean = true,
    val preparationTime: Int = 10   // minutes
)

// ── Order ────────────────────────────────────────────────────────
data class Order(
    val id: String,
    val customerName: String,
    val customerPhone: String,
    val items: List<OrderItem>,
    val totalAmount: Double,
    val status: OrderStatus,
    val placedAt: String,           // formatted time string for display
    val tokenNumber: Int,
    val specialInstructions: String = ""
)

data class OrderItem(
    val menuItem: MenuItem,
    val quantity: Int
)

enum class OrderStatus(val label: String) {
    PENDING("Pending"),
    PREPARING("Preparing"),
    READY("Ready"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled")
}

// ── Category ────────────────────────────────────────────────────
data class MenuCategory(
    val id: String,
    val name: String,
    val itemCount: Int = 0
)

// ── Earnings Summary (for Dashboard) ───────────────────────────
data class EarningsSummary(
    val todayEarnings: Double,
    val weekEarnings: Double,
    val monthEarnings: Double,
    val totalOrders: Int,
    val pendingOrders: Int,
    val completedOrders: Int
)

// ── Sample / dummy data ─────────────────────────────────────────
object SampleData {

    val categories = listOf(
        MenuCategory("cat_1", "Snacks", 8),
        MenuCategory("cat_2", "Main Course", 12),
        MenuCategory("cat_3", "Beverages", 6),
        MenuCategory("cat_4", "Desserts", 4),
        MenuCategory("cat_5", "Combos", 5)
    )

    val menuItems = listOf(
        MenuItem("m1", "Aloo Paratha", "Stuffed potato flatbread with butter", 60.0, "Main Course", isVeg = true, preparationTime = 15),
        MenuItem("m2", "Paneer Tikka", "Grilled spiced cottage cheese", 120.0, "Snacks", isVeg = true, preparationTime = 12),
        MenuItem("m3", "Chicken Roll", "Crispy chicken wrapped in paratha", 90.0, "Snacks", isVeg = false, preparationTime = 10),
        MenuItem("m4", "Maggi", "Classic instant noodles", 40.0, "Snacks", isVeg = true, preparationTime = 8),
        MenuItem("m5", "Masala Chai", "Ginger cardamom spiced tea", 20.0, "Beverages", isVeg = true, preparationTime = 5),
        MenuItem("m6", "Cold Coffee", "Blended iced coffee shake", 50.0, "Beverages", isVeg = true, preparationTime = 5),
        MenuItem("m7", "Rajma Chawal", "Red kidney beans with steamed rice", 80.0, "Main Course", isVeg = true, preparationTime = 15),
        MenuItem("m8", "Gulab Jamun", "Soft milk-solid dumplings in sugar syrup", 30.0, "Desserts", isVeg = true, preparationTime = 3, isAvailable = false)
    )

    val orders = listOf(
        Order("ord_001", "Rahul Sharma", "9876543210",
            listOf(OrderItem(menuItems[0], 2), OrderItem(menuItems[4], 1)),
            140.0, OrderStatus.PENDING, "10:30 AM", 1),
        Order("ord_002", "Priya Singh", "9812345678",
            listOf(OrderItem(menuItems[1], 1), OrderItem(menuItems[5], 2)),
            220.0, OrderStatus.PREPARING, "10:35 AM", 2),
        Order("ord_003", "Amit Kumar", "9898989898",
            listOf(OrderItem(menuItems[3], 3)),
            120.0, OrderStatus.READY, "10:20 AM", 3, "Extra spicy"),
        Order("ord_004", "Neha Verma", "9765432109",
            listOf(OrderItem(menuItems[2], 1), OrderItem(menuItems[3], 1)),
            130.0, OrderStatus.COMPLETED, "10:00 AM", 4),
        Order("ord_005", "Vikram Patel", "9654321098",
            listOf(OrderItem(menuItems[6], 2)),
            160.0, OrderStatus.PENDING, "10:40 AM", 5),
        Order("ord_006", "Sunita Rao", "9543210987",
            listOf(OrderItem(menuItems[4], 2), OrderItem(menuItems[7], 2)),
            100.0, OrderStatus.CANCELLED, "9:50 AM", 6)
    )

    val earningsSummary = EarningsSummary(
        todayEarnings   = 3450.0,
        weekEarnings    = 22800.0,
        monthEarnings   = 95600.0,
        totalOrders     = 38,
        pendingOrders   = 4,
        completedOrders = 32
    )

    val shopInfo = ShopInfo()
}