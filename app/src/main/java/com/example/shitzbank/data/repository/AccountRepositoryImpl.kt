package com.example.shitzbank.data.repository

import com.example.shitzbank.common.CoroutineDispatchers
import com.example.shitzbank.common.network.retryWithBackoff
import com.example.shitzbank.data.dtos.AccountCreateRequestDto
import com.example.shitzbank.data.network.ShmrFinanceApi
import com.example.shitzbank.domain.model.Account
import com.example.shitzbank.domain.model.AccountCreateRequest
import com.example.shitzbank.domain.repository.AccountRepository
import kotlinx.coroutines.withContext
import okio.IOException
import java.net.UnknownHostException
import javax.inject.Inject

class AccountRepositoryImpl
    @Inject
    constructor(
        private val apiService: ShmrFinanceApi,
        private val coroutineDispatchers: CoroutineDispatchers,
    ) : AccountRepository {
        override suspend fun createAccount(request: AccountCreateRequest): Account {
            return withContext(coroutineDispatchers.io) {
                val requestDto =
                    AccountCreateRequestDto(
                        name = request.name,
                        balance = request.balance,
                        currency = request.currency,
                    )
                val createAccountResponseDto =
                    retryWithBackoff {
                        apiService.createAccount(requestDto)
                    }
                createAccountResponseDto.toDomain()
            }
        }

        override suspend fun getAccounts(): List<Account> {
            return withContext(coroutineDispatchers.io) {
                try {
                    val accountsDto =
                        retryWithBackoff {
                            apiService.getAccounts()
                        }
                    accountsDto.map { it.toDomain() }
                } catch (e: UnknownHostException) {
                    println("No internet connection or unknown host: ${e.message}")
                    emptyList()
                } catch (e: IOException) {
                    println("Network error: ${e.message}")
                    emptyList()
                }
            }
        }
    }
