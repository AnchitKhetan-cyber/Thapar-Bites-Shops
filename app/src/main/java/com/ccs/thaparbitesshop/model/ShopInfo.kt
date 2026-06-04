package com.ccs.thaparbitesshop.model

/**
 * Firestore document at shops/{shopId}
 *
 * All fields must have default values so Firestore's toObject() works.
 */
data class ShopInfo(
    val id: String = "",
    val name: String = "",
    val ownerName: String = "",
    val ownerEmail: String = "",
    val ownerPhone: String = "",
    val category: String = "",
    val imageUrl: String = "",
    val isOpen: Boolean = true,
    val rating: Float = 0f,
    val totalOrders: Int = 0,
    // legacy field kept so existing Firestore documents still deserialise
    val description: String = ""
)