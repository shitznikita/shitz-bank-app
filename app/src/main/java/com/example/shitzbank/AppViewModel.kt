package com.example.shitzbank

import androidx.lifecycle.viewModelScope
import com.example.shitzbank.common.SyncDataManager
import com.example.shitzbank.common.network.ConnectionStatus
import com.example.shitzbank.common.network.NetworkMonitor
import com.example.shitzbank.common.network.NetworkMonitorViewModel
import com.example.shitzbank.common.network.retryWithBackoff
import com.example.shitzbank.common.utils.datetime.toIsoZString
import com.example.shitzbank.data.local.dao.AccountDao
import com.example.shitzbank.data.local.dao.CategoryDao
import com.example.shitzbank.data.local.dao.TransactionDao
import com.example.shitzbank.data.network.ShmrFinanceApi
import com.example.shitzbank.data.repository.TransactionRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    networkMonitor: NetworkMonitor,
    private val accountDao: AccountDao,
    private val categoryDao: CategoryDao,
    private val transactionDao: TransactionDao,
    private val apiService: ShmrFinanceApi,
    private val transactionRepositoryImpl: TransactionRepositoryImpl,
    private val syncDataManager: SyncDataManager
) : NetworkMonitorViewModel(networkMonitor) {

    private var hasSuccessfullyLoadedInitialData = false

    private val _lastSyncTime = MutableStateFlow<LocalDateTime>(LocalDateTime.now())
    val lastSyncTime: StateFlow<LocalDateTime> = _lastSyncTime.asStateFlow()

    init {
        viewModelScope.launch {
            networkStatus.collect { status ->
                if (status is ConnectionStatus.Available && !hasSuccessfullyLoadedInitialData) {
                    startInitialApiLoad()
                }
                syncPendingTransactions()
            }
        }
    }

    private fun startInitialApiLoad() {
        viewModelScope.launch {
            val accounts = retryWithBackoff { apiService.getAccounts() }
            accountDao.insertAccounts(accounts)

            val categories = retryWithBackoff { apiService.getCategories() }
            categoryDao.insertCategories(categories)

            val primaryAccountId: Int = accounts.first().id
            val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE
            val sixMonthsAgo = LocalDate.now().minusMonths(6).format(dateFormatter)
            val now = LocalDate.now().format(dateFormatter)
            val transactions = retryWithBackoff {
                apiService.getTransactionsForPeriod(primaryAccountId, sixMonthsAgo, now)
            }
            transactionDao.insertTransactions(transactions)

            hasSuccessfullyLoadedInitialData = true
        }
    }

    private fun syncPendingTransactions() {
        viewModelScope.launch {
            val syncedTransactions = transactionRepositoryImpl.syncPendingTransactions()
            _lastSyncTime.value = syncDataManager.getLastSyncTime()
        }
    }
}
