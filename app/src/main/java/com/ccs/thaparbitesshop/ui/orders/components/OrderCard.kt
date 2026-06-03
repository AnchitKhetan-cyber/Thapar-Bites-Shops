package com.ccs.thaparbitesshop.ui.orders.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ccs.thaparbitesshop.domain.model.ShopOrder

@Composable
fun OrderCard(
    order: ShopOrder,
    onClick: () -> Unit
) {

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = "Order #${order.id}",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = order.timeAgo
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = order.items.joinToString()
            )

            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.SpaceBetween
            ) {

                Text("₹${order.totalAmount}")

                AssistChip(
                    onClick = {},
                    label = {
                        Text(
                            "Token ${order.tokenNumber}"
                        )
                    }
                )
            }
        }
    }
}