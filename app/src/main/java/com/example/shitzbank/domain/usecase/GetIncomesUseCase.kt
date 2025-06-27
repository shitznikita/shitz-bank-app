package com.example.shitzbank.domain.usecase

import com.example.shitzbank.domain.model.TransactionResponse
import com.example.shitzbank.domain.repository.TransactionRepository
import javax.inject.Inject

/**
 * Сценарий использования (Use Case) для получения списка доходных транзакций за указанный период.
 *
 * Фильтрует все транзакции, оставляя только те, которые являются доходом, и возвращает их в обратном порядке.
 *
 * @param transactionRepository Репозиторий для доступа к данным транзакций. Внедряется через Hilt/Dagger.
 */
class GetIncomesUseCase
    @Inject
    constructor(
        private val transactionRepository: TransactionRepository,
    ) {
        /**
         * Выполняет операцию получения списка доходных транзакций.
         *
         * @param accountId Идентификатор счета, для которого нужно получить доходы.
         * @param startDate Строка, представляющая начальную дату периода (например, "ГГГГ-ММ-ДД").
         * @param endDate Строка, представляющая конечную дату периода (например, "ГГГГ-ММ-ДД").
         * @return Список объектов [TransactionResponse], представляющих доходные транзакции за указанный период,
         * отсортированных в обратном порядке (обычно это означает от новых к старым).
         * Может вернуть пустой список, если доходных транзакций нет.
         * @throws Exception Может выбросить исключение, если операция получения транзакций завершится неудачей
         * (например, из-за сетевой ошибки или неверного формата даты).
         */
        suspend fun execute(
            accountId: Int,
            startDate: String,
            endDate: String,
        ): List<TransactionResponse> {
            val allTransactions = transactionRepository.getTransactionsForPeriod(accountId, startDate, endDate)
            return allTransactions.filter { it.category.isIncome }.reversed()
        }
    }
