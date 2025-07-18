package com.example.shitzbank

import androidx.lifecycle.viewModelScope
import com.example.shitzbank.common.network.ConnectionStatus
import com.example.shitzbank.common.network.NetworkMonitor
import com.example.shitzbank.common.network.NetworkMonitorViewModel
import com.example.shitzbank.common.network.retryWithBackoff
import com.example.shitzbank.common.utils.datetime.toIsoZString
import com.example.shitzbank.data.local.dao.AccountDao
import com.example.shitzbank.data.local.dao.CategoryDao
import com.example.shitzbank.data.local.dao.TransactionDao
import com.example.shitzbank.data.network.ShmrFinanceApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    networkMonitor: NetworkMonitor,
    private val accountDao: AccountDao,
    private val categoryDao: CategoryDao,
    private val transactionDao: TransactionDao,
    private val apiService: ShmrFinanceApi
) : NetworkMonitorViewModel(networkMonitor) {

    private var hasSuccessfullyLoadedInitialData = false

    init {
        viewModelScope.launch {
            networkStatus.collect { status ->
                if (status is ConnectionStatus.Available && !hasSuccessfullyLoadedInitialData) {
                    startInitialApiLoad()
                }
            }
        }
    }

    private fun startInitialApiLoad() {
        viewModelScope.launch {
            try {
                val accounts = retryWithBackoff { apiService.getAccounts() }
                accountDao.insertAccounts(accounts)

                val categories = retryWithBackoff { apiService.getCategories() }
                categoryDao.insertCategories(categories)

                val primaryAccountId: Int = accounts.first().id
                val sixMonthsAgo = LocalDateTime.now().minusMonths(6).toIsoZString()
                val now = LocalDateTime.now().toIsoZString()
                val transactions = retryWithBackoff {
                    apiService.getTransactionsForPeriod(primaryAccountId, sixMonthsAgo, now)
                }
                transactionDao.insertTransactions(transactions)

                hasSuccessfullyLoadedInitialData = true
            } catch (e: Exception) {
                println("AppViewModel: Initial data load from API failed: ${e.message}")
            }
        }
    }
}
