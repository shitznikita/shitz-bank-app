package com.example.shitzbank.ui.screen.account

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.shitzbank.R
import com.example.shitzbank.common.utils.getCurrencySymbol
import com.example.shitzbank.domain.model.Account
import com.example.shitzbank.ui.common.CommonLazyColumn
import com.example.shitzbank.ui.common.CommonListItem
import com.example.shitzbank.ui.common.CommonText
import com.example.shitzbank.ui.common.LeadIcon
import com.example.shitzbank.ui.common.PriceDisplay
import com.example.shitzbank.ui.common.ResultStateHandler
import com.example.shitzbank.ui.common.TrailingContent

@Composable
fun AccountScreen(viewModel: AccountViewModel = hiltViewModel()) {
    val state by viewModel.accountState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadAccount()
    }

    ResultStateHandler(
        state = state,
        onSuccess = { data ->
            CommonLazyColumn(
                itemsList = listOf(data),
                itemTemplate = { item ->
                    BalanceAccountListItem(item)
                    CurrencyAccountListItem(item)
                },
            )
        },
    )
}

@Composable
fun BalanceAccountListItem(item: Account) {
    AccountListItem(
        lead = { LeadIcon(label = "💰") },
        content = {
            CommonText(
                text = stringResource(R.string.balance),
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.bodyLarge,
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
                },
            )
        },
    )
}

@Composable
fun CurrencyAccountListItem(item: Account) {
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
                        text = getCurrencySymbol(item.currency),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                },
                icon = {
                    Icon(ImageVector.vectorResource(R.drawable.drill_in), null)
                },
            )
        },
    )
}

@Composable
fun AccountListItem(
    lead: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit,
    trail: (@Composable () -> Unit),
) {
    CommonListItem(
        modifier =
            Modifier.background(MaterialTheme.colorScheme.secondary)
                .clickable { },
        lead = lead,
        content = content,
        trail = trail,
        backgroundColor = MaterialTheme.colorScheme.secondary,
    )
}
