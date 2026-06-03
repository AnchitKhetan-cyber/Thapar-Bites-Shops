package com.ccs.thaparbitesshop.data.model

data class MenuItem(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val category: String = "",
    val price: Double = 0.0,
    val imageUrl: String = "",
    val isAvailable: Boolean = true
)