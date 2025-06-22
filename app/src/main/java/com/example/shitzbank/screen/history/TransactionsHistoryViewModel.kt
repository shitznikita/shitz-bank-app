package com.example.shitzbank.screen.history

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shitzbank.common.ResultState
import com.example.shitzbank.common.network.ConnectionStatus
import com.example.shitzbank.common.network.NetworkMonitor
import com.example.shitzbank.domain.model.TransactionResponse
import com.example.shitzbank.domain.usecase.GetDefaultAccountIdUseCase
import com.example.shitzbank.domain.usecase.GetTransactionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class TransactionsHistoryViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getDefaultAccountIdUseCase: GetDefaultAccountIdUseCase,
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {
    private val isIncome: Boolean = savedStateHandle.get<Boolean>("isIncome") ?: false

    private val _startDate = MutableStateFlow(LocalDate.now().withDayOfMonth(1))
    val startDate: StateFlow<LocalDate> = _startDate.asStateFlow()

    private val _endDate = MutableStateFlow(LocalDate.now())
    val endDate: StateFlow<LocalDate> = _endDate.asStateFlow()

    private val _transactionsState = MutableStateFlow<ResultState<List<TransactionResponse>>>(ResultState.Loading)
    val transactionsState: StateFlow<ResultState<List<TransactionResponse>>> = _transactionsState.asStateFlow()

    private val _total = MutableStateFlow(0.0)
    val total = _total.asStateFlow()

    private val _networkStatus = MutableStateFlow<ConnectionStatus>(ConnectionStatus.Unavailable)
    val networkStatus: StateFlow<ConnectionStatus> = _networkStatus.asStateFlow()

    private val _dateError = MutableStateFlow<String?>(null)
    val dateError: StateFlow<String?> = _dateError.asStateFlow()

    init {
        viewModelScope.launch {
            networkMonitor.connectionStatus.collect { status ->
                _networkStatus.value = status
                if (status is ConnectionStatus.Available) {
                    loadTransactions()
                }
            }
        }
    }

    fun setStartDate(date: LocalDate) {
        _startDate.value = date
        loadTransactions()
    }

    fun setEndDate(date: LocalDate) {
        _endDate.value = date
        loadTransactions()
    }

    private fun loadTransactions() {
        viewModelScope.launch {
            if (_networkStatus.value is ConnectionStatus.Unavailable) {
                return@launch
            }

            _transactionsState.value = ResultState.Loading
            _total.value = 0.0

            try {
                val accountId = getDefaultAccountIdUseCase.execute()

                val apiFormatter = DateTimeFormatter.ISO_LOCAL_DATE
                val start = _startDate.value.format(apiFormatter)
                val end = _endDate.value.format(apiFormatter)

                val transactions = getTransactionsUseCase.execute(accountId!!, start, end).filter {
                    it.category.isIncome == isIncome
                }
                _transactionsState.value = ResultState.Success(transactions)

                if (transactions.isEmpty()) {
                    _total.value = 0.0
                } else {
                    calculateTotalExpense(transactions)
                }
            } catch (e: Exception) {
                _transactionsState.value = ResultState.Error("Error: ${e.message}")
            }
        }
    }

    private fun calculateTotalExpense(expenses: List<TransactionResponse>) {
        val sum = expenses.sumOf { it.amount }
        _total.value = sum
    }

}