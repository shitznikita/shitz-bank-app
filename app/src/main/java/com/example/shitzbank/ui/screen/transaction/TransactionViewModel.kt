package com.example.shitzbank.ui.screen.transaction

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.shitzbank.common.ResultState
import com.example.shitzbank.common.network.ConnectionStatus
import com.example.shitzbank.common.network.NetworkMonitor
import com.example.shitzbank.common.network.NetworkMonitorViewModel
import com.example.shitzbank.domain.model.Account
import com.example.shitzbank.domain.model.AccountBrief
import com.example.shitzbank.domain.model.Category
import com.example.shitzbank.domain.model.TransactionRequest
import com.example.shitzbank.domain.usecase.account.GetAllAccountsUseCase
import com.example.shitzbank.domain.usecase.categories.GetCategoriesByTypeUseCase
import com.example.shitzbank.domain.usecase.transactions.CreateTransactionUseCase
import com.example.shitzbank.domain.usecase.transactions.DeleteTransactionByIdUseCase
import com.example.shitzbank.domain.usecase.transactions.GetTransactionByIdUseCase
import com.example.shitzbank.domain.usecase.transactions.UpdateTransactionByIdUseCase
import com.example.shitzbank.ui.screen.transaction.common.sheet.BottomSheetType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getTransactionByIdUseCase: GetTransactionByIdUseCase,
    private val getAllAccountsUseCase: GetAllAccountsUseCase,
    private val getCategoriesByTypeUseCase: GetCategoriesByTypeUseCase,
    private val deleteTransactionByIdUseCase: DeleteTransactionByIdUseCase,
    private val createTransactionUseCase: CreateTransactionUseCase,
    private val updateTransactionByIdUseCase: UpdateTransactionByIdUseCase,
    networkMonitor: NetworkMonitor
) : NetworkMonitorViewModel(networkMonitor) {
    private val isIncome: Boolean = savedStateHandle.get<Boolean>("isIncome") ?: false
    val isNewTransaction: Boolean = savedStateHandle.get<Boolean>("isNew") ?: false
    private val transactionId: Int = savedStateHandle.get<Int>("id") ?: -1

    private val _transactionState = MutableStateFlow<ResultState<TransactionUiState>>(ResultState.Loading)
    val transactionState: StateFlow<ResultState<TransactionUiState>> = _transactionState.asStateFlow()

    private val _accountsState = MutableStateFlow<ResultState<List<Account>>>(ResultState.Loading)
    val accountsState: StateFlow<ResultState<List<Account>>> = _accountsState.asStateFlow()

    private val _categoriesState = MutableStateFlow<ResultState<List<Category>>>(ResultState.Loading)
    val categoriesState: StateFlow<ResultState<List<Category>>> = _categoriesState.asStateFlow()

    private val _currentBottomSheetType = MutableStateFlow(BottomSheetType.NONE)
    val currentBottomSheetType: StateFlow<BottomSheetType> = _currentBottomSheetType.asStateFlow()

    private val _editableAmount = MutableStateFlow("")
    val editableAmount: StateFlow<String> = _editableAmount.asStateFlow()

    private val _showEditAmountDialog = MutableStateFlow(false)
    val showEditAmountDialog: StateFlow<Boolean> = _showEditAmountDialog.asStateFlow()

    private val _showDatePicker = MutableStateFlow(false)
    val showDatePicker: StateFlow<Boolean> = _showDatePicker.asStateFlow()

    private val _showTimePicker = MutableStateFlow(false)
    val showTimePicker: StateFlow<Boolean> = _showTimePicker.asStateFlow()

    private val _editableComment = MutableStateFlow("")
    val editableComment: StateFlow<String> = _editableComment.asStateFlow()

    private val _showEditCommentDialog = MutableStateFlow(false)
    val showEditCommentDialog: StateFlow<Boolean> = _showEditCommentDialog.asStateFlow()

    private val _showDeleteConfirmationDialog = MutableStateFlow(false)
    val showDeleteConfirmationDialog: StateFlow<Boolean> = _showDeleteConfirmationDialog.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        viewModelScope.launch {
            networkStatus.collect { status ->
                if (status is ConnectionStatus.Available) {
                    loadData()
                }
            }
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            if (networkStatus.value is ConnectionStatus.Unavailable) {
                return@launch
            }

            val accountsLoad = async { loadAccounts() }
            val categoriesLoad = async { loadCategories() }
            accountsLoad.await()
            categoriesLoad.await()

            loadTransaction()
        }
    }

    private suspend fun loadAccounts() {
        _accountsState.value = ResultState.Loading

        val accountsList = getAllAccountsUseCase.execute()
        _accountsState.value = ResultState.Success(accountsList)
    }

    private suspend fun loadCategories() {
        _categoriesState.value = ResultState.Loading

        val categoriesList = getCategoriesByTypeUseCase.execute(isIncome)
        _categoriesState.value = ResultState.Success(categoriesList)
    }

    private suspend fun loadTransaction() {
        _transactionState.value = ResultState.Loading

        if (isNewTransaction) {
            _transactionState.value = ResultState.Success(
                TransactionUiState(
                    account = (accountsState.value as? ResultState.Success)?.data?.firstOrNull()?.toBrief(),
                    category = (categoriesState.value as? ResultState.Success)?.data?.firstOrNull(),
                    amount = "",
                    isIncome = isIncome
                )
            )
            _editableAmount.value = ""
            _editableComment.value = ""
        } else {
            val transaction = getTransactionByIdUseCase.execute(transactionId)
            _transactionState.value = ResultState.Success(
                TransactionUiState(
                    id = transaction.id,
                    account = transaction.account,
                    category = transaction.category,
                    amount = transaction.amount.toString(),
                    date = transaction.transactionDate.toLocalDate(),
                    time = transaction.transactionDate.toLocalTime(),
                    comment = transaction.comment,
                    isIncome = isIncome,
                    isNewTransaction = isNewTransaction
                )
            )
            _editableAmount.value = transaction.amount.toString()
            _editableComment.value = transaction.comment ?: ""
        }
    }

    private fun updateUiState(updater: (TransactionUiState) -> TransactionUiState) {
        _transactionState.value.let { currentState ->
            if (currentState is ResultState.Success) {
                _transactionState.value = ResultState.Success(updater(currentState.data))
            }
        }
    }

    fun showAccountSelectionSheet() {
        _currentBottomSheetType.value = BottomSheetType.ACCOUNT
    }

    fun showCategorySelectionSheet() {
        _currentBottomSheetType.value = BottomSheetType.CATEGORY
    }

    fun dismissBottomSheet() {
        _currentBottomSheetType.value = BottomSheetType.NONE
    }

    fun onAccountSelected(account: AccountBrief) {
        updateUiState {
            it.copy(account = account)
        }
        dismissBottomSheet()
    }

    fun onCategorySelected(category: Category) {
        updateUiState {
            it.copy(category = category)
        }
        dismissBottomSheet()
    }

    fun showEditAmountDialog(show: Boolean) {
        _showEditAmountDialog.value = show
        if (show) {
            val currentAmount = (_transactionState.value as? ResultState.Success)?.data?.amount ?: ""
            _editableAmount.value = currentAmount
        }
    }

    fun onAmountChanged(newAmount: String) {
        _editableAmount.value = newAmount
    }

    fun saveAmountChanges() {
        viewModelScope.launch {
            val amountDouble = _editableAmount.value
                .replace(',', '.')
                .toDoubleOrNull()

            if (amountDouble == null || amountDouble <= 0) {
                return@launch
            }

            updateUiState { it.copy(amount = amountDouble.toString()) }
            _showEditAmountDialog.value = false
        }
    }

    fun showDatePicker(show: Boolean) {
        _showDatePicker.value = show
    }

    fun onDateSelected(newDate: LocalDate) {
        updateUiState { it.copy(date = newDate) }
        _showDatePicker.value = false
    }

    fun showTimePicker(show: Boolean) {
        _showTimePicker.value = show
    }

    fun onTimeSelected(newTime: LocalTime) {
        updateUiState { it.copy(time = newTime) }
        _showTimePicker.value = false
    }

    fun showEditCommentDialog(show: Boolean) {
        _showEditCommentDialog.value = show
        if (show) {
            val currentComment = (_transactionState.value as? ResultState.Success)?.data?.comment ?: ""
            _editableComment.value = currentComment
        }
    }

    fun onCommentChanged(newComment: String) {
        _editableComment.value = newComment
    }

    fun saveCommentChanges() {
        viewModelScope.launch {
            updateUiState { it.copy(comment = _editableComment.value.ifBlank { null }) }
            _showEditCommentDialog.value = false
        }
    }

    fun showDeleteConfirmationDialog(show: Boolean) {
        _showDeleteConfirmationDialog.value = show
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    fun deleteTransaction(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _showDeleteConfirmationDialog.value = false
            val currentTransactionData = (_transactionState.value as? ResultState.Success)?.data

            val transactionId = currentTransactionData?.id
            if (transactionId != null) {
                val success = deleteTransactionByIdUseCase.execute(transactionId)
                if (success) {
                    onSuccess()
                }
            }
        }
    }

    fun saveTransaction(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val currentTransactionData = (_transactionState.value as? ResultState.Success)?.data
            if (currentTransactionData == null) {
                return@launch
            }

            if (currentTransactionData.amount.isNullOrBlank() ||
                (currentTransactionData.amount.replace(',', '.').toDoubleOrNull() ?: 0.0) <= 0)
            {
                _errorMessage.value = "Укажите сумму"
                return@launch
            }

            val request = TransactionRequest(
                accountId = currentTransactionData.account!!.id,
                categoryId = currentTransactionData.category!!.id,
                amount = currentTransactionData.amount!!.replace(',', '.').toDouble(),
                transactionDate = LocalDateTime.of(
                    currentTransactionData.date,
                    currentTransactionData.time
                ),
                comment = currentTransactionData.comment
            )

            val success = if (isNewTransaction) {
                createTransactionUseCase.execute(request) != null
            } else {
                updateTransactionByIdUseCase.execute(transactionId, request) != null
            }

            if (success) {
                onSuccess()
            }
        }
    }
}



