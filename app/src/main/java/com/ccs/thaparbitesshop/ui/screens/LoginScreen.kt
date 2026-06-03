package com.ccs.thaparbitesshop.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

// ── Brand tokens ──────────────────────────────────────────────────────────────
private val OrangePrimary   = Color(0xFFFF6B00)
private val OrangeDark      = Color(0xFFCC5500)
private val BgDark          = Color(0xFF1A1A1A)
private val SurfaceDark     = Color(0xFF242424)
private val SurfaceElevated = Color(0xFF2E2E2E)
private val BorderColor     = Color(0xFF3A3A3A)
private val TextWhite       = Color(0xFFFAFAFA)
private val TextMuted       = Color(0xFFB0B0B0)
private val ErrorRed        = Color(0xFFFF5252)

/**
 * LoginScreen — Shop Owner
 *
 * Authenticates shop owners via Firebase Auth (email + password).
 * On success calls [onLoginSuccess] so the NavHost can navigate to the
 * shop dashboard.
 *
 * NavHost usage:
 *   composable("login") {
 *       LoginScreen(
 *           onLoginSuccess   = { navController.navigate("dashboard") { ... } },
 *           onForgotPassword = { navController.navigate("forgot_password") }
 *       )
 *   }
 */
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onForgotPassword: () -> Unit = {}
) {
    val auth: FirebaseAuth = Firebase.auth

    // ── State ─────────────────────────────────────────────────────────────────
    var email       by remember { mutableStateOf("") }
    var password    by remember { mutableStateOf("") }
    var showPass    by remember { mutableStateOf(false) }
    var isLoading   by remember { mutableStateOf(false) }
    var errorMsg    by remember { mutableStateOf<String?>(null) }

    // Animation: reveal the card on first composition
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    val focusManager = LocalFocusManager.current
    val scrollState  = rememberScrollState()

    // ── Helpers ───────────────────────────────────────────────────────────────
    fun validate(): String? = when {
        email.isBlank()                   -> "Email is required."
        !email.contains('@')              -> "Enter a valid email address."
        password.length < 6              -> "Password must be at least 6 characters."
        else                              -> null
    }

    fun login() {
        errorMsg = validate() ?: run {
            isLoading = true
            focusManager.clearFocus()
            auth.signInWithEmailAndPassword(email.trim(), password)
                .addOnCompleteListener { task ->
                    isLoading = false
                    if (task.isSuccessful) {
                        onLoginSuccess()
                    } else {
                        errorMsg = task.exception?.message
                            ?: "Login failed. Please try again."
                    }
                }
            return
        }
    }

    // ── UI ────────────────────────────────────────────────────────────────────
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDark)
            .imePadding(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // ── Header ────────────────────────────────────────────────────────
            AnimatedVisibility(
                visible = visible,
                enter   = fadeIn() + slideInVertically(initialOffsetY = { -30 })
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🍽️", fontSize = 48.sp)
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text       = "Thapar Bites",
                        fontSize   = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color      = OrangePrimary
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text     = "Shop Partner Portal",
                        fontSize = 13.sp,
                        color    = TextMuted
                    )
                }
            }

            Spacer(Modifier.height(36.dp))

            // ── Login card ────────────────────────────────────────────────────
            AnimatedVisibility(
                visible = visible,
                enter   = fadeIn() + slideInVertically(initialOffsetY = { 60 })
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(SurfaceDark)
                        .border(
                            width = 1.dp,
                            color = BorderColor,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(24.dp)
                ) {
                    Text(
                        text       = "Welcome back",
                        fontSize   = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color      = TextWhite
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text     = "Sign in to manage your shop",
                        fontSize = 13.sp,
                        color    = TextMuted
                    )

                    Spacer(Modifier.height(24.dp))

                    // ── Email field ───────────────────────────────────────────
                    FieldLabel("Email address")
                    Spacer(Modifier.height(6.dp))
                    OutlinedTextField(
                        value          = email,
                        onValueChange  = { email = it; errorMsg = null },
                        placeholder    = { FieldPlaceholder("shopname@tiet.ac.in") },
                        singleLine     = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction    = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        ),
                        colors  = inputColors(),
                        shape   = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(16.dp))

                    // ── Password field ────────────────────────────────────────
                    Row(
                        modifier            = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment   = Alignment.CenterVertically
                    ) {
                        FieldLabel("Password")
                        Text(
                            text     = "Forgot password?",
                            fontSize = 12.sp,
                            color    = OrangePrimary,
                            modifier = Modifier.clickable(onClick = onForgotPassword)
                        )
                    }
                    Spacer(Modifier.height(6.dp))
                    OutlinedTextField(
                        value         = password,
                        onValueChange = { password = it; errorMsg = null },
                        placeholder   = { FieldPlaceholder("••••••••") },
                        singleLine    = true,
                        visualTransformation = if (showPass)
                            VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction    = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(onDone = { login() }),
                        trailingIcon = {
                            IconButton(onClick = { showPass = !showPass }) {
                                Icon(
                                    painter = painterResource(
                                        // Use your actual drawable resource names:
                                        // ic_eye_open / ic_eye_closed — or swap for
                                        // Icons.Default.Visibility / VisibilityOff
                                        if (showPass) android.R.drawable.ic_menu_view
                                        else          android.R.drawable.ic_secure
                                    ),
                                    contentDescription = if (showPass) "Hide password"
                                    else "Show password",
                                    tint   = TextMuted,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        },
                        colors   = inputColors(),
                        shape    = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    // ── Error message ─────────────────────────────────────────
                    AnimatedVisibility(visible = errorMsg != null) {
                        Spacer(Modifier.height(10.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(ErrorRed.copy(alpha = 0.12f))
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text     = errorMsg ?: "",
                                fontSize = 12.sp,
                                color    = ErrorRed,
                                lineHeight = 17.sp
                            )
                        }
                    }

                    Spacer(Modifier.height(24.dp))

                    // ── Sign in button ────────────────────────────────────────
                    Button(
                        onClick  = { login() },
                        enabled  = !isLoading,
                        shape    = RoundedCornerShape(12.dp),
                        colors   = ButtonDefaults.buttonColors(
                            containerColor         = OrangePrimary,
                            contentColor           = Color.White,
                            disabledContainerColor = OrangePrimary.copy(alpha = 0.5f),
                            disabledContentColor   = Color.White.copy(alpha = 0.7f)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                color     = Color.White,
                                modifier  = Modifier.size(22.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text       = "Sign In",
                                fontSize   = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            // ── Help text ─────────────────────────────────────────────────────
            Text(
                text      = "Only registered shop partners can log in.\nContact the Thapar Bites admin to register your shop.",
                fontSize  = 11.sp,
                color     = TextMuted.copy(alpha = 0.6f),
                textAlign = TextAlign.Center,
                lineHeight = 16.sp,
                modifier  = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(Modifier.height(32.dp))
        }
    }
}

// ── Small reusable helpers ────────────────────────────────────────────────────

@Composable
private fun FieldLabel(text: String) = Text(
    text     = text,
    fontSize = 13.sp,
    fontWeight = FontWeight.Medium,
    color    = TextMuted
)

@Composable
private fun FieldPlaceholder(text: String) = Text(
    text     = text,
    fontSize = 14.sp,
    color    = TextMuted.copy(alpha = 0.4f)
)

@Composable
private fun inputColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor   = OrangePrimary,
    unfocusedBorderColor = BorderColor,
    focusedTextColor     = TextWhite,
    unfocusedTextColor   = TextWhite,
    cursorColor          = OrangePrimary,
    focusedContainerColor   = SurfaceElevated,
    unfocusedContainerColor = SurfaceElevated
)

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    LoginScreen(onLoginSuccess = {})
}