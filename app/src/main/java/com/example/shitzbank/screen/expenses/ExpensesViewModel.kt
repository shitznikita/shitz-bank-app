package com.example.shitzbank.screen.expenses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shitzbank.domain.model.TransactionResponse
import com.example.shitzbank.common.ResultState
import com.example.shitzbank.common.network.ConnectionStatus
import com.example.shitzbank.common.network.NetworkMonitor
import com.example.shitzbank.domain.usecase.GetDefaultAccountIdUseCase
import com.example.shitzbank.domain.usecase.GetExpensesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class ExpensesViewModel @Inject constructor(
    private val getExpensesUseCase: GetExpensesUseCase,
    private val getDefaultAccountIdUseCase: GetDefaultAccountIdUseCase,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {
    private val _expensesState = MutableStateFlow<ResultState<List<TransactionResponse>>>(ResultState.Loading)
    val expensesState: StateFlow<ResultState<List<TransactionResponse>>> = _expensesState.asStateFlow()

    private val _totalExpense = MutableStateFlow(0.0)
    val totalExpense: StateFlow<Double> = _totalExpense.asStateFlow()

    private val _networkStatus = MutableStateFlow<ConnectionStatus>(ConnectionStatus.Unavailable)
    val networkStatus: StateFlow<ConnectionStatus> = _networkStatus.asStateFlow()

    init {
        viewModelScope.launch {
            networkMonitor.connectionStatus.collect { status ->
                _networkStatus.value = status
                if (status is ConnectionStatus.Available) {
                    loadExpenses()
                }
            }
        }
    }

    fun loadExpenses() {
        viewModelScope.launch {
            if (_networkStatus.value is ConnectionStatus.Unavailable) {
                return@launch
            }

            _expensesState.value = ResultState.Loading
            _totalExpense.value = 0.0

            try {
                val apiFormatter = DateTimeFormatter.ISO_LOCAL_DATE
                val startDateString = LocalDate.now().format(apiFormatter)
                val endDateString = LocalDate.now().format(apiFormatter)

                val accountId = getDefaultAccountIdUseCase.execute()

                val expenses = getExpensesUseCase.execute(accountId!!, startDateString, endDateString)
                _expensesState.value = ResultState.Success(expenses)

                if (expenses.isEmpty()) {
                    _totalExpense.value = 0.0
                } else {
                    calculateTotalExpense(expenses)
                }

            } catch (e: Exception) {
                _expensesState.value = ResultState.Error("Error: ${e.message}")
            }
        }
    }

    private fun calculateTotalExpense(expenses: List<TransactionResponse>) {
        val sum = expenses.sumOf { it.amount }
        _totalExpense.value = sum
    }
}