package com.ccs.thaparbitesshop.domain.model

enum class OrderStatus(val label: String) {
    NEW("New"),
    PREPARING("Preparing"),
    READY("Ready"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled");

    companion object {
        fun fromString(value: String): OrderStatus =
            entries.firstOrNull { it.name == value } ?: NEW
    }
}