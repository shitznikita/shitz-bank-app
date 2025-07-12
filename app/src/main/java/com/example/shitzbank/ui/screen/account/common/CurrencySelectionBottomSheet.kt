package com.example.shitzbank.ui.screen.account.common

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.example.shitzbank.common.utils.currency.Currency
import com.example.shitzbank.ui.common.composable.CommonListItem
import com.example.shitzbank.ui.common.composable.CommonText
import com.example.shitzbank.ui.common.composable.SelectionBottomSheet

@Composable
fun CurrencySelectionBottomSheet(
    currencyList: List<Currency> = Currency.getAllCurrencies(),
    onCurrencySelected: (Currency) -> Unit,
    onDismiss: () -> Unit
) {
    SelectionBottomSheet(
        itemsList = currencyList,
        itemContent = { currency, modifier ->
            CommonListItem(
                modifier = modifier,
                lead = {
                    Icon(
                        imageVector = ImageVector.vectorResource(currency.iconResId),
                        contentDescription = currency.fullName
                    )
                },
                content = {
                    CommonText(
                        text = currency.fullName + " " + currency.symbol,
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            )
        },
        onItemClick = onCurrencySelected,
        onDismiss = onDismiss
    )
}