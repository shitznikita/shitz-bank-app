package com.example.shitzbank.ui.screen.expenses

import androidx.lifecycle.viewModelScope
import com.example.shitzbank.common.ResultState
import com.example.shitzbank.common.network.ConnectionStatus
import com.example.shitzbank.common.network.NetworkMonitor
import com.example.shitzbank.common.network.NetworkMonitorViewModel
import com.example.shitzbank.common.utils.datetime.toIsoZString
import com.example.shitzbank.domain.model.TransactionResponse
import com.example.shitzbank.domain.usecase.account.GetDefaultAccountUseCase
import com.example.shitzbank.domain.usecase.transactions.GetExpensesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class ExpensesViewModel
    @Inject
    constructor(
        private val getExpensesUseCase: GetExpensesUseCase,
        private val getDefaultAccountUseCase: GetDefaultAccountUseCase,
        networkMonitor: NetworkMonitor,
    ) : NetworkMonitorViewModel(networkMonitor) {
        private val _expensesState = MutableStateFlow<ResultState<List<TransactionResponse>>>(ResultState.Loading)
        val expensesState: StateFlow<ResultState<List<TransactionResponse>>> = _expensesState.asStateFlow()

        private val DEFAULT_TOTAL_VALUE = 0.0
        private val _totalExpense = MutableStateFlow(DEFAULT_TOTAL_VALUE)
        val totalExpense: StateFlow<Double> = _totalExpense.asStateFlow()

        private val _accountCurrency = MutableStateFlow("")
        val accountCurrency: StateFlow<String> = _accountCurrency.asStateFlow()

        init {
            viewModelScope.launch {
//                networkStatus.collect { status ->
//                    if (status is ConnectionStatus.Available) {
//                        loadExpenses()
//                    }
//                }
                loadExpenses()
            }
        }

        fun loadExpenses() {
            viewModelScope.launch {
//                if (networkStatus.value is ConnectionStatus.Unavailable) {
//                    return@launch
//                }

                _expensesState.value = ResultState.Loading
                _totalExpense.value = DEFAULT_TOTAL_VALUE

                val apiFormatter = DateTimeFormatter.ISO_LOCAL_DATE
                val startDateString = LocalDate.now().format(apiFormatter)
                val endDateString = LocalDate.now().format(apiFormatter)

                val account = getDefaultAccountUseCase.execute()

                _accountCurrency.value = account.currency

                val expenses = getExpensesUseCase.execute(account.id, startDateString, endDateString)
                _expensesState.value = ResultState.Success(expenses)

                if (expenses.isEmpty()) {
                    _totalExpense.value = 0.0
                } else {
                    calculateTotalExpense(expenses)
                }
            }
        }

        private fun calculateTotalExpense(expenses: List<TransactionResponse>) {
            val sum = expenses.sumOf { it.amount }
            _totalExpense.value = sum
        }
    }
