package com.ccs.thaparbitesshop.data.repository

import com.ccs.thaparbitesshop.domain.model.OrderItemDetail
import com.ccs.thaparbitesshop.domain.model.OrderStatus
import com.ccs.thaparbitesshop.domain.model.ShopOrder
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : OrderRepository {

    // ── Real-time stream ───────────────────────────────────────────────────────

    override fun getOrdersStream(shopId: String): Flow<List<ShopOrder>> =
        callbackFlow {

            val listener = firestore
                .collection("shops")
                .document(shopId)
                .collection("orders")
                .orderBy("placedAt", Query.Direction.DESCENDING)
                .addSnapshotListener { snapshot, error ->

                    if (error != null) {
                        // Don't close the flow; keep listening after transient errors
                        return@addSnapshotListener
                    }

                    val orders = snapshot?.documents?.mapNotNull { doc ->
                        doc.toShopOrder()
                    } ?: emptyList()

                    trySend(orders)
                }

            awaitClose { listener.remove() }
        }

    // ── Single fetch ───────────────────────────────────────────────────────────

    override suspend fun getOrder(orderId: String): Result<ShopOrder> {
        // orderId encodes shopId + "/" + documentId for convenience
        // e.g. "shop_01/abc123"  — or supply shopId separately if you prefer
        return try {
            val parts = orderId.split("/")
            require(parts.size == 2) { "orderId must be 'shopId/docId'" }

            val doc = firestore
                .collection("shops")
                .document(parts[0])
                .collection("orders")
                .document(parts[1])
                .get()
                .await()

            val order = doc.toShopOrder()
                ?: return Result.failure(Exception("Order not found"))

            Result.success(order)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ── Status update ──────────────────────────────────────────────────────────

    override suspend fun updateOrderStatus(
        orderId: String,
        status: OrderStatus
    ): Result<Unit> {
        return try {
            val parts = orderId.split("/")
            require(parts.size == 2) { "orderId must be 'shopId/docId'" }

            firestore
                .collection("shops")
                .document(parts[0])
                .collection("orders")
                .document(parts[1])
                .update("status", status.name)
                .await()

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ── Mapping helper ─────────────────────────────────────────────────────────

    @Suppress("UNCHECKED_CAST")
    private fun com.google.firebase.firestore.DocumentSnapshot.toShopOrder(): ShopOrder? {
        return try {
            val data = data ?: return null

            // items: stored as List<String> e.g. ["2x Chole Bhature"]
            val items = (data["items"] as? List<*>)
                ?.filterIsInstance<String>()
                ?: emptyList()

            // itemDetails: stored as List<Map<String, Any>>
            val itemDetails = (data["itemDetails"] as? List<*>)
                ?.filterIsInstance<Map<String, Any>>()
                ?.map { m ->
                    OrderItemDetail(
                        name = m["name"] as? String ?: "",
                        qty = (m["qty"] as? Long)?.toInt() ?: 0,
                        price = (m["price"] as? Double) ?: 0.0
                    )
                } ?: emptyList()

            ShopOrder(
                id = id,
                tokenNumber = (data["tokenNumber"] as? Long)?.toInt() ?: 0,
                customerName = data["customerName"] as? String ?: "",
                customerPhone = data["customerPhone"] as? String ?: "",
                items = items,
                itemDetails = itemDetails,
                totalAmount = data["totalAmount"] as? Double ?: 0.0,
                status = OrderStatus.fromString(data["status"] as? String ?: ""),
                paymentMethod = data["paymentMethod"] as? String ?: "",
                note = data["note"] as? String ?: "",
                placedAt = data["placedAt"] as? Timestamp
            )
        } catch (e: Exception) {
            null
        }
    }
}

