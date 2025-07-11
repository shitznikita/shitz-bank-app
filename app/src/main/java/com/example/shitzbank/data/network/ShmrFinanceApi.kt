package com.example.shitzbank.data.network

import com.example.shitzbank.data.dtos.AccountCreateRequestDto
import com.example.shitzbank.data.dtos.AccountDto
import com.example.shitzbank.data.dtos.AccountHistoryResponseDto
import com.example.shitzbank.data.dtos.AccountResponseDto
import com.example.shitzbank.data.dtos.CategoryDto
import com.example.shitzbank.data.dtos.TransactionRequestDto
import com.example.shitzbank.data.dtos.TransactionResponseDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ShmrFinanceApi {
    @GET("accounts")
    suspend fun getAccounts(): List<AccountDto>

    @POST("accounts")
    suspend fun createAccount(
        @Body request: AccountCreateRequestDto,
    ): AccountDto

    @GET("accounts/{id}")
    suspend fun getAccountById(
        @Path("id") id: Int,
    ): AccountResponseDto

    @PUT("accounts/{id}")
    suspend fun updateAccountById(
        @Path("id") id: Int,
        @Body request: AccountCreateRequestDto
    ): AccountDto

    @DELETE("accounts/{id}")
    suspend fun deleteAccountById(
        @Path("id") id: Int
    ): Boolean

    @GET("accounts/{id}/history")
    suspend fun getAccountHistory(
        @Path("id") id: Int,
    ): AccountHistoryResponseDto

    @GET("categories")
    suspend fun getCategories(): List<CategoryDto>

    @GET("categories/type/{isIncome}")
    suspend fun getCategoriesByType(
        @Path("isIncome") isIncome: Boolean,
    ): List<CategoryDto>

    @POST("transactions")
    suspend fun createTransaction(
        @Body request: TransactionRequestDto,
    ): TransactionResponseDto

    @GET("transactions/{id}")
    suspend fun getTransactionById(
        @Path("id") id: Int,
    ): TransactionResponseDto

    @PUT("transactions/{id}")
    suspend fun updateTransactionById(
        @Path("id") id: Int,
        @Body request: TransactionRequestDto
    )

    @DELETE("transactions/{id}")
    suspend fun deleteTransactionById(
        @Path("id") id: Int
    ): Boolean

    @GET("transactions/account/{accountId}/period")
    suspend fun getTransactionsForPeriod(
        @Path("accountId") accountId: Int,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String,
    ): List<TransactionResponseDto>
}
