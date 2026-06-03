package com.ccs.thaparbitesshop.ui.menu

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFoodScreen(
    viewModel: MenuViewModel,
    onBack: () -> Unit
) {

    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Add Food Item")
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(
                    rememberScrollState()
                )
        ) {

            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                },
                label = {
                    Text("Food Name")
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = description,
                onValueChange = {
                    description = it
                },
                label = {
                    Text("Description")
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = category,
                onValueChange = {
                    category = it
                },
                label = {
                    Text("Category")
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = price,
                onValueChange = {
                    price = it
                },
                label = {
                    Text("Price")
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(24.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {

                    viewModel.addItem(
                        name = name,
                        description = description,
                        category = category,
                        price = price.toDoubleOrNull() ?: 0.0
                    )

                    onBack()
                }
            ) {
                Text("Save Item")
            }
        }
    }
}