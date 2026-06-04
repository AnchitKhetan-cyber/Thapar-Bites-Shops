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

data class ProfileUiState(
    val shopName: String = "",
    val ownerName: String = "",
    val email: String = "",
    val phone: String = "",
    val category: String = "",
    val isOpen: Boolean = true
)



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