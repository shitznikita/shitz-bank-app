package com.example.shitzbank.data.repository

import com.example.shitzbank.data.network.ShmrFinanceApi
import com.example.shitzbank.domain.model.TransactionResponse
import com.example.shitzbank.domain.repository.TransactionRepository
import okio.IOException
import java.net.UnknownHostException
import javax.inject.Inject

class TransactionReposirotyImpl @Inject constructor(
    private val apiService: ShmrFinanceApi
) : TransactionRepository {

    override suspend fun getTransactionsForPeriod(
       accountId: Int,
       startDate: String,
       endDate: String
    ): List<TransactionResponse> {
        return try {
            apiService.getTransactionsForPeriod(accountId, startDate, endDate)
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