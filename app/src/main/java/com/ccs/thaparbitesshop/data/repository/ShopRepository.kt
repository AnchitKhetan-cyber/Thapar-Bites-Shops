package com.ccs.thaparbitesshop.data.repository

import com.ccs.thaparbitesshop.model.ShopInfo

interface ShopRepository {

    suspend fun getShopInfo(
        shopId: String
    ): Result<ShopInfo>

    suspend fun updateShopStatus(
        shopId: String,
        isOpen: Boolean
    ): Result<Unit>
}