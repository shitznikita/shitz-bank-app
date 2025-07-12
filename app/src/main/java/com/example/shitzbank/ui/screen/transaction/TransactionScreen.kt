package com.example.shitzbank.ui.screen.transaction

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.shitzbank.ui.navigation.utils.ActionConfig
import com.example.shitzbank.R
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.shitzbank.ui.common.composable.ResultStateHandler
import com.example.shitzbank.ui.screen.transaction.common.DeleteButton
import com.example.shitzbank.ui.screen.transaction.common.DeleteConfirmationDialog
import com.example.shitzbank.ui.screen.transaction.common.EditAmountDialog
import com.example.shitzbank.ui.screen.transaction.common.EditDescriptionDialog
import com.example.shitzbank.ui.screen.transaction.common.TransactionDatePicker
import com.example.shitzbank.ui.screen.transaction.common.TransactionTimePicker
import com.example.shitzbank.ui.screen.transaction.common.sheet.AccountSelectionBottomSheet
import com.example.shitzbank.ui.screen.transaction.common.selector.AccountSelector
import com.example.shitzbank.ui.screen.transaction.common.selector.AmountSelector
import com.example.shitzbank.ui.screen.transaction.common.selector.CategorySelector
import com.example.shitzbank.ui.screen.transaction.common.selector.DateSelector
import com.example.shitzbank.ui.screen.transaction.common.selector.DescriptionSelector
import com.example.shitzbank.ui.screen.transaction.common.selector.TimeSelector
import com.example.shitzbank.ui.screen.transaction.common.sheet.BottomSheetType
import com.example.shitzbank.ui.screen.transaction.common.sheet.CategorySelectionBottomSheet
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionScreen(
    navController: NavController,
    onSetTopBarAction: (ActionConfig?) -> Unit,
    viewModel: TransactionViewModel = hiltViewModel()
) {

    val saveAndNavigate: () -> Unit = {
        viewModel.saveTransaction {
            navController.previousBackStackEntry?.savedStateHandle?.set("refresh_needed", true)
            navController.navigateUp()
        }
    }
    val icon = ImageVector.vectorResource(R.drawable.ic_done)
    val saveActionConfig = ActionConfig(
        onClick = saveAndNavigate,
        icon = icon
    )

    LaunchedEffect(Unit) {
        onSetTopBarAction(saveActionConfig)
    }

    val transactionState by viewModel.transactionState.collectAsState()
    val accountsState by viewModel.accountsState.collectAsState()
    val categoriesState by viewModel.categoriesState.collectAsState()

    val currentBottomSheetType by viewModel.currentBottomSheetType.collectAsState()
    val editableAmount by viewModel.editableAmount.collectAsState()
    val showEditAmountDialog by viewModel.showEditAmountDialog.collectAsState()
    val showDatePicker by viewModel.showDatePicker.collectAsState()
    val showTimePicker by viewModel.showTimePicker.collectAsState()
    val editableComment by viewModel.editableComment.collectAsState()
    val showEditCommentDialog by viewModel.showEditCommentDialog.collectAsState()
    val showDeleteConfirmationDialog by viewModel.showDeleteConfirmationDialog.collectAsState()

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(currentBottomSheetType) {
        if (currentBottomSheetType != BottomSheetType.NONE) {
            scope.launch { sheetState.show() }
        } else {
            scope.launch { sheetState.hide() }
        }
    }

    ResultStateHandler(
        state = transactionState,
        onSuccess = { data ->
            TransactionContent(
                uiState = data,
                onShowAccountSelectionSheet = viewModel::showAccountSelectionSheet,
                onShowCategorySelectionSheet = viewModel::showCategorySelectionSheet,
                onShowEditAmountDialog = viewModel::showEditAmountDialog,
                onShowDatePicker = viewModel::showDatePicker,
                onShowTimePicker = viewModel::showTimePicker,
                onShowEditCommentDialog = viewModel::showEditCommentDialog,
                onShowDeleteConfirmationDialog = viewModel::showDeleteConfirmationDialog,
                isNewTransaction = viewModel.isNewTransaction
            )

            if (currentBottomSheetType != BottomSheetType.NONE) {
                ModalBottomSheet(
                    modifier = Modifier.fillMaxWidth()
                        .wrapContentHeight(),
                    onDismissRequest = {
                        scope.launch { sheetState.hide() }
                        viewModel.dismissBottomSheet()
                    },
                    sheetState = sheetState
                ) {
                    when (currentBottomSheetType) {
                        BottomSheetType.ACCOUNT -> {
                            ResultStateHandler(
                                state = accountsState,
                                onSuccess = { accounts ->
                                    AccountSelectionBottomSheet(
                                        accounts = accounts.map { it.toBrief() },
                                        onAccountSelected = viewModel::onAccountSelected,
                                        onDismiss = viewModel::dismissBottomSheet
                                    )
                                }
                            )
                        }
                        BottomSheetType.CATEGORY -> {
                            ResultStateHandler(
                                state = categoriesState,
                                onSuccess = { categories ->
                                    CategorySelectionBottomSheet(
                                        categories = categories,
                                        onCategorySelected = viewModel::onCategorySelected,
                                        onDismiss = viewModel::dismissBottomSheet
                                    )
                                }
                            )
                        }
                        else -> {}
                    }
                }
            }

            if (showEditAmountDialog) {
                EditAmountDialog(
                    currentAmount = editableAmount,
                    onAmountChange = viewModel::onAmountChanged,
                    onDismiss = { viewModel.showEditAmountDialog(false) },
                    onSave = { viewModel.saveAmountChanges() }
                )
            }

            if (showDatePicker) {
                TransactionDatePicker(
                    currentDate = data.date,
                    onDateSelected = viewModel::onDateSelected,
                    onDismiss = { viewModel.showDatePicker(false) }
                )
            }

            if (showTimePicker) {
                TransactionTimePicker(
                    currentTime = data.time,
                    onTimeSelected = viewModel::onTimeSelected,
                    onDismiss = { viewModel.showTimePicker(false) }
                )
            }

            if (showEditCommentDialog) {
                EditDescriptionDialog(
                    currentComment = editableComment,
                    onCommentChange = viewModel::onCommentChanged,
                    onDismiss = { viewModel.showEditCommentDialog(false) },
                    onSave = { viewModel.saveCommentChanges() }
                )
            }

            if (showDeleteConfirmationDialog) {
                DeleteConfirmationDialog(
                    onConfirm = {
                        viewModel.deleteTransaction {
                            navController.previousBackStackEntry?.savedStateHandle?.set("refresh_needed", true)
                            navController.navigateUp()
                        }
                    },
                    onDismiss = { viewModel.showDeleteConfirmationDialog(false) }
                )
            }
        }
    )
}

@Composable
fun TransactionContent(
    uiState: TransactionUiState,
    onShowAccountSelectionSheet: () -> Unit,
    onShowCategorySelectionSheet: () -> Unit,
    onShowEditAmountDialog: (Boolean) -> Unit,
    onShowDatePicker: (Boolean) -> Unit,
    onShowTimePicker: (Boolean) -> Unit,
    onShowEditCommentDialog: (Boolean) -> Unit,
    onShowDeleteConfirmationDialog: (Boolean) -> Unit,
    isNewTransaction: Boolean
) {
    Column {
        AccountSelector(
            selectedAccount = uiState.account,
            onClick = onShowAccountSelectionSheet
        )

        CategorySelector(
            selectedCategory = uiState.category,
            onClick = onShowCategorySelectionSheet
        )

        AmountSelector(
            currentAmount = uiState.amount ?: "",
            currency = uiState.account?.currency ?: "",
            onClick = { onShowEditAmountDialog(true) }
        )

        DateSelector(
            selectedDate = uiState.date,
            onClick = { onShowDatePicker(true) }
        )

        TimeSelector(
            selectedTime = uiState.time,
            onClick = { onShowTimePicker(true) }
        )

        DescriptionSelector(
            currentComment = uiState.comment,
            onClick = { onShowEditCommentDialog(true) }
        )

        if (!isNewTransaction) {
            Spacer(Modifier.height(16.dp))
            DeleteButton(
                onClick = { onShowDeleteConfirmationDialog(true) }
            )
        }
    }
}