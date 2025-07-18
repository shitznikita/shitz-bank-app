package com.example.shitzbank.data.repository

import android.util.Log
import com.example.shitzbank.common.CoroutineDispatchers
import com.example.shitzbank.common.network.ConnectionStatus
import com.example.shitzbank.common.network.NetworkMonitor
import com.example.shitzbank.common.network.retryWithBackoff
import com.example.shitzbank.data.dtos.AccountCreateRequestDto
import com.example.shitzbank.data.local.dao.AccountDao
import com.example.shitzbank.data.network.ShmrFinanceApi
import com.example.shitzbank.domain.model.Account
import com.example.shitzbank.domain.model.AccountCreateRequest
import com.example.shitzbank.domain.repository.AccountRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import okio.IOException
import java.net.UnknownHostException
import javax.inject.Inject

class AccountRepositoryImpl
    @Inject
    constructor(
        private val apiService: ShmrFinanceApi,
        private val coroutineDispatchers: CoroutineDispatchers,
        private val accountDao: AccountDao, // Inject AccountDao
        private val networkMonitor: NetworkMonitor,
    ) : AccountRepository {
    private suspend fun isNetworkAvailable(): Boolean {
        return networkMonitor.connectionStatus.firstOrNull() is ConnectionStatus.Available
    }

    override suspend fun createAccount(request: AccountCreateRequest): Account {
        return withContext(coroutineDispatchers.io) {
            // For account creation, we typically try network first.
            // If offline, you'd save it locally with a pending sync flag.
            // For simplicity here, we'll assume creation requires network.
            // If you want offline creation, you'd need a similar pending sync mechanism
            // as you have for transactions, with a WorkManager to sync later.
            if (isNetworkAvailable()) {
                try {
                    val requestDto = AccountCreateRequestDto(
                        name = request.name,
                        balance = request.balance,
                        currency = request.currency,
                    )
                    val createAccountResponseDto = retryWithBackoff {
                        apiService.createAccount(requestDto)
                    }
                    val createdAccount = createAccountResponseDto
                    // Save the newly created account to Room
                    accountDao.insertAccountEntity(createdAccount.toEntity()) // toEntity() mapper for Account domain to AccountEntity
                    Log.d("AccountRepo", "Account created on API and saved to Room: ${createdAccount.id}")
                    return@withContext createdAccount.toDomain()
                } catch (e: Exception) {
                    Log.e("AccountRepo", "Failed to create account online: ${e.message}", e)
                    throw e // Re-throw to indicate failure if creation requires online
                }
            } else {
                Log.e("AccountRepo", "Cannot create account: No network available.")
                throw IOException("No network available to create account.") // Or a custom exception
            }
        }
    }

    override suspend fun getAccounts(): List<Account> {
        return withContext(coroutineDispatchers.io) {
            if (isNetworkAvailable()) {
                try {
                    val accountsDto = retryWithBackoff {
                        apiService.getAccounts()
                    }
                    // Clear old accounts and insert new ones for full sync
                    accountDao.deleteAllAccounts() // Or smarter merge logic
                    accountDao.insertAccountsEntities(accountsDto.map { it.toEntity() }) // Assuming toEntity mapper from DTO to Entity
                    Log.d("AccountRepo", "Accounts synchronized from API and saved to Room.")
                } catch (e: Exception) {
                    Log.e("AccountRepo", "Failed to synchronize accounts from API: ${e.message}. Using cached data.", e)
                    // This catch block handles UnknownHostException, IOException, etc.
                }
            } else {
                Log.d("AccountRepo", "No network available for getAccounts. Using cached data.")
            }
            // Always return data from Room
            val accounts = accountDao.getAccountsEntities().map { it.toDomain() }
            Log.d("AccountRepo", "Loaded ${accounts.size} accounts from Room.")
            accounts
        }
    }

    override suspend fun updateAccount(id: Int, request: AccountCreateRequest): Account {
        return withContext(coroutineDispatchers.io) {
            // Similar to create, updates typically require network for immediate consistency.
            // If offline updates are needed, implement pending sync like in TransactionRepositoryImpl.
            if (isNetworkAvailable()) {
                try {
                    val requestDto = AccountCreateRequestDto(
                        name = request.name,
                        balance = request.balance,
                        currency = request.currency
                    )
                    val updateAccountResponseDto = retryWithBackoff {
                        apiService.updateAccountById(id, requestDto)
                    }
                    val updatedAccount = updateAccountResponseDto
                    // Update the account in Room as well
                    accountDao.insertAccountEntity(updatedAccount.toEntity()) // REPLACE strategy will update
                    Log.d("AccountRepo", "Account updated on API and in Room: ${updatedAccount.id}")
                    return@withContext updatedAccount.toDomain()
                } catch (e: Exception) {
                    Log.e("AccountRepo", "Failed to update account online: ${e.message}", e)
                    throw e
                }
            } else {
                Log.e("AccountRepo", "Cannot update account: No network available.")
                throw IOException("No network available to update account.")
            }
        }
    }
    }
