package com.example.shitzbank.ui.screen.transaction.common.sheet

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.example.shitzbank.domain.model.AccountBrief
import com.example.shitzbank.ui.common.composable.CommonListItem
import com.example.shitzbank.ui.common.composable.CommonText
import com.example.shitzbank.ui.common.composable.SelectionBottomSheet

@Composable
fun AccountSelectionBottomSheet(
    accounts: List<AccountBrief>,
    onAccountSelected: (AccountBrief) -> Unit,
    onDismiss: () -> Unit
) {
    SelectionBottomSheet(
        itemsList = accounts,
        itemContent = { account, modifier ->
            CommonListItem(
                modifier = modifier,
                content = {
                    CommonText(
                        text = account.name,
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            )
        },
        onItemClick = onAccountSelected,
        onDismiss = onDismiss
    )
}