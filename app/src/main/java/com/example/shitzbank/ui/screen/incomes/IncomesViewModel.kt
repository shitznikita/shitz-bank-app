package com.example.shitzbank.ui.screen.incomes

import androidx.lifecycle.viewModelScope
import com.example.shitzbank.common.ResultState
import com.example.shitzbank.common.network.ConnectionStatus
import com.example.shitzbank.common.network.NetworkMonitor
import com.example.shitzbank.common.network.NetworkMonitorViewModel
import com.example.shitzbank.domain.model.TransactionResponse
import com.example.shitzbank.domain.usecase.GetDefaultAccountIdUseCase
import com.example.shitzbank.domain.usecase.GetIncomesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class IncomesViewModel
    @Inject
    constructor(
        private val getDefaultAccountIdUseCase: GetDefaultAccountIdUseCase,
        private val getIncomesUseCase: GetIncomesUseCase,
        networkMonitor: NetworkMonitor,
    ) : NetworkMonitorViewModel(networkMonitor) {
        private val _incomesState = MutableStateFlow<ResultState<List<TransactionResponse>>>(ResultState.Loading)
        val incomesState: StateFlow<ResultState<List<TransactionResponse>>> = _incomesState.asStateFlow()

        private val DEFAULT_TOTAL_VALUE = 0.0
        private val _totalIncome = MutableStateFlow(DEFAULT_TOTAL_VALUE)
        val totalIncome: StateFlow<Double> = _totalIncome.asStateFlow()

        init {
            viewModelScope.launch {
                networkStatus.collect { status ->
                    if (status is ConnectionStatus.Available) {
                        loadIncomes()
                    }
                }
            }
        }

        fun loadIncomes() {
            viewModelScope.launch {
                if (networkStatus.value is ConnectionStatus.Unavailable) {
                    return@launch
                }

                _incomesState.value = ResultState.Loading
                _totalIncome.value = DEFAULT_TOTAL_VALUE

                val apiFormatter = DateTimeFormatter.ISO_LOCAL_DATE
                val startDateString = LocalDate.now().format(apiFormatter)
                val endDateString = LocalDate.now().format(apiFormatter)

                val accountId = getDefaultAccountIdUseCase.execute()

                val incomes = getIncomesUseCase.execute(accountId, startDateString, endDateString)
                _incomesState.value = ResultState.Success(incomes)

                if (incomes.isEmpty()) {
                    _totalIncome.value = 0.0
                } else {
                    calculateTotalIncome(incomes)
                }
            }
        }

        private fun calculateTotalIncome(expenses: List<TransactionResponse>) {
            val sum = expenses.sumOf { it.amount }
            _totalIncome.value = sum
        }
    }
