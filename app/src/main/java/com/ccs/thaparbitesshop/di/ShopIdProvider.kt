package com.ccs.thaparbitesshop.di

import javax.inject.Inject
import javax.inject.Singleton

/**
 * Holds the Firestore document ID of the currently logged-in shop.
 *
 * After login / registration, call [set] with the shop's document id.
 * All ViewModels inject this to know which shop's data to load.
 *
 * The shop id is stored in the "shops" collection in Firestore.
 * During registration you create the document and save its id here
 * (and optionally persist it in SharedPreferences / DataStore for restarts).
 */
@Singleton
class ShopIdProvider @Inject constructor() {

    /** Volatile so cross-thread reads always see the latest value. */
    @Volatile
    private var shopId: String = ""

    fun set(id: String) {
        shopId = id
    }

    fun get(): String = shopId

    fun isSet(): Boolean = shopId.isNotBlank()
}