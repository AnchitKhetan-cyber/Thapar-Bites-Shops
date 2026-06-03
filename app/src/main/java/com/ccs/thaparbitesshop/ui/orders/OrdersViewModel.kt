package com.ccs.thaparbitesshop.ui.orders

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.ccs.thaparbitesshop.domain.model.*

class OrdersViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        OrdersUiState(
            orders = sampleOrders()
        )
    )

    val uiState: StateFlow<OrdersUiState> =
        _uiState.asStateFlow()

    fun selectFilter(filter: String) {
        _uiState.value =
            _uiState.value.copy(
                selectedFilter = filter
            )
    }

    companion object {

        private fun sampleOrders() = listOf(
            ShopOrder(
                id = "1042",
                tokenNumber = 42,
                items = listOf(
                    "2x Chole Bhature",
                    "1x Chai",
                    "1x Lassi"
                ),
                totalAmount = 185.0,
                status = OrderStatus.NEW,
                timeAgo = "2 min ago"
            ),
            ShopOrder(
                id = "1041",
                tokenNumber = 41,
                items = listOf(
                    "1x Veg Burger",
                    "2x Cold Coffee"
                ),
                totalAmount = 210.0,
                status = OrderStatus.PREPARING,
                timeAgo = "5 min ago"
            )
        )
    }
}