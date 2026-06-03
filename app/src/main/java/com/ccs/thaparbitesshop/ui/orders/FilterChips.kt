package com.ccs.thaparbitesshop.ui.orders

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun FilterChips(
    selected: String,
    onSelected: (String) -> Unit
) {

    val filters = listOf(
        "ALL",
        "NEW",
        "PREPARING",
        "READY",
        "COMPLETED"
    )

    Row(
        modifier = Modifier
            .horizontalScroll(
                rememberScrollState()
            )
    ) {

        filters.forEach { filter ->

            FilterChip(
                selected = selected == filter,
                onClick = {
                    onSelected(filter)
                },
                label = {
                    Text(filter)
                }
            )
        }
    }
}