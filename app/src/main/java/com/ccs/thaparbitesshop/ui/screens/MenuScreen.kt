package com.ccs.thaparbitesshop.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ccs.thaparbitesshop.data.model.MenuItem
import com.ccs.thaparbitesshop.ui.menu.MenuViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    onBack: () -> Unit,
    onNavigateToAddFood: () -> Unit
) {

    val viewModel: MenuViewModel = viewModel()

    val state by viewModel
        .uiState
        .collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Menu Management") }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            Text(
                text = "Menu Items",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    onNavigateToAddFood()
                }
            ) {
                Text("Add Item")
            }

            Spacer(Modifier.height(24.dp))

            LazyColumn {

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

@Composable
fun MenuItemCard(
    item: MenuItem,
    onAvailabilityToggle: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(item.name)

            Text(item.description)

            Text("₹${item.price}")

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Switch(
                checked = item.isAvailable,
                onCheckedChange = {
                    onAvailabilityToggle()
                }
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Button(
                onClick = onDelete
            ) {
                Text("Delete")
            }
        }
    }
}