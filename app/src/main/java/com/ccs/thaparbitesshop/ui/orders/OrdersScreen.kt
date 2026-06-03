package com.ccs.thaparbitesshop.ui.orders

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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

    Column {

        FilterChips(
            selected = state.selectedFilter,
            onSelected = viewModel::selectFilter
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding =
                PaddingValues(12.dp),
            verticalArrangement =
                Arrangement.spacedBy(12.dp)
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