package com.example.shitzbank.ui.screen.account

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.window.DialogProperties
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
    val editableAccountName by viewModel.editableAccountName.collectAsState()
    val showEditDialog by viewModel.showEditDialog.collectAsState()

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
                    AccountNameListItem(item) { viewModel.showEditAccountDialog(true) }
                },
            )
        },
    )

    if (showEditDialog) {
        EditAccountNameDialog(
            currentName = editableAccountName,
            onNameChange = viewModel::onAccountNameChanged,
            onDismiss = { viewModel.showEditAccountDialog(false) },
            onSave = { viewModel.saveAccountChanges() }
        )
    }
}

@Composable
fun AccountNameListItem(
    item: Account,
    onClick: () -> Unit
) {
    AccountListItem(
        lead = { LeadIcon(label = "💰") },
        content = {
            CommonText(
                text = item.name,
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

@Composable
fun EditAccountNameDialog(
    currentName: String,
    onNameChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onSave: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            CommonText(
                text = "hello",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.bodyLarge
            )
        },
        text = {
            OutlinedTextField(
                value = currentName,
                onValueChange = onNameChange,
                label = {
                    CommonText(
                        text = "account name",
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                singleLine = true
            )
        },
        confirmButton = {
            Button (onClick = onSave) {
                CommonText(
                    text = "Save",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                CommonText(
                    text = "Отмена",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
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
                }
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
    onClick: () -> Unit = {}
) {
    CommonListItem(
        modifier =
            Modifier.background(MaterialTheme.colorScheme.secondary)
                .clickable(onClick = onClick),
        lead = lead,
        content = content,
        trail = trail,
        backgroundColor = MaterialTheme.colorScheme.secondary,
    )
}
