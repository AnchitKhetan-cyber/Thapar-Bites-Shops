package com.ccs.thaparbitesshop.ui.orderdetail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ccs.thaparbitesshop.domain.model.OrderStatus

@Composable
fun StatusSelector(
    currentStatus: OrderStatus,
    onStatusChange: (OrderStatus) -> Unit
) {

    Row(
        horizontalArrangement =
            Arrangement.spacedBy(8.dp)
    ) {

        OrderStatus.entries.forEach { status ->

            FilterChip(
                selected = status == currentStatus,
                onClick = {
                    onStatusChange(status)
                },
                label = {
                    Text(status.name)
                }
            )
        }
    }
}