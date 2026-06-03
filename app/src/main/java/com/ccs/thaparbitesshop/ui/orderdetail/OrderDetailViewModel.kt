package com.ccs.thaparbitesshop.ui.orderdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.ccs.thaparbitesshop.domain.model.OrderStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class OrderDetailViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val orderId =
        savedStateHandle.get<String>("orderId") ?: ""

    private val _uiState =
        MutableStateFlow(
            OrderDetailUiState(
                orderId = orderId,
                tokenNumber = 42,
                customerName = "Priya Sharma",
                customerPhone = "9876543210",
                paymentMethod = "UPI",
                note = "Less spicy please",
                total = 275.0,
                status = OrderStatus.NEW,
                items = listOf(
                    OrderItemUi(
                        "Chole Bhature",
                        2,
                        180.0
                    ),
                    OrderItemUi(
                        "Masala Chai",
                        1,
                        20.0
                    ),
                    OrderItemUi(
                        "Lassi",
                        1,
                        75.0
                    )
                )
            )
        )

    val uiState: StateFlow<OrderDetailUiState> =
        _uiState.asStateFlow()

    fun updateStatus(
        status: OrderStatus
    ) {
        _uiState.value =
            _uiState.value.copy(
                status = status
            )
    }
}