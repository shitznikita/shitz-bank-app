package com.example.shitzbank.data.network

import com.example.shitzbank.domain.model.Account
import com.example.shitzbank.domain.model.AccountCreateRequest
import com.example.shitzbank.domain.model.AccountHistoryResponse
import com.example.shitzbank.domain.model.AccountResponse
import com.example.shitzbank.domain.model.Category
import com.example.shitzbank.domain.model.TransactionRequest
import com.example.shitzbank.domain.model.TransactionResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ShmrFinanceApi {

    @GET("accounts")
    suspend fun getAccounts(): List<Account>

    @POST("accounts")
    suspend fun createAccount(
        @Body request: AccountCreateRequest
    ): Account

    @GET("accounts/{id}")
    suspend fun getAccountById(
        @Path("id") id: Int
    ): AccountResponse

//    @PUT("accounts/{id}")
//    suspend fun updateAccountById(
//        @Path("id") id: Int,
//        @Body request: AccountCreateRequest
//    ): Account
//
//    @DELETE("accounts/{id}")
//    suspend fun deleteAccountById(
//        @Path("id") id: Int
//    ): Boolean

    @GET("accounts/{id}/history")
    suspend fun getAccountHistory(
        @Path("id") id: Int
    ): AccountHistoryResponse

    @GET("categories")
    suspend fun getCategories(): List<Category>

    @GET("categories/type/{isIncome}")
    suspend fun getCategoriesByType(
        @Path("isIncome") isIncome: Boolean
    ): List<Category>

    @POST("transactions")
    suspend fun createTransaction(
        @Body request: TransactionRequest
    ): TransactionResponse

    @GET("transactions/{id}")
    suspend fun getTransactionsById(
        @Path("id") id: Int
    ): TransactionResponse

//    @PUT("transactions/{id}")
//    suspend fun updateTransactionById(
//        @Path("id") id: Int,
//        @Body request: TransactionRequest
//    )
//
//    @DELETE("transactions/{id}")
//    suspend fun deleteTransactionById(
//        @Path("id") id: Int
//    ): Boolean

    @GET("transactions/account/{accountId}/period")
    suspend fun getTransactionsForPeriod(
        @Path("accountId") accountId: Int,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): List<TransactionResponse>

}