package com.ccs.thaparbitesshop.ui.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ccs.thaparbitesshop.data.model.MenuItem
import com.ccs.thaparbitesshop.data.repository.MenuRepository
import com.ccs.thaparbitesshop.di.ShopIdProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val menuRepository: MenuRepository,
    private val shopIdProvider: ShopIdProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(MenuUiState(isLoading = true))
    val uiState: StateFlow<MenuUiState> = _uiState.asStateFlow()

    init {
        observeMenu()
    }

    private fun observeMenu() {

        val shopId = shopIdProvider.get()

        android.util.Log.d(
            "MENU_DEBUG",
            "Loading menu for shopId='$shopId'"
        )

        if (shopId.isBlank()) {
            android.util.Log.e(
                "MENU_DEBUG",
                "Shop ID is blank"
            )
            return
        }

        viewModelScope.launch {

            menuRepository
                .getMenuStream(shopId)
                .catch { e ->

                    android.util.Log.e(
                        "MENU_DEBUG",
                        "Menu stream failed",
                        e
                    )

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = e.message
                        )
                    }
                }
                .collect { items ->

                    android.util.Log.d(
                        "MENU_DEBUG",
                        "Received ${items.size} menu items"
                    )

                    _uiState.update {
                        it.copy(
                            items = items,
                            isLoading = false,
                            error = null
                        )
                    }
                }
        }
    }

    fun addItem(
        name: String,
        description: String,
        category: String,
        price: Double
    ) {
        val shopId = shopIdProvider.get()

        android.util.Log.d(
            "MENU_DEBUG",
            "shopId = '$shopId'"
        )

        val newItem = MenuItem(
            name = name,
            description = description,
            category = category,
            price = price,
            isAvailable = true
        )

        viewModelScope.launch {

            menuRepository.addItem(shopId, newItem)
                .onSuccess {
                    android.util.Log.d(
                        "MENU_DEBUG",
                        "Item added successfully"
                    )
                }
                .onFailure {
                    android.util.Log.e(
                        "MENU_DEBUG",
                        "Failed to add item",
                        it
                    )
                }
        }
    }

    fun deleteItem(itemId: String) {
        viewModelScope.launch {
            menuRepository.deleteItem(shopIdProvider.get(), itemId)
                .onFailure { e ->
                    _uiState.update { it.copy(error = e.message) }
                }
        }
    }

    fun toggleAvailability(itemId: String) {
        val current = _uiState.value.items.find { it.id == itemId } ?: return

        viewModelScope.launch {
            menuRepository.toggleAvailability(
                shopId = shopIdProvider.get(),
                itemId = itemId,
                isAvailable = !current.isAvailable
            ).onFailure { e ->
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }
}