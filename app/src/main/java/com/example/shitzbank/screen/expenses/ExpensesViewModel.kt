package com.example.shitzbank.screen.expenses.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shitzbank.domain.model.TransactionResponse
import com.example.shitzbank.domain.repository.ResultState
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
    private val getExpensesUseCase: GetExpensesUseCase
) : ViewModel() {
    private val _expensesState = MutableStateFlow<ResultState<List<TransactionResponse>>>(ResultState.Loading)
    val expensesState: StateFlow<ResultState<List<TransactionResponse>>> = _expensesState.asStateFlow()

    private val _totalExpense = MutableStateFlow(0.0)
    val totalExpense: StateFlow<Double> = _totalExpense.asStateFlow()

    fun loadExpenses() {
        viewModelScope.launch {
            _expensesState.value = ResultState.Loading

            try {
                val today = LocalDate.now()
                val startOfMonth = today.withDayOfMonth(1)

                val formatter = DateTimeFormatter.ISO_LOCAL_DATE
                val apiFormatter = DateTimeFormatter.ISO_DATE_TIME
                val startDateString = startOfMonth.atStartOfDay().format(apiFormatter)
                val endDateString = today.atTime(23, 59, 59).format(apiFormatter)

                val accountId = 0;

                val expenses = getExpensesUseCase.execute(accountId, startDateString, endDateString)
                _expensesState.value = ResultState.Success(expenses)

            } catch (e: Exception) {
                _expensesState.value = ResultState.Error("Error: ${e.message}")
            }
        }
    }
}