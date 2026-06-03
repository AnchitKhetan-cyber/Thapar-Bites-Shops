package com.ccs.thaparbitesshop.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ccs.thaparbitesshop.data.model.ShopCategory
import com.ccs.thaparbitesshop.data.model.ShopOwner
import com.ccs.thaparbitesshop.ui.viewmodel.AdminViewModel
import com.ccs.thaparbitesshop.ui.viewmodel.AdminViewModelFactory

// ── Brand tokens ──────────────────────────────────────────────────────────────
private val OrangePrimary   = Color(0xFFFF6B00)
private val BgDark          = Color(0xFF1A1A1A)
private val SurfaceDark     = Color(0xFF242424)
private val SurfaceElevated = Color(0xFF2E2E2E)
private val BorderColor     = Color(0xFF3A3A3A)
private val TextWhite       = Color(0xFFFAFAFA)
private val TextMuted       = Color(0xFFB0B0B0)
private val ErrorRed        = Color(0xFFFF5252)
private val SuccessGreen    = Color(0xFF4CAF50)

/**
 * CreateShopScreen
 *
 * Admin-only form to register a new shop owner account.
 * Collects: owner name, email, password, shop name, location, category.
 *
 * NavHost wiring:
 *   composable("create_shop") {
 *       CreateShopScreen(
 *           adminEmail    = savedAdminEmail,
 *           adminPassword = savedAdminPassword,
 *           onSuccess     = { navController.popBackStack() },
 *           onBack        = { navController.popBackStack() }
 *       )
 *   }
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateShopScreen(
    adminEmail: String,
    adminPassword: String,
    onSuccess: () -> Unit,
    onBack: () -> Unit,
    vm: AdminViewModel = viewModel(
        factory = AdminViewModelFactory(adminEmail, adminPassword)
    )
) {
    val state by vm.formState.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current
    val scrollState  = rememberScrollState()

    // Navigate on success
    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            vm.onSuccessDismissed()
            onSuccess()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Register Shop", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = TextWhite)
                        Text("Admin Panel", fontSize = 11.sp, color = TextMuted)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Outlined.ArrowBack, contentDescription = "Back", tint = TextWhite)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SurfaceDark)
            )
        },
        containerColor = BgDark
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {

            // ── Section: Owner Info ───────────────────────────────────────────
            SectionHeader("👤  Owner Information")
            Spacer(Modifier.height(12.dp))

            FormCard {
                FormField(
                    label       = "Owner Full Name",
                    value       = state.ownerName,
                    onChange    = vm::onOwnerNameChange,
                    placeholder = "e.g. Rajesh Kumar",
                    icon        = Icons.Outlined.Person,
                    error       = state.ownerNameError,
                    imeAction   = ImeAction.Next,
                    onNext      = { focusManager.moveFocus(FocusDirection.Down) }
                )

                Spacer(Modifier.height(14.dp))

                FormField(
                    label       = "Email Address",
                    value       = state.email,
                    onChange    = vm::onEmailChange,
                    placeholder = "shopowner@email.com",
                    icon        = Icons.Outlined.Email,
                    error       = state.emailError,
                    keyboardType = KeyboardType.Email,
                    imeAction   = ImeAction.Next,
                    onNext      = { focusManager.moveFocus(FocusDirection.Down) }
                )

                Spacer(Modifier.height(14.dp))

                PasswordFormField(
                    label       = "Set Password",
                    value       = state.password,
                    onChange    = vm::onPasswordChange,
                    isVisible   = state.isPasswordVisible,
                    onToggle    = vm::onTogglePassword,
                    error       = state.passwordError,
                    imeAction   = ImeAction.Next,
                    onNext      = { focusManager.moveFocus(FocusDirection.Down) }
                )

                Spacer(Modifier.height(14.dp))

                PasswordFormField(
                    label       = "Confirm Password",
                    value       = state.confirmPassword,
                    onChange    = vm::onConfirmPasswordChange,
                    isVisible   = state.isConfirmPasswordVisible,
                    onToggle    = vm::onToggleConfirmPassword,
                    error       = state.confirmPasswordError,
                    imeAction   = ImeAction.Next,
                    onNext      = { focusManager.moveFocus(FocusDirection.Down) }
                )
            }

            Spacer(Modifier.height(20.dp))

            // ── Section: Shop Info ────────────────────────────────────────────
            SectionHeader("🏪  Shop Information")
            Spacer(Modifier.height(12.dp))

            FormCard {
                FormField(
                    label       = "Shop Name",
                    value       = state.shopName,
                    onChange    = vm::onShopNameChange,
                    placeholder = "e.g. Sharma Ji ki Chai",
                    icon        = Icons.Outlined.Store,
                    error       = state.shopNameError,
                    imeAction   = ImeAction.Next,
                    onNext      = { focusManager.moveFocus(FocusDirection.Down) }
                )

                Spacer(Modifier.height(14.dp))

                FormField(
                    label       = "Location on Campus",
                    value       = state.location,
                    onChange    = vm::onLocationChange,
                    placeholder = "e.g. Near LT-6, Block C",
                    icon        = Icons.Outlined.LocationOn,
                    error       = state.locationError,
                    imeAction   = ImeAction.Done,
                    onNext      = { focusManager.clearFocus() }
                )

                Spacer(Modifier.height(14.dp))

                // Category picker
                Text("Category", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = TextMuted)
                Spacer(Modifier.height(8.dp))
                CategoryPicker(
                    selected  = state.category,
                    onSelect  = vm::onCategoryChange
                )
            }

            Spacer(Modifier.height(20.dp))

            // ── Submit error ──────────────────────────────────────────────────
            AnimatedVisibility(visible = state.submitError != null, enter = fadeIn(), exit = fadeOut()) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(ErrorRed.copy(alpha = 0.12f))
                            .border(1.dp, ErrorRed.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("⚠️", fontSize = 14.sp)
                        Text(state.submitError ?: "", fontSize = 12.sp, color = ErrorRed, modifier = Modifier.weight(1f))
                    }
                    Spacer(Modifier.height(16.dp))
                }
            }

            // ── Submit button ─────────────────────────────────────────────────
            Button(
                onClick  = { focusManager.clearFocus(); vm.createShopOwner() },
                enabled  = !state.isLoading,
                shape    = RoundedCornerShape(14.dp),
                colors   = ButtonDefaults.buttonColors(
                    containerColor         = OrangePrimary,
                    contentColor           = Color.White,
                    disabledContainerColor = OrangePrimary.copy(alpha = 0.5f)
                ),
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(22.dp), strokeWidth = 2.dp)
                } else {
                    Icon(Icons.Outlined.PersonAdd, null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Create Shop Account", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 0.3.sp)
                }
            }

            Spacer(Modifier.height(12.dp))

            Text(
                text     = "The shop owner will be able to log in immediately with these credentials.",
                fontSize = 11.sp,
                color    = TextMuted.copy(alpha = 0.6f),
                lineHeight = 16.sp
            )

            Spacer(Modifier.height(32.dp))
        }
    }
}

// ── Admin Dashboard Screen ────────────────────────────────────────────────────

/**
 * AdminDashboardScreen
 *
 * Lists all registered shops. Admin can activate/deactivate or delete shops,
 * and navigate to CreateShopScreen to add new ones.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    adminEmail: String,
    adminPassword: String,
    onCreateShop: () -> Unit,
    onLogout: () -> Unit,
    vm: AdminViewModel = viewModel(
        factory = AdminViewModelFactory(adminEmail, adminPassword)
    )
) {
    val state by vm.dashState.collectAsStateWithLifecycle()
    var deleteTarget by remember { mutableStateOf<String?>(null) }

    // Delete confirmation dialog
    if (deleteTarget != null) {
        AlertDialog(
            onDismissRequest = { deleteTarget = null },
            containerColor   = SurfaceDark,
            title = { Text("Delete shop?", color = TextWhite, fontWeight = FontWeight.SemiBold) },
            text  = { Text("This removes the Firestore record. The Auth account remains — delete it manually in Firebase Console.", color = TextMuted, fontSize = 13.sp) },
            confirmButton = {
                TextButton(onClick = { vm.deleteShop(deleteTarget!!); deleteTarget = null }) {
                    Text("Delete", color = ErrorRed, fontWeight = FontWeight.SemiBold)
                }
            },
            dismissButton = {
                TextButton(onClick = { deleteTarget = null }) {
                    Text("Cancel", color = TextMuted)
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Admin Panel", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = TextWhite)
                        Text("Thapar Bites", fontSize = 11.sp, color = TextMuted)
                    }
                },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Outlined.Logout, "Sign out", tint = TextMuted)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SurfaceDark)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick          = onCreateShop,
                containerColor   = OrangePrimary,
                contentColor     = Color.White,
                shape            = RoundedCornerShape(14.dp)
            ) {
                Icon(Icons.Outlined.Add, "Register new shop")
            }
        },
        containerColor = BgDark
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Stats row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MiniStat("Total Shops", state.shops.size.toString(), Modifier.weight(1f))
                MiniStat("Active", state.shops.count { it.isActive }.toString(), Modifier.weight(1f), SuccessGreen)
                MiniStat("Inactive", state.shops.count { !it.isActive }.toString(), Modifier.weight(1f), ErrorRed)
            }

            if (state.isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = OrangePrimary)
                }
            } else if (state.shops.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🏪", fontSize = 48.sp)
                        Spacer(Modifier.height(12.dp))
                        Text("No shops registered yet", color = TextMuted, fontSize = 14.sp)
                        Spacer(Modifier.height(8.dp))
                        Text("Tap + to add your first shop", color = TextMuted.copy(alpha = 0.5f), fontSize = 12.sp)
                    }
                }
            } else {
                androidx.compose.foundation.lazy.LazyColumn(
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(state.shops.size) { index ->
                        ShopCard(
                            shop          = state.shops[index],
                            onToggleActive = { vm.toggleShopActive(state.shops[index].uid, state.shops[index].isActive) },
                            onDelete      = { deleteTarget = state.shops[index].uid }
                        )
                    }
                    item { Spacer(Modifier.height(80.dp)) }
                }
            }
        }
    }
}

// ── Sub-composables ───────────────────────────────────────────────────────────

@Composable
private fun ShopCard(
    shop: ShopOwner,
    onToggleActive: () -> Unit,
    onDelete: () -> Unit
) {
    val category = ShopCategory.fromName(shop.category)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(SurfaceDark)
            .border(1.dp, BorderColor, RoundedCornerShape(14.dp))
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Emoji icon
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(OrangePrimary.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Text(category.emoji, fontSize = 22.sp)
        }

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(shop.shopName, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextWhite)
            Text(shop.ownerName, fontSize = 12.sp, color = TextMuted)
            Text(shop.location,  fontSize = 11.sp, color = TextMuted.copy(alpha = 0.6f))
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Active toggle
            Switch(
                checked   = shop.isActive,
                onCheckedChange = { onToggleActive() },
                colors    = SwitchDefaults.colors(
                    checkedTrackColor   = SuccessGreen,
                    uncheckedTrackColor = SurfaceElevated
                ),
                modifier  = Modifier.size(width = 40.dp, height = 24.dp)
                    .padding(0.dp)
            )
            Spacer(Modifier.height(4.dp))
            // Delete
            IconButton(onClick = onDelete, modifier = Modifier.size(28.dp)) {
                Icon(Icons.Outlined.Delete, "Delete shop", tint = ErrorRed.copy(alpha = 0.7f), modifier = Modifier.size(16.dp))
            }
        }
    }
}

@Composable
private fun CategoryPicker(
    selected: ShopCategory,
    onSelect: (ShopCategory) -> Unit
) {
    val rows = ShopCategory.entries.chunked(3)
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        rows.forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                row.forEach { cat ->
                    val isSelected = cat == selected
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) OrangePrimary.copy(alpha = 0.18f) else SurfaceElevated)
                            .border(
                                width = if (isSelected) 1.dp else 0.5.dp,
                                color = if (isSelected) OrangePrimary else BorderColor,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .clickable { onSelect(cat) }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(cat.emoji, fontSize = 18.sp)
                            Spacer(Modifier.height(3.dp))
                            Text(
                                text = cat.displayName.split(" ").first(),
                                fontSize = 10.sp,
                                color = if (isSelected) OrangePrimary else TextMuted,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                            )
                        }
                    }
                }
                // Fill empty cells in last row
                repeat(3 - row.size) { Spacer(Modifier.weight(1f)) }
            }
        }
    }
}

@Composable
private fun FormField(
    label: String,
    value: String,
    onChange: (String) -> Unit,
    placeholder: String,
    icon: ImageVector,
    error: String?,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    onNext: () -> Unit = {}
) {
    Text(label, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = TextMuted)
    Spacer(Modifier.height(6.dp))
    OutlinedTextField(
        value         = value,
        onValueChange = onChange,
        placeholder   = { Text(placeholder, fontSize = 13.sp, color = TextMuted.copy(alpha = 0.4f)) },
        leadingIcon   = { Icon(icon, null, tint = if (error != null) ErrorRed else TextMuted, modifier = Modifier.size(18.dp)) },
        isError       = error != null,
        supportingText = error?.let { { Text(it, color = ErrorRed, fontSize = 11.sp) } },
        singleLine    = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        keyboardActions = KeyboardActions(
            onNext = { onNext() }, onDone = { onNext() }, onSend = { onNext() }
        ),
        colors  = fieldColors(error != null),
        shape   = RoundedCornerShape(10.dp),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun PasswordFormField(
    label: String,
    value: String,
    onChange: (String) -> Unit,
    isVisible: Boolean,
    onToggle: () -> Unit,
    error: String?,
    imeAction: ImeAction = ImeAction.Next,
    onNext: () -> Unit = {}
) {
    Text(label, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = TextMuted)
    Spacer(Modifier.height(6.dp))
    OutlinedTextField(
        value                = value,
        onValueChange        = onChange,
        placeholder          = { Text("••••••••", fontSize = 13.sp, color = TextMuted.copy(alpha = 0.4f)) },
        leadingIcon          = { Icon(Icons.Outlined.Lock, null, tint = if (error != null) ErrorRed else TextMuted, modifier = Modifier.size(18.dp)) },
        trailingIcon         = {
            IconButton(onClick = onToggle) {
                Icon(
                    if (isVisible) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                    if (isVisible) "Hide" else "Show",
                    tint = TextMuted, modifier = Modifier.size(18.dp)
                )
            }
        },
        visualTransformation = if (isVisible) VisualTransformation.None else PasswordVisualTransformation(),
        isError              = error != null,
        supportingText       = error?.let { { Text(it, color = ErrorRed, fontSize = 11.sp) } },
        singleLine           = true,
        keyboardOptions      = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = imeAction),
        keyboardActions      = KeyboardActions(onNext = { onNext() }, onDone = { onNext() }),
        colors  = fieldColors(error != null),
        shape   = RoundedCornerShape(10.dp),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun SectionHeader(text: String) {
    Text(text, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextMuted, letterSpacing = 0.4.sp)
}

@Composable
private fun FormCard(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceDark)
            .border(1.dp, BorderColor, RoundedCornerShape(16.dp))
            .padding(16.dp),
        content = content
    )
}

@Composable
private fun MiniStat(label: String, value: String, modifier: Modifier, color: Color = TextWhite) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceDark)
            .border(1.dp, BorderColor, RoundedCornerShape(12.dp))
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(value, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = color)
        Text(label, fontSize = 10.sp, color = TextMuted)
    }
}

@Composable
private fun fieldColors(isError: Boolean) = OutlinedTextFieldDefaults.colors(
    focusedBorderColor      = if (isError) ErrorRed else OrangePrimary,
    unfocusedBorderColor    = if (isError) ErrorRed.copy(alpha = 0.6f) else BorderColor,
    focusedTextColor        = TextWhite,
    unfocusedTextColor      = TextWhite,
    cursorColor             = OrangePrimary,
    focusedContainerColor   = SurfaceElevated,
    unfocusedContainerColor = SurfaceElevated,
    errorContainerColor     = SurfaceElevated
)

@Preview(showBackground = true)
@Composable
private fun Preview() {
    CreateShopScreen("", "", {}, {})
}