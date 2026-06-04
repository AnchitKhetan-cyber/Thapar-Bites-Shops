package com.ccs.thaparbitesshop.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ccs.thaparbitesshop.ui.register.RegisterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    val primaryColor = Color(0xFF7A1F3D)
    val secondaryColor = Color(0xFF4A1025)
    val accentColor = Color(0xFFC9A227)
    val bgColor = MaterialTheme.colorScheme.background

    var shopName by remember { mutableStateOf("") }
    var ownerName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("") }
    var categoryExpanded by remember { mutableStateOf(false) }
    var localError by remember { mutableStateOf("") }

    val categories = listOf("Fast Food","South Indian","North Indian","Chinese","Beverages","Snacks","Desserts","Other")

    // Navigate on success
    LaunchedEffect(state.registerSuccess) {
        if (state.registerSuccess) onRegisterSuccess()
    }

    Box(modifier = Modifier.fillMaxSize().background(bgColor)) {
        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {

            // Header
            Box(
                modifier = Modifier.fillMaxWidth().height(160.dp)
                    .background(Brush.verticalGradient(listOf(primaryColor, secondaryColor))),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text("Register Your Shop", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text("Join Thapar Bites as a partner", fontSize = 13.sp, color = Color.White.copy(alpha = 0.85f))
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SectionHeader("Shop Information", primaryColor)

                OutlinedTextField(
                    value = shopName, onValueChange = { shopName = it },
                    label = { Text("Shop Name") },
                    leadingIcon = { Icon(Icons.Default.Store, null, tint = primaryColor) },
                    modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp),
                    singleLine = true, colors = outlinedFieldColors(primaryColor)
                )

                ExposedDropdownMenuBox(
                    expanded = categoryExpanded,
                    onExpandedChange = { categoryExpanded = !categoryExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedCategory, onValueChange = {}, readOnly = true,
                        label = { Text("Shop Category") },
                        leadingIcon = { Icon(Icons.Default.Category, null, tint = primaryColor) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                        modifier = Modifier.fillMaxWidth().menuAnchor(),
                        shape = RoundedCornerShape(12.dp), colors = outlinedFieldColors(primaryColor)
                    )
                    ExposedDropdownMenu(
                        expanded = categoryExpanded,
                        onDismissRequest = { categoryExpanded = false }
                    ) {
                        categories.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category) },
                                onClick = { selectedCategory = category; categoryExpanded = false }
                            )
                        }
                    }
                }

                SectionHeader("Owner Details", primaryColor)

                OutlinedTextField(
                    value = ownerName, onValueChange = { ownerName = it },
                    label = { Text("Owner Name") },
                    leadingIcon = { Icon(Icons.Default.Person, null, tint = primaryColor) },
                    modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp),
                    singleLine = true, colors = outlinedFieldColors(primaryColor)
                )
                OutlinedTextField(
                    value = phone, onValueChange = { if (it.length <= 10) phone = it },
                    label = { Text("Phone Number") },
                    leadingIcon = { Icon(Icons.Default.Phone, null, tint = primaryColor) },
                    modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true, colors = outlinedFieldColors(primaryColor)
                )
                OutlinedTextField(
                    value = email, onValueChange = { email = it },
                    label = { Text("Email Address") },
                    leadingIcon = { Icon(Icons.Default.Email, null, tint = primaryColor) },
                    modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true, colors = outlinedFieldColors(primaryColor)
                )

                SectionHeader("Set Password", primaryColor)

                OutlinedTextField(
                    value = password, onValueChange = { password = it },
                    label = { Text("Password") },
                    leadingIcon = { Icon(Icons.Default.Lock, null, tint = primaryColor) },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility, null)
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true, colors = outlinedFieldColors(primaryColor)
                )
                OutlinedTextField(
                    value = confirmPassword, onValueChange = { confirmPassword = it },
                    label = { Text("Confirm Password") },
                    leadingIcon = { Icon(Icons.Default.Lock, null, tint = primaryColor) },
                    trailingIcon = {
                        IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                            Icon(if (confirmPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility, null)
                        }
                    },
                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true, colors = outlinedFieldColors(primaryColor)
                )

                val displayError = localError.ifBlank { state.error ?: "" }
                if (displayError.isNotEmpty()) {
                    Text(text = displayError, color = MaterialTheme.colorScheme.error, fontSize = 13.sp)
                }

                Spacer(modifier = Modifier.height(4.dp))

                Button(
                    onClick = {
                        localError = when {
                            shopName.isBlank() || ownerName.isBlank() || phone.isBlank() || email.isBlank() ->
                                "Please fill in all fields"
                            selectedCategory.isBlank() -> "Please select a shop category"
                            password.length < 6 -> "Password must be at least 6 characters"
                            password != confirmPassword -> "Passwords do not match"
                            else -> ""
                        }
                        if (localError.isBlank()) {
                            viewModel.register(
                                email = email,
                                password = password,
                                shopName = shopName,
                                ownerName = ownerName,
                                phone = phone,
                                category = selectedCategory
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                    enabled = !state.isLoading
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(22.dp), color = Color.White, strokeWidth = 2.dp)
                    } else {
                        Text("Create Account", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Already have an account? ", color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f))
                    TextButton(onClick = onNavigateBack) {
                        Text("Sign In", color = primaryColor, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 4.dp)) {
        Box(modifier = Modifier.width(4.dp).height(20.dp).background(color, RoundedCornerShape(2.dp)))
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
    }
}

@Composable
private fun outlinedFieldColors(primaryColor: Color) = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = primaryColor,
    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
    focusedLabelColor = primaryColor,
    unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
    cursorColor = primaryColor,
    focusedTextColor = MaterialTheme.colorScheme.onSurface,
    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
    focusedLeadingIconColor = primaryColor,
    unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
    focusedTrailingIconColor = primaryColor,
    unfocusedTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
    focusedContainerColor = Color.Transparent,
    unfocusedContainerColor = Color.Transparent
)