package com.example.shitzbank.screen.incomes.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shitzbank.screen.wallet.ui.WalletViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.shitzbank.ui.common.CommonLazyColumn
import com.example.shitzbank.ui.common.CommonListItem
import com.example.shitzbank.ui.common.ResultStateHandler
import com.example.shitzbank.ui.theme.LightGreen
import com.example.shitzbank.R
import com.example.shitzbank.ui.common.PriceDisplay
import com.example.shitzbank.ui.common.TrailingContent

@Composable
fun IncomesScreen(viewModel: WalletViewModel = viewModel()) {
    val mock by viewModel.incomeTransactions.collectAsState()
    val totalAmount by viewModel.totalIncome.collectAsState()

    ResultStateHandler(
        state = mock,
        onSuccess = { data ->
            CommonLazyColumn(
                topItem = {
                    CommonListItem(
                        backgroundColor = LightGreen,
                        content = { Text(stringResource(R.string.in_total)) },
                        trail = {
                            PriceDisplay(
                                amount = totalAmount,
                                currencySymbol = "₽"
                            )
                        }
                    )
                },
                itemsList = data,
                itemTemplate = { item ->
                    CommonListItem(
                        modifier = Modifier.height(68.dp)
                            .clickable {  },
                        content = { Text(item.title) },
                        trail = {
                            TrailingContent(
                                content = {
                                    PriceDisplay(
                                        amount = item.amount,
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
            )
        }
    )
}