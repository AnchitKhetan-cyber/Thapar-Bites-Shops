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
        if (shopId.isBlank()) return

        // Fetch shop info once
        viewModelScope.launch {
            shopRepository.getShopInfo(shopId)
                .onSuccess { info ->
                    _uiState.update { it.copy(shopInfo = info, isLoading = false) }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }
        }

        // Live stats stream
        viewModelScope.launch {
            shopRepository.getStatsStream(shopId)
                .catch { e -> _uiState.update { it.copy(error = e.message) } }
                .collect { stats ->
                    _uiState.update { it.copy(stats = stats) }
                }
        }

        // Recent orders (latest 5)
        viewModelScope.launch {
            orderRepository.getOrdersStream(shopId)
                .catch { e -> _uiState.update { it.copy(error = e.message) } }
                .collect { orders ->
                    _uiState.update { it.copy(recentOrders = orders.take(5)) }
                }
        }
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