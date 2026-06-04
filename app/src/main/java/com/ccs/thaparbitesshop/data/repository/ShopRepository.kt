package com.ccs.thaparbitesshop.data.repository

import com.ccs.thaparbitesshop.model.ShopInfo
import com.ccs.thaparbitesshop.model.ShopStats
import kotlinx.coroutines.flow.Flow

interface ShopRepository {

    suspend fun getShopInfo(shopId: String): Result<ShopInfo>

    suspend fun updateShopStatus(shopId: String, isOpen: Boolean): Result<Unit>

    /** Real-time stream of today's stats (orders count, pending, revenue) */
    fun getStatsStream(shopId: String): Flow<ShopStats>
}