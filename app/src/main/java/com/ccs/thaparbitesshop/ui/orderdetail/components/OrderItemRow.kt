package com.ccs.thaparbitesshop.ui.orderdetail.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ccs.thaparbitesshop.ui.orderdetail.OrderItemUi

@Composable
fun OrderItemRow(
    item: OrderItemUi
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement =
            Arrangement.SpaceBetween
    ) {

        Column {

            Text(item.name)

            Text(
                "Qty: ${item.quantity}"
            )
        }

        Text("₹${item.price}")
    }
}