package com.example.shitzbank.screen.expenses.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shitzbank.common.ResultState
import com.example.shitzbank.common.network.ConnectionStatus
import com.example.shitzbank.common.network.NetworkMonitor
import com.example.shitzbank.domain.model.TransactionResponse
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
class ExpensesHistoryViewModel @Inject constructor(
    private val getDefaultAccountIdUseCase: GetDefaultAccountIdUseCase,
    private val getExpensesUseCase: GetExpensesUseCase,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {
    private val _startDate = MutableStateFlow(LocalDate.now().withDayOfMonth(1))
    val startDate: StateFlow<LocalDate> = _startDate.asStateFlow()

    private val _endDate = MutableStateFlow(LocalDate.now())
    val endDate: StateFlow<LocalDate> = _endDate.asStateFlow()

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

    fun setStartDate(date: LocalDate) {
        _startDate.value = date
        loadExpenses()
    }

    fun setEndDate(date: LocalDate) {
        _endDate.value = date
        loadExpenses()
    }

    private fun loadExpenses() {
        viewModelScope.launch {
            if (_networkStatus.value is ConnectionStatus.Unavailable) {
                _expensesState.value = ResultState.Loading
                return@launch
            }

            _expensesState.value = ResultState.Loading
            _totalExpense.value = 0.0

            try {
                val accountId = getDefaultAccountIdUseCase.execute()

                val apiFormatter = DateTimeFormatter.ISO_LOCAL_DATE
                val start = _startDate.value.format(apiFormatter)
                val end = _endDate.value.format(apiFormatter)

                val expenses = getExpensesUseCase.execute(accountId!!, start, end)
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