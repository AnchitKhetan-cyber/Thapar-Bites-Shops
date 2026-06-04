package com.ccs.thaparbitesshop.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ccs.thaparbitesshop.ui.profile.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onBack: () -> Unit,
    onLogout: () -> Unit = {},
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var showLogoutDialog by remember { mutableStateOf(false) }

    val primaryColor = Color(0xFF7A1F3D)
    val secondaryColor = Color(0xFF4A1025)
    val accentColor = Color(0xFFC9A227)

    Scaffold(containerColor = MaterialTheme.colorScheme.background) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(20.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .background(Brush.horizontalGradient(listOf(primaryColor, secondaryColor)))
                        .padding(24.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Surface(shape = CircleShape, color = Color.White, modifier = Modifier.size(90.dp)) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Store, null, tint = primaryColor, modifier = Modifier.size(45.dp))
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = state.shopName.ifBlank { "Your Shop" },
                            color = Color.White,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(text = "TIET Campus Food Network", color = Color.White.copy(alpha = 0.85f))

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            ProfileChip(if (state.isOpen) "Open" else "Closed")
                            ProfileChip("Verified")
                            ProfileChip("Partner")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            ProfileItem(Icons.Default.Store,    "Shop", state.shopName.ifBlank { "—" }, primaryColor)
            ProfileItem(Icons.Default.Person,   "Owner", state.ownerName.ifBlank { "—" }, primaryColor)
            ProfileItem(Icons.Default.Email,    "Email", state.email.ifBlank { "—" }, primaryColor)
            ProfileItem(Icons.Default.Phone,    "Phone", state.phone.ifBlank { "—" }, primaryColor)
            ProfileItem(Icons.Default.Store,    "Category", state.category.ifBlank { "—" }, primaryColor)

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { showLogoutDialog = true },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = accentColor, contentColor = Color.Black),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Logout, null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Logout", fontWeight = FontWeight.Bold)
            }
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Logout") },
            text = { Text("Are you sure you want to logout?") },
            confirmButton = {
                TextButton(onClick = { showLogoutDialog = false; onLogout() }) {
                    Text("Logout", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) { Text("Cancel") }
            }
        )
    }
}

@Composable
private fun ProfileItem(icon: ImageVector, title: String, value: String, iconColor: Color) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = null, tint = iconColor)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = title, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(text = value, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun ProfileChip(text: String) {
    Surface(shape = RoundedCornerShape(20.dp), color = Color.White.copy(alpha = 0.15f)) {
        Text(text = text, modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), color = Color.White, fontWeight = FontWeight.Medium)
    }
}