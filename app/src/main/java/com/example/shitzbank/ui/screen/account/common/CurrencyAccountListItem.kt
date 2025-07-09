package com.example.shitzbank.ui.screen.account.common

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.example.shitzbank.R
import com.example.shitzbank.common.utils.currency.Currency
import com.example.shitzbank.domain.model.Account
import com.example.shitzbank.ui.common.composable.CommonText
import com.example.shitzbank.ui.common.composable.TrailingContent

@Composable
fun CurrencyAccountListItem(
    item: Account,
    onClick: () -> Unit
) {
    AccountListItem(
        content = {
            CommonText(
                text = stringResource(R.string.currency),
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.bodyLarge,
            )
        },
        trail = {
            TrailingContent(
                content = {
                    CommonText(
                        text = Currency.fromCode(item.currency),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                },
                icon = {
                    Icon(ImageVector.vectorResource(R.drawable.drill_in), null)
                },
            )
        },
        onClick = onClick
    )
}