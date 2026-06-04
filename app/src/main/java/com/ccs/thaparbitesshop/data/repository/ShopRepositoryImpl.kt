package com.ccs.thaparbitesshop.data.repository

import com.ccs.thaparbitesshop.domain.model.OrderStatus
import com.ccs.thaparbitesshop.model.ShopInfo
import com.ccs.thaparbitesshop.model.ShopStats
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.Calendar
import javax.inject.Inject

class ShopRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : ShopRepository {

    override suspend fun getShopInfo(shopId: String): Result<ShopInfo> {
        return try {
            val doc = firestore
                .collection("shops")
                .document(shopId)
                .get()
                .await()

            val shop = doc.toObject(ShopInfo::class.java) ?: ShopInfo()
            Result.success(shop.copy(id = doc.id))

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateShopStatus(shopId: String, isOpen: Boolean): Result<Unit> {
        return try {
            firestore
                .collection("shops")
                .document(shopId)
                .update("isOpen", isOpen)
                .await()

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Listens to today's orders in real-time and aggregates live stats.
     * Firestore query: shops/{shopId}/orders WHERE placedAt >= startOfDay
     */
    override fun getStatsStream(shopId: String): Flow<ShopStats> = callbackFlow {

        val startOfDay = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time

        val listener = firestore
            .collection("shops")
            .document(shopId)
            .collection("orders")
            .whereGreaterThanOrEqualTo("placedAt", Timestamp(startOfDay))
            .addSnapshotListener { snapshot, error ->

                if (error != null) return@addSnapshotListener

                val docs = snapshot?.documents ?: emptyList()

                val pendingStatuses = setOf(
                    OrderStatus.NEW.name,
                    OrderStatus.PREPARING.name
                )

                val pending = docs.count {
                    (it.getString("status") ?: "") in pendingStatuses
                }

                val revenue = docs
                    .filter { it.getString("status") != OrderStatus.CANCELLED.name }
                    .sumOf { it.getDouble("totalAmount") ?: 0.0 }

                trySend(
                    ShopStats(
                        todayOrders = docs.size,
                        pendingOrders = pending,
                        todayRevenue = revenue
                    )
                )
            }

        awaitClose { listener.remove() }
    }
}