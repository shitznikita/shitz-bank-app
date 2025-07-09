package com.example.shitzbank.ui.screen.account.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.shitzbank.R
import com.example.shitzbank.common.utils.currency.Currency
import com.example.shitzbank.ui.common.composable.CommonLazyColumn
import com.example.shitzbank.ui.common.composable.CommonListItem
import com.example.shitzbank.ui.common.composable.CommonText

@Composable
fun CurrencySelectionBottomSheet(
    onCurrencySelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val currencyList = Currency.getAllCurrencies()

    Column {
        CommonLazyColumn(
            itemsList = currencyList,
            itemTemplate = { item ->
                CommonListItem(
                    modifier = Modifier.height(68.dp)
                        .clickable { onCurrencySelected(item.code) },
                    lead = {
                        Icon(
                            imageVector = ImageVector.vectorResource(item.iconResId),
                            contentDescription = item.fullName
                        )
                    },
                    content = {
                        CommonText(
                            text = item.fullName + " " + item.symbol,
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                )
            }
        )

        CommonListItem(
            modifier = Modifier.clickable(onClick = onDismiss),
            lead = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_cancel),
                    contentDescription = stringResource(R.string.cancel),
                    tint = MaterialTheme.colorScheme.background
                )
            },
            content = {
                CommonText(
                    text = stringResource(R.string.cancel),
                    color = MaterialTheme.colorScheme.background,
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            backgroundColor = MaterialTheme.colorScheme.error
        )
    }
}