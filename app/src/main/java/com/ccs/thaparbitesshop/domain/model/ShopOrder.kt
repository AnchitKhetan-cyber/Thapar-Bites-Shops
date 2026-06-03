package com.ccs.thaparbitesshop.domain.model

data class ShopOrder(
    val id: String,
    val tokenNumber: Int,
    val items: List<String>,
    val totalAmount: Double,
    val status: OrderStatus,
    val timeAgo: String
)