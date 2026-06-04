package com.ccs.thaparbitesshop.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ccs.thaparbitesshop.data.model.MenuItem
import com.ccs.thaparbitesshop.ui.menu.MenuViewModel

@Composable
fun MenuScreen(
    onBack: () -> Unit,
    onNavigateToAddFood: () -> Unit
) {

    val viewModel: MenuViewModel = hiltViewModel()

    val state by viewModel
        .uiState
        .collectAsStateWithLifecycle()

    val primaryColor = Color(0xFF7A1F3D)
    val secondaryColor = Color(0xFF4A1025)
    val accentColor = Color(0xFFC9A227)

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            MenuHeader()

            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                Button(
                    onClick = onNavigateToAddFood,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = accentColor,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {

                    Icon(
                        Icons.Default.Add,
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        "Add New Item",
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    items(state.items) { item ->

                        MenuItemCard(
                            item = item,
                            onAvailabilityToggle = {
                                viewModel.toggleAvailability(item.id)
                            },
                            onDelete = {
                                viewModel.deleteItem(item.id)
                            }
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun MenuHeader() {

    val primaryColor = Color(0xFF7A1F3D)
    val secondaryColor = Color(0xFF4A1025)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {

        Box(
            modifier = Modifier
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            primaryColor,
                            secondaryColor
                        )
                    )
                )
                .padding(20.dp)
                .fillMaxWidth()
        ) {

            Column {

                Text(
                    text = "🍔 Menu Management",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

                Text(
                    text = "Manage your shop menu items",
                    color = Color.White.copy(alpha = 0.85f)
                )
            }
        }
    }
}

@Composable
fun MenuItemCard(
    item: MenuItem,
    onAvailabilityToggle: () -> Unit,
    onDelete: () -> Unit
) {

    val primaryColor = Color(0xFF7A1F3D)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    Icons.Default.RestaurantMenu,
                    contentDescription = null,
                    tint = primaryColor
                )

                Spacer(
                    modifier = Modifier.width(8.dp)
                )

                Text(
                    text = item.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = item.description,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = "₹${item.price}",
                color = primaryColor,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = if (item.isAvailable)
                            "Available"
                        else
                            "Unavailable"
                    )

                    Spacer(
                        modifier = Modifier.width(8.dp)
                    )

                    Switch(
                        checked = item.isAvailable,
                        onCheckedChange = {
                            onAvailabilityToggle()
                        }
                    )
                }

                OutlinedButton(
                    onClick = onDelete,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {

                    Icon(
                        Icons.Default.Delete,
                        contentDescription = null
                    )

                    Spacer(
                        modifier = Modifier.width(4.dp)
                    )

                    Text("Delete")
                }
            }
        }
    }
}