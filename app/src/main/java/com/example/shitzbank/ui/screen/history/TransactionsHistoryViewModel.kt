package com.example.shitzbank.ui.screen.history

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.shitzbank.common.ResultState
import com.example.shitzbank.common.network.ConnectionStatus
import com.example.shitzbank.common.network.NetworkMonitor
import com.example.shitzbank.common.network.NetworkMonitorViewModel
import com.example.shitzbank.domain.model.TransactionResponse
import com.example.shitzbank.domain.usecase.account.GetDefaultAccountUseCase
import com.example.shitzbank.domain.usecase.transactions.GetTransactionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class TransactionsHistoryViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        private val getDefaultAccountUseCase: GetDefaultAccountUseCase,
        private val getTransactionsUseCase: GetTransactionsUseCase,
        networkMonitor: NetworkMonitor,
    ) : NetworkMonitorViewModel(networkMonitor) {
        private val isIncome: Boolean = savedStateHandle.get<Boolean>("isIncome") ?: false

        private val _startDate = MutableStateFlow(LocalDate.now().withDayOfMonth(1))
        val startDate: StateFlow<LocalDate> = _startDate.asStateFlow()

        private val _endDate = MutableStateFlow(LocalDate.now())
        val endDate: StateFlow<LocalDate> = _endDate.asStateFlow()

        private val _transactionsState = MutableStateFlow<ResultState<List<TransactionResponse>>>(ResultState.Loading)
        val transactionsState: StateFlow<ResultState<List<TransactionResponse>>> = _transactionsState.asStateFlow()

        private val DEFAULT_TOTAL_VALUE = 0.0
        private val _total = MutableStateFlow(DEFAULT_TOTAL_VALUE)
        val total = _total.asStateFlow()

        private val _accountCurrency = MutableStateFlow("")
        val accountCurrency: StateFlow<String> = _accountCurrency.asStateFlow()

        private val _dateError = MutableStateFlow<String?>(null)
        val dateError: StateFlow<String?> = _dateError.asStateFlow()

        init {
            viewModelScope.launch {
//                networkStatus.collect { status ->
//                    if (status is ConnectionStatus.Available) {
//                        loadTransactions()
//                    }
//                }
                loadTransactions()
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

        fun loadTransactions() {
            viewModelScope.launch {
//                if (networkStatus.value is ConnectionStatus.Unavailable) {
//                    return@launch
//                }

                _transactionsState.value = ResultState.Loading
                _total.value = DEFAULT_TOTAL_VALUE

                val account = getDefaultAccountUseCase.execute()

                val apiFormatter = DateTimeFormatter.ISO_LOCAL_DATE
                val start = _startDate.value.format(apiFormatter)
                val end = _endDate.value.format(apiFormatter)

                _accountCurrency.value = account.currency

                val transactions =
                    getTransactionsUseCase.execute(account.id, start, end).filter {
                        it.category.isIncome == isIncome
                    }
                _transactionsState.value = ResultState.Success(transactions)

                if (transactions.isEmpty()) {
                    _total.value = 0.0
                } else {
                    calculateTotalExpense(transactions)
                }
            }
        }

        private fun calculateTotalExpense(expenses: List<TransactionResponse>) {
            val sum = expenses.sumOf { it.amount }
            _total.value = sum
        }
    }
