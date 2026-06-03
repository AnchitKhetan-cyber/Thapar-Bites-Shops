package com.ccs.thaparbitesshop.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ccs.thaparbitesshop.ui.components.ShopPrimaryButton
import com.ccs.thaparbitesshop.ui.components.ShopTextField
import com.ccs.thaparbitesshop.ui.theme.ShopDivider
import com.ccs.thaparbitesshop.ui.theme.ShopOrange
import com.ccs.thaparbitesshop.ui.theme.ShopOrangeDark
import com.ccs.thaparbitesshop.ui.theme.ShopTextHint

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    fun validate(): Boolean {
        var valid = true
        emailError = if (email.isBlank()) {
            valid = false; "Email is required"
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            valid = false; "Enter a valid email"
        } else ""
        passwordError = if (password.length < 6) {
            valid = false; "Password must be at least 6 characters"
        } else ""
        return valid
    }

    Box(modifier = Modifier.fillMaxSize()) {

        // Header gradient
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(ShopOrangeDark, ShopOrange)
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(48.dp))

            // Logo
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector        = Icons.Default.Store,
                    contentDescription = null,
                    tint               = ShopOrange,
                    modifier           = Modifier.size(44.dp)
                )
            }

            Spacer(Modifier.height(12.dp))

            Text(
                text       = "Thapar Bites Shop",
                color      = Color.White,
                fontSize   = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text  = "Shop Management Portal",
                color = Color.White.copy(alpha = 0.8f),
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(Modifier.height(32.dp))

            // Card
            Card(
                modifier  = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape     = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors    = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text       = "Welcome Back!",
                        style      = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color      = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text  = "Sign in to manage your shop",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    Spacer(Modifier.height(24.dp))

                    ShopTextField(
                        value         = email,
                        onValueChange = { email = it; emailError = "" },
                        label         = "Email Address",
                        leadingIcon   = {
                            Icon(Icons.Default.Email, contentDescription = null,
                                tint = ShopOrange)
                        },
                        isError       = emailError.isNotEmpty(),
                        errorMessage  = emailError,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                    )

                    Spacer(Modifier.height(16.dp))

                    ShopTextField(
                        value         = password,
                        onValueChange = { password = it; passwordError = "" },
                        label         = "Password",
                        leadingIcon   = {
                            Icon(Icons.Default.Lock, contentDescription = null,
                                tint = ShopOrange)
                        },
                        trailingIcon  = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible)
                                        Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = "Toggle password",
                                    tint = ShopTextHint
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible)
                            VisualTransformation.None else PasswordVisualTransformation(),
                        isError      = passwordError.isNotEmpty(),
                        errorMessage = passwordError,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                    )

                    // Forgot password
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { /* TODO: forgot password */ }) {
                            Text(
                                text  = "Forgot Password?",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }

                    Spacer(Modifier.height(20.dp))

                    ShopPrimaryButton(
                        text      = "Sign In",
                        onClick   = {
                            if (validate()) {
                                isLoading = true
                                // Navigation handled by parent – just call callback
                                onLoginSuccess()
                            }
                        },
                        modifier  = Modifier.fillMaxWidth(),
                        isLoading = isLoading
                    )

                    Spacer(Modifier.height(16.dp))

                    // Divider
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Divider(modifier = Modifier.weight(1f), color = ShopDivider)
                        Text(
                            text     = "  OR  ",
                            style    = MaterialTheme.typography.bodySmall,
                            color    = ShopTextHint
                        )
                        Divider(modifier = Modifier.weight(1f), color = ShopDivider)
                    }

                    Spacer(Modifier.height(16.dp))

                    // Register link
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment     = Alignment.CenterVertically
                    ) {
                        Text(
                            text  = "New shop owner?",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        TextButton(onClick = onNavigateToRegister) {
                            Text(
                                text       = "Register Here",
                                color      = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold,
                                style      = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            Text(
                text      = "© 2025 Thapar Bites. All rights reserved.",
                style     = MaterialTheme.typography.bodySmall,
                color     = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(24.dp))
        }
    }
}