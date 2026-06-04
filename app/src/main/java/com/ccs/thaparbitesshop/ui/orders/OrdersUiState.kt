package com.ccs.thaparbitesshop.ui.orders

import com.ccs.thaparbitesshop.domain.model.ShopOrder

data class OrdersUiState(
    val orders: List<ShopOrder> = emptyList(),
    val selectedFilter: String = "ALL",
    val isLoading: Boolean = false,
    val error: String? = null
)