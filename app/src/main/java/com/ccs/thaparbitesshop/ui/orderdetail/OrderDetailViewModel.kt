package com.ccs.thaparbitesshop.ui.orderdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ccs.thaparbitesshop.data.repository.OrderRepository
import com.ccs.thaparbitesshop.di.ShopIdProvider
import com.ccs.thaparbitesshop.domain.model.OrderStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val orderRepository: OrderRepository,
    private val shopIdProvider: ShopIdProvider
) : ViewModel() {

    // orderId passed via nav argument — just the Firestore document id (not shopId/docId)
    private val docId: String = savedStateHandle["orderId"] ?: ""

    private val _uiState = MutableStateFlow(
        OrderDetailUiState(orderId = docId, isLoading = true)
    )
    val uiState: StateFlow<OrderDetailUiState> = _uiState.asStateFlow()

    init {
        loadOrder()
    }

    private fun loadOrder() {
        viewModelScope.launch {
            val shopId = shopIdProvider.get()
            // Compose the compound key expected by OrderRepositoryImpl
            val compoundId = "$shopId/$docId"

            orderRepository.getOrder(compoundId)
                .onSuccess { order ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            orderId = order.id,
                            tokenNumber = order.tokenNumber,
                            customerName = order.customerName,
                            customerPhone = order.customerPhone,
                            paymentMethod = order.paymentMethod,
                            note = order.note,
                            total = order.totalAmount,
                            status = order.status,
                            items = order.itemDetails.map { d ->
                                OrderItemUi(d.name, d.qty, d.price)
                            }.ifEmpty {
                                // Fallback: parse from string list if itemDetails is empty
                                order.items.map { s -> OrderItemUi(s, 1, 0.0) }
                            }
                        )
                    }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(isLoading = false, error = e.message)
                    }
                }
        }
    }

    fun updateStatus(status: OrderStatus) {
        val shopId = shopIdProvider.get()
        val compoundId = "$shopId/$docId"

        viewModelScope.launch {
            orderRepository.updateOrderStatus(compoundId, status)
                .onSuccess {
                    _uiState.update { it.copy(status = status) }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(error = e.message) }
                }
        }
    }
}