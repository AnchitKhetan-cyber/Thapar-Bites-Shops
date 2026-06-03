package com.ccs.thaparbitesshop.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ccs.thaparbitesshop.data.repository.AuthRepository

class ShopViewModelFactory(
    private val authRepository: AuthRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return when (modelClass) {

            LoginViewModel::class.java ->
                LoginViewModel(authRepository) as T

            else ->
                throw IllegalArgumentException(
                    "Unknown ViewModel class: ${modelClass.name}"
                )
        }
    }
}