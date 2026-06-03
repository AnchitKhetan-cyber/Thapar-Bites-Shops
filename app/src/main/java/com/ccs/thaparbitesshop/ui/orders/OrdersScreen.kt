package com.ccs.thaparbitesshop.ui.orders

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ccs.thaparbitesshop.ui.orders.components.OrderCard



@Composable
fun OrdersScreen(
    viewModel: OrdersViewModel,
    onOrderClick: (String) -> Unit
) {

    val state by viewModel
        .uiState
        .collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        OrdersHeader()

        FilterChips(
            selected = state.selectedFilter,
            onSelected = viewModel::selectFilter
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            items(state.orders) { order ->

                OrderCard(
                    order = order,
                    onClick = {
                        onOrderClick(order.id)
                    }
                )
            }
        }
    }
}

@Composable
private fun OrdersHeader() {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFF6B35)
        ),
        shape = RoundedCornerShape(24.dp)
    ) {

        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            Text(
                text = "Live Orders",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "Manage incoming orders quickly",
                color = Color.White.copy(alpha = 0.85f)
            )
        }
    }
}