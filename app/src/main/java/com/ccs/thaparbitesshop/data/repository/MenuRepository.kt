package com.ccs.thaparbitesshop.data.repository

import com.ccs.thaparbitesshop.data.model.MenuItem
import kotlinx.coroutines.flow.Flow

interface MenuRepository {

    /** Real-time stream of all menu items for this shop */
    fun getMenuStream(shopId: String): Flow<List<MenuItem>>

    /** Add a new menu item; returns the generated document id */
    suspend fun addItem(shopId: String, item: MenuItem): Result<String>

    /** Toggle isAvailable flag */
    suspend fun toggleAvailability(
        shopId: String,
        itemId: String,
        isAvailable: Boolean
    ): Result<Unit>

    /** Delete a menu item */
    suspend fun deleteItem(shopId: String, itemId: String): Result<Unit>
}