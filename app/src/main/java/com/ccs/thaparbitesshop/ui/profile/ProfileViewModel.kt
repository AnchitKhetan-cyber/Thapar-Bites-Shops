package com.ccs.thaparbitesshop.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ccs.thaparbitesshop.data.repository.ShopRepository
import com.ccs.thaparbitesshop.di.ShopIdProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val shopRepository: ShopRepository,
    private val shopIdProvider: ShopIdProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            shopRepository.getShopInfo(shopIdProvider.get())
                .onSuccess { info ->
                    _uiState.update {
                        it.copy(
                            shopName = info.name,
                            ownerName = info.ownerName,
                            email = info.ownerEmail,
                            phone = info.ownerPhone,
                            category = info.category,
                            isOpen = info.isOpen
                        )
                    }
                }
        }
    }
}

/*
 NOTE: ShopInfo in Models.kt currently uses:
   description -> ownerName
   imageUrl    -> ownerEmail
   (this is a stopgap; ideally add proper fields to ShopInfo):

   data class ShopInfo(
       ...
       val ownerName: String = "",
       val ownerEmail: String = "",
       val ownerPhone: String = "",
   )

 And update ShopRepositoryImpl.getShopInfo to map those fields.
*/