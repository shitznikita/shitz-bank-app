package com.example.shitzbank.ui.screen.account.common

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.example.shitzbank.R
import com.example.shitzbank.domain.model.Account
import com.example.shitzbank.ui.common.composable.CommonText
import com.example.shitzbank.ui.common.composable.LeadIcon
import com.example.shitzbank.ui.common.composable.PriceDisplay
import com.example.shitzbank.ui.common.composable.TrailingContent

@Composable
fun AccountNameListItem(
    item: Account,
    onClick: () -> Unit
) {
    AccountListItem(
        lead = { LeadIcon(label = "💰") },
        content = {
            CommonText(
                text = if (item.name.isNotBlank()) item.name
                else stringResource(R.string.balance),
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.bodyLarge
            )
        },
        trail = {
            TrailingContent(
                content = {
                    PriceDisplay(
                        amount = item.balance,
                        currency = item.currency,
                    )
                },
                icon = {
                    Icon(ImageVector.vectorResource(R.drawable.drill_in), null)
                }
            )
        },
        onClick = onClick
    )
}