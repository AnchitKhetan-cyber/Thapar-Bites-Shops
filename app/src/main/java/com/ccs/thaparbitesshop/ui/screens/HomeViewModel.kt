package com.ccs.thaparbitesshop.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ccs.thaparbitesshop.data.repository.OrderRepository
import com.ccs.thaparbitesshop.data.repository.ShopRepository
import com.ccs.thaparbitesshop.di.ShopIdProvider
import com.ccs.thaparbitesshop.domain.model.ShopOrder
import com.ccs.thaparbitesshop.model.ShopInfo
import com.ccs.thaparbitesshop.model.ShopStats
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val shopInfo: ShopInfo = ShopInfo(),
    val stats: ShopStats = ShopStats(),
    val recentOrders: List<ShopOrder> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val shopRepository: ShopRepository,
    private val orderRepository: OrderRepository,
    private val shopIdProvider: ShopIdProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    private fun load() {

        val shopId = shopIdProvider.get()

        android.util.Log.d(
            "SHOP_DEBUG",
            "shopId='$shopId'"
        )

        if (shopId.isBlank()) {

            android.util.Log.e(
                "SHOP_DEBUG",
                "SHOP ID IS EMPTY"
            )

            _uiState.update {
                it.copy(
                    isLoading = false,
                    error = "Shop ID Empty"
                )
            }

            return
        }

        android.util.Log.d(
            "SHOP_DEBUG",
            "Starting Firestore load"
        )

        // existing code
    }

    fun toggleShopOpen() {
        val shopId = shopIdProvider.get()
        val newStatus = !_uiState.value.shopInfo.isOpen

        // Optimistic update
        _uiState.update {
            it.copy(shopInfo = it.shopInfo.copy(isOpen = newStatus))
        }

        viewModelScope.launch {
            shopRepository.updateShopStatus(shopId, newStatus)
                .onFailure { e ->
                    // Revert
                    _uiState.update {
                        it.copy(
                            shopInfo = it.shopInfo.copy(isOpen = !newStatus),
                            error = e.message
                        )
                    }
                }
        }
    }
}