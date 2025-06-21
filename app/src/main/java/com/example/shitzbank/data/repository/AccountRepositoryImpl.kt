package com.example.shitzbank.data.repository

import com.example.shitzbank.data.network.ShmrFinanceApi
import com.example.shitzbank.domain.model.Account
import com.example.shitzbank.domain.model.AccountCreateRequest
import com.example.shitzbank.domain.repository.AccountRepository
import okio.IOException
import java.net.UnknownHostException
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val apiService: ShmrFinanceApi
) : AccountRepository {

    override suspend fun createAccount(request: AccountCreateRequest): Account {
        return apiService.createAccount(request)
    }

    override suspend fun getAccounts(): List<Account> {
        return try {
            apiService.getAccounts()
        } catch (e: UnknownHostException) {
            println("No internet connection or unknown host: ${e.message}")
            emptyList()
        } catch (e: IOException) {
            println("Network error: ${e.message}")
            emptyList()
        } catch (e: Exception) {
            println("An unexpected error occurred: ${e.message}")
            emptyList()
        }
    }

}