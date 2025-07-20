package com.example.shitzbank.ui.screen.analysis

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.shitzbank.common.ResultState
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

data class CategorySummary(
    val categoryName: String,
    val categoryEmoji: String,
    val totalAmount: Double
)

@HiltViewModel
class AnalysisViewModel @Inject constructor(
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

    private val _categorySummaries = MutableStateFlow<ResultState<List<CategorySummary>>>(ResultState.Loading)
    val categorySummaries: StateFlow<ResultState<List<CategorySummary>>> = _categorySummaries.asStateFlow()

    private val _accountCurrency = MutableStateFlow("")
    val accountCurrency: StateFlow<String> = _accountCurrency.asStateFlow()

    private val _dateError = MutableStateFlow<String?>(null)
    val dateError: StateFlow<String?> = _dateError.asStateFlow()

    init {
        viewModelScope.launch {
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
            _transactionsState.value = ResultState.Loading
            _total.value = DEFAULT_TOTAL_VALUE
            _categorySummaries.value = ResultState.Loading

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
                _categorySummaries.value = ResultState.Loading
            } else {
                calculateTotalAndCategorySums(transactions)
            }
        }
    }

    private fun calculateTotalAndCategorySums(
        transactions: List<TransactionResponse>
    ) {
        val totalSum = transactions.sumOf { it.amount }
        _total.value = totalSum

        val categoryMap = transactions.groupBy { it.category.id }
            .mapValues { (_, transactionsInGroup) ->
                transactionsInGroup.sumOf { it.amount }
            }

        val summaries = categoryMap.mapNotNull { (categoryId, totalAmount) ->
            val categoryInfo = transactions.firstOrNull { it.category.id == categoryId }?.category
            categoryInfo?.let {
                CategorySummary(
                    categoryName = it.name,
                    categoryEmoji = it.emoji,
                    totalAmount = totalAmount
                )
            }
        }.sortedByDescending { it.totalAmount }

        _categorySummaries.value = ResultState.Success(summaries)
    }
}
