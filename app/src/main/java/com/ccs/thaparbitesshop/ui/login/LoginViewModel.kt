package com.ccs.thaparbitesshop.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ccs.thaparbitesshop.data.repository.AuthRepository
import com.ccs.thaparbitesshop.di.ShopIdProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val shopIdProvider: ShopIdProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    fun updateEmail(value: String) = _uiState.update { it.copy(email = value) }
    fun updatePassword(value: String) = _uiState.update { it.copy(password = value) }

    fun login() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            authRepository.login(_uiState.value.email, _uiState.value.password)
                .onSuccess {
                    // UID == shopId (set during registration)
                    val uid = authRepository.currentUserId() ?: ""
                    shopIdProvider.set(uid)
                    _uiState.update { it.copy(isLoading = false, loginSuccess = true) }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false, error = error.message) }
                }
        }
    }

    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            authRepository.signInWithGoogle(idToken)
                .onSuccess {
                    val uid = authRepository.currentUserId() ?: ""
                    shopIdProvider.set(uid)
                    _uiState.update { it.copy(loginSuccess = true) }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(error = error.message) }
                }
        }
    }

    fun logout() {
        authRepository.logout()
        shopIdProvider.set("")
    }
}