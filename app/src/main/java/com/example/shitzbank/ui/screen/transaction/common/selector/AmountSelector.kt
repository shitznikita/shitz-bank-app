package com.example.shitzbank.ui.screen.transaction.common.selector

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.shitzbank.ui.common.composable.CommonText
import com.example.shitzbank.ui.screen.transaction.common.SelectorListItem
import com.example.shitzbank.R
import com.example.shitzbank.ui.common.trail.AmountTrailingContent

@Composable
fun AmountSelector(
    currentAmount: String,
    currency: String,
    onClick: () -> Unit
) {
    SelectorListItem(
        content = {
            CommonText(
                text = stringResource(R.string.amount),
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.bodyLarge
            )
        },
        trail = {
            AmountTrailingContent(
                amount = currentAmount.toDoubleOrNull()!!,
                currency = currency
            )
        },
        onClick = onClick
    )
}