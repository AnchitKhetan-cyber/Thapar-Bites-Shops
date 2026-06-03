package com.ccs.thaparbitesshop.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Three possible states after splash resolves
sealed class AuthState {
    object Loading   : AuthState()
    object LoggedIn  : AuthState()
    object LoggedOut : AuthState()
}

class SplashViewModel : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState

    init {
        checkAuthState()
    }

    private fun checkAuthState() {
        viewModelScope.launch {
            // Minimum splash display time (animations need ~1.8s)
            delay(2500)

            val user = FirebaseAuth.getInstance().currentUser
            _authState.value = if (user != null) {
                AuthState.LoggedIn
            } else {
                AuthState.LoggedOut
            }
        }
    }
}