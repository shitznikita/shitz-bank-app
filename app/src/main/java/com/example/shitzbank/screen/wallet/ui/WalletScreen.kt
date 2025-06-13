package com.example.shitzbank.screen.wallet.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.example.shitzbank.ui.common.CommonLazyColumn
import com.example.shitzbank.ui.common.CommonListItem
import com.example.shitzbank.ui.common.ResultStateHandler
import com.example.shitzbank.ui.theme.LightGreen
import com.example.shitzbank.R
import com.example.shitzbank.domain.model.Account
import com.example.shitzbank.ui.common.LeadIcon
import com.example.shitzbank.ui.common.PriceDisplay
import com.example.shitzbank.ui.common.TrailingContent
import com.example.shitzbank.ui.viewmodel.MainViewModel

@Composable
fun WalletScreen(viewModel: MainViewModel) {
    val mock by viewModel.accounts.collectAsState()

    ResultStateHandler(
        state = mock,
        onSuccess = { data ->
            CommonLazyColumn(
                itemsList = data,
                itemTemplate = { item ->
                    BalanceWalletListItem(item)
                    CurrencyWalletListItem(item)
                }
            )
        }
    )
}

@Composable
fun BalanceWalletListItem(item: Account) {
    WalletListItem(
        lead = { LeadIcon(label = "💰") },
        content = { Text(stringResource(R.string.balance)) },
        trail = {
            TrailingContent(
                content = {
                    PriceDisplay(
                        amount = item.balance,
                        currencySymbol = item.currency
                    )
                },
                icon = {
                    Icon(ImageVector.vectorResource(R.drawable.drill_in), null)
                }
            )
        }
    )
}

@Composable
fun CurrencyWalletListItem(item: Account) {
    WalletListItem(
        content = { Text(stringResource(R.string.currency),) },
        trail = {
            TrailingContent(
                content = {
                    Text(
                        item.currency,
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                icon = {
                    Icon(ImageVector.vectorResource(R.drawable.drill_in), null)
                }
            )
        }
    )
}

@Composable
fun WalletListItem(
    lead: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit,
    trail: (@Composable () -> Unit)
) {
    CommonListItem(
        modifier = Modifier.background(LightGreen)
            .clickable {  },
        lead = lead,
        content = content,
        trail = trail,
        backgroundColor = LightGreen
    )
}