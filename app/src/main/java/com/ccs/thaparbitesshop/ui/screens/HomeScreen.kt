package com.ccs.thaparbitesshop.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ccs.thaparbitesshop.domain.model.ShopOrder
import com.ccs.thaparbitesshop.domain.model.OrderStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToOrders: () -> Unit,
    onNavigateToMenu: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onOrderClick: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    val primaryColor = Color(0xFF7A1F3D)
    val secondaryColor = Color(0xFF4A1025)
    val accentColor = Color(0xFFC9A227)

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {},
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { innerPadding ->

        if (state.isLoading) {
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = primaryColor)
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // ── Header card ──────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(primaryColor, secondaryColor)
                        )
                    )
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Good morning! 👋",
                            fontSize = 13.sp,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                        Text(
                            text = state.shopInfo.name,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color.White.copy(alpha = 0.2f))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (state.shopInfo.isOpen) Color(0xFF4CAF50)
                                        else Color(0xFFFF5252)
                                    )
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (state.shopInfo.isOpen) "Shop Open" else "Shop Closed",
                                fontSize = 13.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = if (state.shopInfo.isOpen) "Open" else "Closed",
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                        Switch(
                            checked = state.shopInfo.isOpen,
                            onCheckedChange = { viewModel.toggleShopOpen() },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = Color(0xFF4CAF50),
                                uncheckedThumbColor = Color.White,
                                uncheckedTrackColor = Color(0xFFFF5252)
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ── Today's Stats ────────────────────────────────────────────────
            Text(
                text = "Today's Overview",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    title = "Orders",
                    value = state.stats.todayOrders.toString(),
                    icon = Icons.Default.ShoppingBag,
                    iconBg = Color(0xFFFFECE5),
                    iconTint = primaryColor
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    title = "Pending",
                    value = state.stats.pendingOrders.toString(),
                    icon = Icons.Default.HourglassEmpty,
                    iconBg = Color(0xFFFFF8E1),
                    iconTint = Color(0xFFFFA000)
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    title = "Revenue",
                    value = "₹${state.stats.todayRevenue.toInt()}",
                    icon = Icons.Default.CurrencyRupee,
                    iconBg = Color(0xFFE8F5E9),
                    iconTint = Color(0xFF388E3C)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ── Quick Actions ────────────────────────────────────────────────
            Text(
                text = "Quick Actions",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickActionCard(
                    modifier = Modifier.weight(1f),
                    title = "New Orders",
                    subtitle = "${state.stats.pendingOrders} pending",
                    icon = Icons.Default.Notifications,
                    color = primaryColor,
                    onClick = onNavigateToOrders
                )
                QuickActionCard(
                    modifier = Modifier.weight(1f),
                    title = "Manage Menu",
                    subtitle = "${state.stats.menuItemCount} items",
                    icon = Icons.Default.MenuBook,
                    color = accentColor,
                    onClick = onNavigateToMenu
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickActionCard(
                    modifier = Modifier.weight(1f),
                    title = "Order History",
                    subtitle = "View all",
                    icon = Icons.Default.History,
                    color = secondaryColor,
                    onClick = onNavigateToOrders
                )
                QuickActionCard(
                    modifier = Modifier.weight(1f),
                    title = "My Profile",
                    subtitle = "Shop settings",
                    icon = Icons.Default.Store,
                    color = primaryColor.copy(alpha = 0.8f),
                    onClick = onNavigateToProfile
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ── Recent Orders ────────────────────────────────────────────────
            Text(
                text = "Recent Orders",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Column(
                modifier = Modifier.padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (state.recentOrders.isEmpty()) {
                    Text(
                        text = "No orders yet today",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                } else {
                    state.recentOrders.forEach { order ->
                        RecentOrderRow(
                            orderId = "#${order.tokenNumber}",
                            customerName = order.customerName.ifBlank { "Customer" },
                            status = order.status.label,
                            onClick = { onOrderClick(order.id) }
                        )
                    }

                    TextButton(
                        onClick = onNavigateToOrders,
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text(
                            text = "View All Orders →",
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// ── Shared composables (unchanged from original) ──────────────────────────────

@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    icon: ImageVector,
    iconBg: Color,
    iconTint: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp), horizontalAlignment = Alignment.Start) {
            Box(
                modifier = Modifier.size(36.dp).clip(RoundedCornerShape(10.dp)).background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = value, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            Text(text = title, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun QuickActionCard(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(40.dp).clip(RoundedCornerShape(12.dp)).background(color.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(22.dp))
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                Text(text = subtitle, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun RecentOrderRow(orderId: String, customerName: String, status: String, onClick: () -> Unit) {
    val statusColor = when (status) {
        "New" -> Color(0xFFFFA000)
        "Preparing" -> Color(0xFF5C6BC0)
        "Ready" -> Color(0xFF26A69A)
        "Completed" -> Color(0xFF4CAF50)
        "Cancelled" -> Color(0xFFEF5350)
        else -> Color(0xFF888888)
    }
    val primaryColor = Color(0xFF7A1F3D)

    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(38.dp).clip(CircleShape).background(primaryColor.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Receipt, contentDescription = null, tint = primaryColor, modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(text = orderId, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                    Text(text = customerName, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Surface(shape = RoundedCornerShape(20.dp), color = statusColor.copy(alpha = 0.12f)) {
                Text(text = status, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp), fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = statusColor)
            }
        }
    }
}