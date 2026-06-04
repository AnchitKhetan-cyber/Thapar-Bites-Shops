package com.ccs.thaparbitesshop.ui.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ccs.thaparbitesshop.data.repository.OrderRepository
import com.ccs.thaparbitesshop.di.ShopIdProvider
import com.ccs.thaparbitesshop.domain.model.ShopOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    private val shopIdProvider: ShopIdProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrdersUiState())
    val uiState: StateFlow<OrdersUiState> = _uiState.asStateFlow()

    init {
        loadOrders()
    }

    private fun loadOrders() {
        val shopId = shopIdProvider.get()
        if (shopId.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            orderRepository
                .getOrdersStream(shopId)
                .catch { e ->
                    _uiState.update {
                        it.copy(isLoading = false, error = e.message)
                    }
                }
                .collect { orders ->
                    _uiState.update {
                        it.copy(
                            orders = orders,
                            isLoading = false,
                            error = null
                        )
                    }
                }
        }
    }

    fun selectFilter(filter: String) {
        _uiState.update { it.copy(selectedFilter = filter) }
    }

    /** Filtered view used by the screen */
    fun filteredOrders(): List<ShopOrder> {
        val state = _uiState.value
        if (state.selectedFilter == "ALL") return state.orders
        return state.orders.filter { it.status.name == state.selectedFilter }
    }
}