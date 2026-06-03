package com.ccs.thaparbitesshop.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ccs.thaparbitesshop.data.repository.AuthRepository
import com.ccs.thaparbitesshop.data.repository.AuthResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// ── UI State ──────────────────────────────────────────────────────────────────

data class LoginUiState(
    val email: String              = "",
    val password: String           = "",
    val isLoading: Boolean         = false,
    val isPasswordVisible: Boolean = false,
    val emailError: String?        = null,
    val passwordError: String?     = null,
    val loginError: String?        = null,   // Firebase / network errors shown in UI
    val isLoginSuccess: Boolean    = false,
    // Forgot-password sub-flow
    val forgotEmail: String        = "",
    val isSendingReset: Boolean    = false,
    val resetEmailSent: Boolean    = false,
    val resetError: String?        = null
)

// ── ViewModel ─────────────────────────────────────────────────────────────────

/**
 * LoginViewModel
 *
 * All login-screen state lives here. Delegates Firebase calls to
 * [AuthRepository] so the screen stays testable.
 *
 * Wiring in Compose:
 *   val vm: LoginViewModel = viewModel(
 *       factory = ShopViewModelFactory(AuthRepository())
 *   )
 *   val state by vm.uiState.collectAsStateWithLifecycle()
 */
class LoginViewModel(
    private val repo: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    // ── Field updates ─────────────────────────────────────────────────────────

    fun onEmailChange(value: String) = _uiState.update {
        it.copy(email = value, emailError = null, loginError = null)
    }

    fun onPasswordChange(value: String) = _uiState.update {
        it.copy(password = value, passwordError = null, loginError = null)
    }

    fun onTogglePasswordVisibility() = _uiState.update {
        it.copy(isPasswordVisible = !it.isPasswordVisible)
    }

    fun onForgotEmailChange(value: String) = _uiState.update {
        it.copy(forgotEmail = value, resetError = null)
    }

    // ── Validation ────────────────────────────────────────────────────────────

    private fun validate(): Boolean {
        val s = _uiState.value

        val emailErr = when {
            s.email.isBlank()                                  -> "Email is required."
            !s.email.contains('@') || !s.email.contains('.') -> "Enter a valid email address."
            else                                               -> null
        }
        val passErr = when {
            s.password.isBlank()   -> "Password is required."
            s.password.length < 6 -> "Password must be at least 6 characters."
            else                   -> null
        }

        _uiState.update { it.copy(emailError = emailErr, passwordError = passErr) }
        return emailErr == null && passErr == null
    }

    // ── Login ─────────────────────────────────────────────────────────────────

    fun login() {
        if (!validate()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, loginError = null) }

            when (val result = repo.signIn(
                email    = _uiState.value.email.trim(),
                password = _uiState.value.password
            )) {
                is AuthResult.Success -> _uiState.update {
                    it.copy(isLoading = false, isLoginSuccess = true)
                }
                is AuthResult.Error   -> _uiState.update {
                    it.copy(isLoading = false, loginError = result.message)
                }
            }
        }
    }

    /** Call after the NavController has navigated away — resets the success flag. */
    fun onLoginNavigated() = _uiState.update { it.copy(isLoginSuccess = false) }

    // ── Forgot password ───────────────────────────────────────────────────────

    fun sendPasswordReset() {
        val email = _uiState.value.forgotEmail.trim()
        if (email.isBlank() || !email.contains('@')) {
            _uiState.update { it.copy(resetError = "Enter a valid email address.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSendingReset = true, resetError = null) }

            when (val result = repo.sendPasswordReset(email)) {
                is AuthResult.Success -> _uiState.update {
                    it.copy(isSendingReset = false, resetEmailSent = true)
                }
                is AuthResult.Error   -> _uiState.update {
                    it.copy(isSendingReset = false, resetError = result.message)
                }
            }
        }
    }

    fun onResetEmailDismissed() = _uiState.update {
        it.copy(resetEmailSent = false, forgotEmail = "", resetError = null)
    }
}