package com.ccs.thaparbitesshop.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ccs.thaparbitesshop.data.repository.AuthRepository
import com.ccs.thaparbitesshop.di.ShopIdProvider
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

data class RegisterUiState(
    val isLoading: Boolean = false,
    val registerSuccess: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val firestore: FirebaseFirestore,
    private val shopIdProvider: ShopIdProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun register(
        email: String,
        password: String,
        shopName: String,
        ownerName: String,
        phone: String,
        category: String
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            // 1. Create Firebase Auth user
            authRepository.register(email, password)
                .onFailure { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                    return@launch
                }

            val uid = authRepository.currentUserId() ?: run {
                _uiState.update { it.copy(isLoading = false, error = "Auth failed") }
                return@launch
            }

            // 2. Create shop document in Firestore
            val shopData = mapOf(
                "name" to shopName,
                "ownerName" to ownerName,
                "phone" to phone,
                "email" to email,
                "category" to category,
                "isOpen" to true,
                "ownerId" to uid,
                "rating" to 0.0,
                "totalOrders" to 0,
                "createdAt" to com.google.firebase.Timestamp.now()
            )

            try {
                // Use uid as the shop document id so we can always find it
                firestore
                    .collection("shops")
                    .document(uid)
                    .set(shopData)
                    .await()

                // 3. Store shopId for the rest of the session
                shopIdProvider.set(uid)

                _uiState.update { it.copy(isLoading = false, registerSuccess = true) }

            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}