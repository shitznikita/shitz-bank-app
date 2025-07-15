package com.example.shitzbank.domain.repository

import com.example.shitzbank.domain.model.Transaction
import com.example.shitzbank.domain.model.TransactionRequest
import com.example.shitzbank.domain.model.TransactionResponse

/**
 * Интерфейс репозитория для управления данными транзакций ([TransactionResponse]).
 * Определяет операцию по получению информации о транзакциях за определенный период.
 *
 * Абстрагирует доменный слой от деталей реализации хранения и получения данных транзакций.
 */
interface TransactionRepository {
    /**
     * Получает список транзакций для указанного счета за заданный период времени.
     *
     * @param accountId Уникальный идентификатор счета, для которого нужно получить транзакции.
     * @param startDate Строка, представляющая начальную дату периода (например, "ГГГГ-ММ-ДД").
     * @param endDate Строка, представляющая конечную дату периода (например, "ГГГГ-ММ-ДД").
     * @return Список объектов [TransactionResponse], соответствующих транзакциям за указанный период.
     * Может вернуть пустой список, если транзакции отсутствуют.
     * @throws Exception Может выбросить исключение в случае ошибки получения транзакций
     * (например, сетевой ошибки, неверного формата даты).
     */
    suspend fun getTransactionsForPeriod(
        accountId: Int,
        startDate: String,
        endDate: String,
    ): List<TransactionResponse>

    suspend fun createTransaction(
        request: TransactionRequest
    ): Transaction?

    suspend fun updateTransactionById(
        transactionId: Int,
        request: TransactionRequest
    ): TransactionResponse?

    suspend fun getTransactionById(
        transactionId: Int
    ): TransactionResponse?

    suspend fun deleteTransactionById(
        transactionId: Int
    ): Boolean
}
