package com.ccs.thaparbitesshop.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ccs.thaparbitesshop.data.model.ShopCategory
import com.ccs.thaparbitesshop.data.model.ShopOwner
import com.ccs.thaparbitesshop.data.repository.AdminRepository
import com.ccs.thaparbitesshop.data.repository.AuthResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// ── UI State ──────────────────────────────────────────────────────────────────

data class CreateShopFormState(
    // Form fields
    val ownerName: String       = "",
    val email: String           = "",
    val password: String        = "",
    val confirmPassword: String = "",
    val shopName: String        = "",
    val location: String        = "",
    val category: ShopCategory = ShopCategory.FOOD,
    val isPasswordVisible: Boolean      = false,
    val isConfirmPasswordVisible: Boolean = false,

    // Field-level errors
    val ownerNameError: String?     = null,
    val emailError: String?         = null,
    val passwordError: String?      = null,
    val confirmPasswordError: String? = null,
    val shopNameError: String?      = null,
    val locationError: String?      = null,

    // Async state
    val isLoading: Boolean          = false,
    val submitError: String?        = null,
    val isSuccess: Boolean          = false,
    val createdShop: ShopOwner?     = null
)

data class AdminDashboardState(
    val shops: List<ShopOwner>  = emptyList(),
    val isLoading: Boolean      = false,
    val error: String?          = null,
    val isAdmin: Boolean        = false
)

// ── ViewModel ─────────────────────────────────────────────────────────────────

class AdminViewModel(
    private val repo: AdminRepository,
    // Admin credentials needed to re-sign-in after creating a shop owner account.
    // Passed in from the login screen and held only in memory.
    private val adminEmail: String,
    private val adminPassword: String
) : ViewModel() {

    private val _formState = MutableStateFlow(CreateShopFormState())
    val formState: StateFlow<CreateShopFormState> = _formState.asStateFlow()

    private val _dashState = MutableStateFlow(AdminDashboardState())
    val dashState: StateFlow<AdminDashboardState> = _dashState.asStateFlow()

    init {
        checkAdminAndLoadShops()
    }

    // ── Init ──────────────────────────────────────────────────────────────────

    private fun checkAdminAndLoadShops() {
        viewModelScope.launch {
            val isAdmin = repo.isCurrentUserAdmin()
            _dashState.update { it.copy(isAdmin = isAdmin) }
            if (isAdmin) loadShops()
        }
    }

    fun loadShops() {
        viewModelScope.launch {
            _dashState.update { it.copy(isLoading = true, error = null) }
            when (val result = repo.getAllShops()) {
                is AuthResult.Success -> _dashState.update {
                    it.copy(isLoading = false, shops = result.data)
                }
                is AuthResult.Error -> _dashState.update {
                    it.copy(isLoading = false, error = result.message)
                }
            }
        }
    }

    // ── Form field updates ────────────────────────────────────────────────────

    fun onOwnerNameChange(v: String)    = _formState.update { it.copy(ownerName = v, ownerNameError = null, submitError = null) }
    fun onEmailChange(v: String)        = _formState.update { it.copy(email = v, emailError = null, submitError = null) }
    fun onPasswordChange(v: String)     = _formState.update { it.copy(password = v, passwordError = null, submitError = null) }
    fun onConfirmPasswordChange(v: String) = _formState.update { it.copy(confirmPassword = v, confirmPasswordError = null) }
    fun onShopNameChange(v: String)     = _formState.update { it.copy(shopName = v, shopNameError = null, submitError = null) }
    fun onLocationChange(v: String)     = _formState.update { it.copy(location = v, locationError = null, submitError = null) }
    fun onCategoryChange(v: ShopCategory) = _formState.update { it.copy(category = v) }
    fun onTogglePassword()              = _formState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    fun onToggleConfirmPassword()       = _formState.update { it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible) }

    // ── Validation ────────────────────────────────────────────────────────────

    private fun validate(): Boolean {
        val s = _formState.value
        val ownerNameErr    = if (s.ownerName.isBlank())   "Owner name is required." else null
        val emailErr        = when {
            s.email.isBlank()                                  -> "Email is required."
            !s.email.contains('@') || !s.email.contains('.') -> "Enter a valid email."
            else                                               -> null
        }
        val passwordErr     = when {
            s.password.isBlank()   -> "Password is required."
            s.password.length < 6 -> "Minimum 6 characters."
            else                   -> null
        }
        val confirmPassErr  = when {
            s.confirmPassword.isBlank()        -> "Please confirm the password."
            s.confirmPassword != s.password    -> "Passwords do not match."
            else                               -> null
        }
        val shopNameErr     = if (s.shopName.isBlank())    "Shop name is required." else null
        val locationErr     = if (s.location.isBlank())    "Location is required."  else null

        _formState.update {
            it.copy(
                ownerNameError        = ownerNameErr,
                emailError            = emailErr,
                passwordError         = passwordErr,
                confirmPasswordError  = confirmPassErr,
                shopNameError         = shopNameErr,
                locationError         = locationErr
            )
        }
        return listOf(ownerNameErr, emailErr, passwordErr, confirmPassErr,
            shopNameErr, locationErr).all { it == null }
    }

    // ── Submit ────────────────────────────────────────────────────────────────

    fun createShopOwner() {
        if (!validate()) return

        val s = _formState.value
        val newShop = ShopOwner(
            ownerName = s.ownerName.trim(),
            email     = s.email.trim(),
            shopName  = s.shopName.trim(),
            location  = s.location.trim(),
            category  = s.category.name
        )

        viewModelScope.launch {
            _formState.update { it.copy(isLoading = true, submitError = null) }

            when (val result = repo.createShopOwner(
                shopOwner  = newShop,
                password   = s.password,
                adminEmail = adminEmail,
                adminPass  = adminPassword
            )) {
                is AuthResult.Success -> {
                    _formState.update {
                        it.copy(isLoading = false, isSuccess = true, createdShop = result.data)
                    }
                    loadShops() // refresh list
                }
                is AuthResult.Error -> _formState.update {
                    it.copy(isLoading = false, submitError = result.message)
                }
            }
        }
    }

    fun onSuccessDismissed() {
        _formState.update { CreateShopFormState() } // reset form
    }

    // ── Shop actions ──────────────────────────────────────────────────────────

    fun toggleShopActive(shopUid: String, currentlyActive: Boolean) {
        viewModelScope.launch {
            repo.setShopActive(shopUid, !currentlyActive)
            loadShops()
        }
    }

    fun deleteShop(shopUid: String) {
        viewModelScope.launch {
            repo.deleteShop(shopUid)
            loadShops()
        }
    }
}

// ── Factory ───────────────────────────────────────────────────────────────────

class AdminViewModelFactory(
    private val adminEmail: String,
    private val adminPassword: String
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AdminViewModel::class.java))
            return AdminViewModel(AdminRepository(), adminEmail, adminPassword) as T
        throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
    }
}