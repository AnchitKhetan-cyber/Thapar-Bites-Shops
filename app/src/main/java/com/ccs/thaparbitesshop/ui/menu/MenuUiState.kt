package com.ccs.thaparbitesshop.ui.menu

import com.ccs.thaparbitesshop.data.model.MenuItem

data class MenuUiState(
    val items: List<MenuItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)