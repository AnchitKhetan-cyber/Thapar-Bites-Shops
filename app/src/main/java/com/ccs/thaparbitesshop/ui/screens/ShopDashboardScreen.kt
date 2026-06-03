package com.ccs.thaparbitesshop.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Firebase
import com.google.firebase.auth.auth


// ── Brand tokens ──────────────────────────────────────────────────────────────
private val OrangePrimary   = Color(0xFFFF6B00)
private val OrangeLight     = Color(0xFFFF8C38)
private val BgDark          = Color(0xFF1A1A1A)
private val SurfaceDark     = Color(0xFF242424)
private val SurfaceElevated = Color(0xFF2E2E2E)
private val BorderColor     = Color(0xFF3A3A3A)
private val TextWhite       = Color(0xFFFAFAFA)
private val TextMuted       = Color(0xFFB0B0B0)
private val GreenAccent     = Color(0xFF4CAF50)
private val RedAccent       = Color(0xFFFF5252)
private val AmberAccent     = Color(0xFFFFC107)

// ── Data models ───────────────────────────────────────────────────────────────

data class StatCard(
    val label: String,
    val value: String,
    val sub: String,
    val color: Color,
    val emoji: String
)

data class RecentOrder(
    val orderId: String,
    val itemSummary: String,
    val studentName: String,
    val amount: String,
    val status: OrderStatus,
    val timeAgo: String
)

enum class OrderStatus { PENDING, PREPARING, READY, DELIVERED }

/**
 * ShopDashboardScreen
 *
 * Home screen shown right after login. Displays:
 *  - Greeting header with shop open/closed toggle
 *  - Today's stat cards (orders, revenue, pending, rating)
 *  - Quick action buttons (Menu, Orders, Profile, Analytics)
 *  - Recent orders list
 *
 * NavHost usage:
 *   composable("dashboard") {
 *       ShopDashboardScreen(
 *           onNavigateToOrders  = { navController.navigate("orders") },
 *           onNavigateToMenu    = { navController.navigate("menu") },
 *           onNavigateToProfile = { navController.navigate("profile") },
 *           onLogout            = { navController.navigate("login") { popUpTo(0) } }
 *       )
 *   }
 *
 * TODO: Replace sampleStats / sampleOrders with real Firestore streams via ViewModel.
 */
@Composable
fun ShopDashboardScreen(
    onNavigateToOrders:   () -> Unit = {},
    onNavigateToMenu:     () -> Unit = {},
    onNavigateToProfile:  () -> Unit = {},
    onNavigateToAnalytics:() -> Unit = {},
    onLogout:             () -> Unit = {}
) {
    // ── State ─────────────────────────────────────────────────────────────────
    var isShopOpen by remember { mutableStateOf(true) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    val shopName = Firebase.auth.currentUser?.displayName
        ?.takeIf { it.isNotBlank() } ?: "Your Shop"

    val scrollState = rememberScrollState()

    // ── Sample data (swap with ViewModel + Firestore) ─────────────────────────
    val stats = listOf(
        StatCard("Today's Orders",  "24",    "+3 from yesterday", OrangePrimary, "📦"),
        StatCard("Revenue",         "₹3,840","Today's earnings",  GreenAccent,   "💰"),
        StatCard("Pending",         "5",     "Awaiting prep",     AmberAccent,   "⏳"),
        StatCard("Rating",          "4.7★",  "Last 30 days",      OrangeLight,   "⭐"),
    )

    val recentOrders = listOf(
        RecentOrder("#1042", "Maggi + Chai × 2",        "Rahul S.",  "₹120", OrderStatus.PENDING,    "2 min ago"),
        RecentOrder("#1041", "Chole Bhature",            "Priya M.",  "₹90",  OrderStatus.PREPARING,  "8 min ago"),
        RecentOrder("#1040", "Bread Omelette + Coffee",  "Aryan K.",  "₹110", OrderStatus.READY,      "14 min ago"),
        RecentOrder("#1039", "Samosa × 4 + Lassi",       "Sneha R.",  "₹80",  OrderStatus.DELIVERED,  "22 min ago"),
        RecentOrder("#1038", "Aloo Paratha + Curd",      "Vikram T.", "₹95",  OrderStatus.DELIVERED,  "35 min ago"),
    )

    // ── Logout dialog ─────────────────────────────────────────────────────────
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest  = { showLogoutDialog = false },
            containerColor    = SurfaceDark,
            title = {
                Text("Sign out?", color = TextWhite, fontWeight = FontWeight.SemiBold)
            },
            text = {
                Text(
                    "You'll need to sign in again to manage your shop.",
                    color = TextMuted, fontSize = 14.sp
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    Firebase.auth.signOut()
                    showLogoutDialog = false
                    onLogout()
                }) {
                    Text("Sign Out", color = RedAccent, fontWeight = FontWeight.SemiBold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancel", color = TextMuted)
                }
            }
        )
    }

    // ── Root layout ───────────────────────────────────────────────────────────
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDark)
            .statusBarsPadding()
            .verticalScroll(scrollState)
    ) {

        // ── Top bar ───────────────────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Good ${greetingByHour()} 👋",
                    fontSize = 13.sp,
                    color = TextMuted
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = shopName,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Avatar / logout
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(OrangePrimary.copy(alpha = 0.18f))
                    .border(1.dp, OrangePrimary.copy(alpha = 0.4f), CircleShape)
                    .clickable { showLogoutDialog = true },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = shopName.take(1).uppercase(),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = OrangePrimary
                )
            }
        }

        // ── Shop open/closed toggle ───────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(SurfaceDark)
                .border(1.dp, BorderColor, RoundedCornerShape(12.dp))
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                PulseDot(color = if (isShopOpen) GreenAccent else RedAccent)
                Column {
                    Text(
                        text = if (isShopOpen) "Shop is Open" else "Shop is Closed",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isShopOpen) GreenAccent else RedAccent
                    )
                    Text(
                        text = if (isShopOpen) "Accepting new orders" else "Not accepting orders",
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                }
            }
            Switch(
                checked = isShopOpen,
                onCheckedChange = { isShopOpen = it },
                colors = SwitchDefaults.colors(
                    checkedThumbColor   = Color.White,
                    checkedTrackColor   = GreenAccent,
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = SurfaceElevated
                )
            )
        }

        Spacer(Modifier.height(24.dp))

        // ── Stat cards (2 × 2 grid) ───────────────────────────────────────────
        SectionLabel("Today's Overview")
        Spacer(Modifier.height(10.dp))
        Column(
            modifier = Modifier.padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCardItem(stats[0], Modifier.weight(1f))
                StatCardItem(stats[1], Modifier.weight(1f))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCardItem(stats[2], Modifier.weight(1f))
                StatCardItem(stats[3], Modifier.weight(1f))
            }
        }

        Spacer(Modifier.height(24.dp))

        // ── Quick actions ─────────────────────────────────────────────────────
        SectionLabel("Quick Actions")
        Spacer(Modifier.height(10.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            QuickAction("📋", "Menu",      Modifier.weight(1f), onClick = onNavigateToMenu)
            QuickAction("🛒", "Orders",    Modifier.weight(1f), onClick = onNavigateToOrders)
            QuickAction("👤", "Profile",   Modifier.weight(1f), onClick = onNavigateToProfile)
            QuickAction("📊", "Analytics", Modifier.weight(1f), onClick = onNavigateToAnalytics)
        }

        Spacer(Modifier.height(24.dp))

        // ── Recent orders ─────────────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SectionLabel("Recent Orders", padded = false)
            Text(
                text = "View all →",
                fontSize = 12.sp,
                color = OrangePrimary,
                modifier = Modifier.clickable(onClick = onNavigateToOrders)
            )
        }

        Spacer(Modifier.height(10.dp))

        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(SurfaceDark)
                .border(1.dp, BorderColor, RoundedCornerShape(16.dp)),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            recentOrders.forEachIndexed { index, order ->
                OrderRow(order)
                if (index < recentOrders.lastIndex) {
                    Divider(color = BorderColor, thickness = 0.5.dp)
                }
            }
        }

        Spacer(Modifier.height(32.dp))
    }
}

// ── Sub-composables ───────────────────────────────────────────────────────────

@Composable
private fun SectionLabel(text: String, padded: Boolean = true) {
    Text(
        text = text,
        fontSize = 13.sp,
        fontWeight = FontWeight.SemiBold,
        color = TextMuted,
        letterSpacing = 0.6.sp,
        modifier = if (padded) Modifier.padding(horizontal = 20.dp) else Modifier
    )
}

@Composable
private fun StatCardItem(stat: StatCard, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(SurfaceDark)
            .border(1.dp, BorderColor, RoundedCornerShape(14.dp))
            .padding(14.dp)
    ) {
        Text(stat.emoji, fontSize = 22.sp)
        Spacer(Modifier.height(8.dp))
        Text(
            text = stat.value,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = stat.color
        )
        Spacer(Modifier.height(2.dp))
        Text(
            text = stat.label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = TextWhite
        )
        Text(
            text = stat.sub,
            fontSize = 10.sp,
            color = TextMuted,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun QuickAction(
    emoji: String,
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceDark)
            .border(1.dp, BorderColor, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(emoji, fontSize = 22.sp, textAlign = TextAlign.Center)
        Spacer(Modifier.height(5.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = TextMuted,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun OrderRow(order: RecentOrder) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left: order id + item summary
        Column(modifier = Modifier.weight(1f)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = order.orderId,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextWhite
                )
                StatusBadge(order.status)
            }
            Spacer(Modifier.height(2.dp))
            Text(
                text = order.itemSummary,
                fontSize = 12.sp,
                color = TextMuted,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "${order.studentName} · ${order.timeAgo}",
                fontSize = 11.sp,
                color = TextMuted.copy(alpha = 0.6f)
            )
        }

        // Right: amount
        Text(
            text = order.amount,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextWhite
        )
    }
}

@Composable
private fun StatusBadge(status: OrderStatus) {
    val (label, color) = when (status) {
        OrderStatus.PENDING   -> "Pending"   to AmberAccent
        OrderStatus.PREPARING -> "Preparing" to OrangeLight
        OrderStatus.READY     -> "Ready"     to GreenAccent
        OrderStatus.DELIVERED -> "Done"      to TextMuted
    }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(color.copy(alpha = 0.15f))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            color = color
        )
    }
}

/** Animated pulsing dot for the open/closed indicator */
@Composable
private fun PulseDot(color: Color) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f, targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dotAlpha"
    )
    Box(
        modifier = Modifier
            .size(10.dp)
            .clip(CircleShape)
            .background(color.copy(alpha = alpha))
    )
}

private fun greetingByHour(): String {
    val hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
    return when {
        hour < 12 -> "morning"
        hour < 17 -> "afternoon"
        else      -> "evening"
    }
}

@Preview(showBackground = true)
@Composable
private fun DashboardPreview() {
    ShopDashboardScreen()
}