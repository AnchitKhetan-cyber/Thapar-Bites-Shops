package com.ccs.thaparbitesshop.ui.orderdetail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ccs.thaparbitesshop.domain.model.OrderStatus
import com.ccs.thaparbitesshop.ui.orderdetail.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailScreen(
    viewModel: OrderDetailViewModel,
    onBack: () -> Unit
) {

    val state by viewModel
        .uiState
        .collectAsStateWithLifecycle()

    Scaffold(
        topBar = {

            TopAppBar(
                title = {
                    Text(
                        "Order #${state.orderId}"
                    )
                }
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding =
                PaddingValues(16.dp),
            verticalArrangement =
                Arrangement.spacedBy(16.dp)
        ) {

            item {

                Card {

                    Column(
                        Modifier.padding(16.dp)
                    ) {

                        Text(
                            "Token #${state.tokenNumber}",
                            style =
                                MaterialTheme
                                    .typography
                                    .headlineMedium
                        )

                        Spacer(
                            Modifier.height(8.dp)
                        )

                        Text(
                            state.customerName
                        )

                        Text(
                            state.customerPhone
                        )
                    }
                }
            }

            item {

                StatusSelector(
                    currentStatus =
                        state.status,
                    onStatusChange =
                        viewModel::updateStatus
                )
            }

            item {

                Text(
                    "Items",
                    style =
                        MaterialTheme
                            .typography
                            .titleMedium
                )
            }

            items(state.items) {

                OrderItemRow(it)
            }

            item {

                HorizontalDivider()
            }

            item {

                Row(
                    modifier =
                        Modifier.fillMaxWidth(),
                    horizontalArrangement =
                        Arrangement.SpaceBetween
                ) {

                    Text("Total")

                    Text(
                        "₹${state.total}",
                        style =
                            MaterialTheme
                                .typography
                                .titleMedium
                    )
                }
            }

            item {

                Card {

                    Column(
                        Modifier.padding(16.dp)
                    ) {

                        Text("Payment")

                        Text(
                            state.paymentMethod
                        )
                    }
                }
            }

            item {

                Card {

                    Column(
                        Modifier.padding(16.dp)
                    ) {

                        Text("Special Note")

                        Text(state.note)
                    }
                }
            }

            item {

                when(state.status) {

                    OrderStatus.NEW -> {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {

                            Button(
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    viewModel.updateStatus(
                                        OrderStatus.PREPARING
                                    )
                                }
                            ) {
                                Text("Accept")
                            }

                            OutlinedButton(
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    viewModel.updateStatus(
                                        OrderStatus.CANCELLED
                                    )
                                }
                            ) {
                                Text("Reject")
                            }
                        }
                    }

                    OrderStatus.PREPARING -> {

                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                viewModel.updateStatus(
                                    OrderStatus.READY
                                )
                            }
                        ) {
                            Text("Mark Ready")
                        }
                    }

                    OrderStatus.READY -> {

                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                viewModel.updateStatus(
                                    OrderStatus.COMPLETED
                                )
                            }
                        ) {
                            Text("Mark Completed")
                        }
                    }

                    OrderStatus.COMPLETED -> {

                        FilledTonalButton(
                            onClick = {},
                            enabled = false,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Completed ✓")
                        }
                    }

                    OrderStatus.CANCELLED -> {

                        FilledTonalButton(
                            onClick = {},
                            enabled = false,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Cancelled ✕")
                        }
                    }
                }
            }
        }
    }
}

private fun getActionText(
    status: OrderStatus
): String {

    return when(status) {

        OrderStatus.NEW ->
            "Accept Order"

        OrderStatus.PREPARING ->
            "Mark Ready"

        OrderStatus.READY ->
            "Mark Completed"

        OrderStatus.COMPLETED ->
            "Completed ✓"

        OrderStatus.CANCELLED ->
            "Cancelled ✕"
    }
}