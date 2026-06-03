package com.ccs.thaparbitesshop.ui.menu

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.ccs.thaparbitesshop.data.model.MenuItem

class MenuViewModel : ViewModel() {


    private var nextId = 100


    private val _uiState = MutableStateFlow(
        MenuUiState(
            items = listOf(
                MenuItem(
                    id = "1",
                    name = "Paneer Burger",
                    description = "Loaded paneer burger",
                    category = "Burger",
                    price = 99.0,
                    isAvailable = true
                ),
                MenuItem(
                    id = "2",
                    name = "Cold Coffee",
                    description = "Creamy cold coffee",
                    category = "Beverage",
                    price = 79.0,
                    isAvailable = true
                ),
                MenuItem(
                    id = "3",
                    name = "Veg Pizza",
                    description = "Cheesy veg pizza",
                    category = "Pizza",
                    price = 199.0,
                    isAvailable = false
                )
            )
        )
    )

    val uiState: StateFlow<MenuUiState> =
        _uiState.asStateFlow()

    fun addItem(
        name: String,
        description: String,
        category: String,
        price: Double
    ) {

        val newItem = MenuItem(
            id = nextId++.toString(),
            name = name,
            description = description,
            category = category,
            price = price,
            isAvailable = true
        )

        _uiState.value = _uiState.value.copy(
            items = _uiState.value.items + newItem
        )
    }

    fun deleteItem(itemId: String) {

        _uiState.value = _uiState.value.copy(
            items = _uiState.value.items.filter {
                it.id != itemId
            }
        )
    }

    fun toggleAvailability(itemId: String) {

        _uiState.value = _uiState.value.copy(
            items = _uiState.value.items.map { item ->

                if (item.id == itemId) {
                    item.copy(
                        isAvailable = !item.isAvailable
                    )
                } else {
                    item
                }
            }
        )
    }

    fun updateItem(updatedItem: MenuItem) {

        _uiState.value = _uiState.value.copy(
            items = _uiState.value.items.map { item ->

                if (item.id == updatedItem.id) {
                    updatedItem
                } else {
                    item
                }
            }
        )
    }
}