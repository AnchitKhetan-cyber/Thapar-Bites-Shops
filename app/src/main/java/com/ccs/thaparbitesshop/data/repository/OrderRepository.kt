package com.ccs.thaparbitesshop.data.repository

import com.ccs.thaparbitesshop.domain.model.ShopOrder
import com.ccs.thaparbitesshop.domain.model.OrderStatus
import kotlinx.coroutines.flow.Flow

interface OrderRepository {

    /** Real-time stream of all orders for this shop */
    fun getOrdersStream(shopId: String): Flow<List<ShopOrder>>

    /** Fetch a single order by id */
    suspend fun getOrder(orderId: String): Result<ShopOrder>

    /** Update the status of an order */
    suspend fun updateOrderStatus(
        orderId: String,
        status: OrderStatus
    ): Result<Unit>
}