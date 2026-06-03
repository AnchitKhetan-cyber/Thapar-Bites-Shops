package com.ccs.thaparbitesshop.ui.orderdetail

import com.ccs.thaparbitesshop.domain.model.OrderStatus

data class OrderDetailUiState(
    val orderId: String = "",
    val tokenNumber: Int = 0,
    val customerName: String = "",
    val customerPhone: String = "",
    val items: List<OrderItemUi> = emptyList(),
    val total: Double = 0.0,
    val paymentMethod: String = "",
    val note: String = "",
    val status: OrderStatus = OrderStatus.NEW
)

data class OrderItemUi(
    val name: String,
    val quantity: Int,
    val price: Double
)