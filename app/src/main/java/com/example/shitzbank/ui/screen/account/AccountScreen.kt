package com.example.shitzbank.ui.screen.account

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.shitzbank.ui.common.composable.CommonLazyColumn
import com.example.shitzbank.ui.common.composable.ResultStateHandler
import com.example.shitzbank.ui.screen.account.common.AccountNameListItem
import com.example.shitzbank.ui.screen.account.common.CurrencyAccountListItem
import com.example.shitzbank.ui.screen.account.common.CurrencySelectionBottomSheet
import com.example.shitzbank.ui.screen.account.common.EditAccountNameDialog
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(viewModel: AccountViewModel = hiltViewModel()) {
    val state by viewModel.accountState.collectAsState()
    val editableAccountName by viewModel.editableAccountName.collectAsState()
    val showEditDialog by viewModel.showEditNameDialog.collectAsState()
    val showCurrencyBottomSheet by viewModel.showCurrencyBottomSheet.collectAsState()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.loadAccount()
    }

    ResultStateHandler(
        state = state,
        onSuccess = { data ->
            CommonLazyColumn(
                itemsList = listOf(data),
                itemTemplate = { item ->
                    AccountNameListItem(item) {
                        viewModel.showEditNameDialog(true)
                    }
                    CurrencyAccountListItem(item) {
                        scope.launch { sheetState.show() }
                        viewModel.showCurrencyBottomSheet(true)
                    }
                },
            )
        },
    )

    if (showEditDialog) {
        EditAccountNameDialog(
            currentName = editableAccountName,
            onNameChange = viewModel::onAccountNameChanged,
            onDismiss = { viewModel.showEditNameDialog(false) },
            onSave = { viewModel.saveNameChanges() }
        )
    }

    if (showCurrencyBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                scope.launch { sheetState.hide() }
                viewModel.showCurrencyBottomSheet(false)
            },
            sheetState = sheetState,
            modifier = Modifier.fillMaxWidth()
        ) {
            CurrencySelectionBottomSheet(
                onCurrencySelected = { selectedCurrency ->
                    viewModel.saveCurrencyChanges(selectedCurrency)
                    scope.launch { sheetState.hide() }
                    viewModel.showCurrencyBottomSheet(false)
                },
                onDismiss = {
                    scope.launch { sheetState.hide() }
                    viewModel.showCurrencyBottomSheet(false)
                }
            )
        }
    }
}
