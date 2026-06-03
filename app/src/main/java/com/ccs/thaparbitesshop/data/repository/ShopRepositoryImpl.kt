package com.ccs.thaparbitesshop.data.repository

import com.ccs.thaparbitesshop.model.ShopInfo
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ShopRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : ShopRepository {

    override suspend fun getShopInfo(
        shopId: String
    ): Result<ShopInfo> {

        return try {

            val doc =
                firestore
                    .collection("shops")
                    .document(shopId)
                    .get()
                    .await()

            val shop =
                doc.toObject(
                    ShopInfo::class.java
                ) ?: ShopInfo()

            Result.success(shop)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    override suspend fun updateShopStatus(
        shopId: String,
        isOpen: Boolean
    ): Result<Unit> {

        return try {

            firestore
                .collection("shops")
                .document(shopId)
                .update(
                    "isOpen",
                    isOpen
                )
                .await()

            Result.success(Unit)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }
}