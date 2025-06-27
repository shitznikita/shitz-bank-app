package com.example.shitzbank.domain.usecase

import com.example.shitzbank.domain.model.TransactionResponse
import com.example.shitzbank.domain.repository.TransactionRepository
import javax.inject.Inject

/**
 * Сценарий использования (Use Case) для получения списка расходных транзакций за указанный период.
 *
 * Фильтрует все транзакции, оставляя только те, которые не являются доходом, и возвращает их в обратном порядке.
 *
 * @param transactionRepository Репозиторий для доступа к данным транзакций. Внедряется через Hilt/Dagger.
 */
class GetExpensesUseCase
    @Inject
    constructor(
        private val transactionRepository: TransactionRepository,
    ) {
        /**
         * Выполняет операцию получения списка расходных транзакций.
         *
         * @param accountId Идентификатор счета, для которого нужно получить расходы.
         * @param startDate Строка, представляющая начальную дату периода (например, "ГГГГ-ММ-ДД").
         * @param endDate Строка, представляющая конечную дату периода (например, "ГГГГ-ММ-ДД").
         * @return Список объектов [TransactionResponse], представляющих расходные транзакции за указанный период,
         * отсортированных в обратном порядке (обычно это означает от новых к старым).
         * Может вернуть пустой список, если расходных транзакций нет.
         * @throws Exception Может выбросить исключение, если операция получения транзакций завершится неудачей
         * (например, из-за сетевой ошибки или неверного формата даты).
         */
        suspend fun execute(
            accountId: Int,
            startDate: String,
            endDate: String,
        ): List<TransactionResponse> {
            val allTransactions = transactionRepository.getTransactionsForPeriod(accountId, startDate, endDate)
            return allTransactions.filter { !it.category.isIncome }.reversed()
        }
    }
