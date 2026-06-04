package com.ccs.thaparbitesshop.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ccs.thaparbitesshop.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(LoginUiState())

    val uiState =
        _uiState.asStateFlow()

    fun updateEmail(
        value: String
    ) {
        _uiState.update {
            it.copy(email = value)
        }
    }

    fun updatePassword(
        value: String
    ) {
        _uiState.update {
            it.copy(password = value)
        }
    }

    fun login() {

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isLoading = true,
                    error = null
                )
            }

            authRepository.login(
                _uiState.value.email,
                _uiState.value.password
            )
                .onSuccess {

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            loginSuccess = true
                        )
                    }
                }
                .onFailure { error ->

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = error.message
                        )
                    }
                }
        }
    }

    fun signInWithGoogle(
        idToken: String
    ) {

        viewModelScope.launch {

            authRepository
                .signInWithGoogle(
                    idToken
                )
                .onSuccess {

                    _uiState.update {
                        it.copy(
                            loginSuccess = true
                        )
                    }
                }
                .onFailure { error ->

                    _uiState.update {
                        it.copy(
                            error = error.message
                        )
                    }
                }
        }
    }

    fun logout() {
        FirebaseAuth.getInstance().signOut()
    }
}