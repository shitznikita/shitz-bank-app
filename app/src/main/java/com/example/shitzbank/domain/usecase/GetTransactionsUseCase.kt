package com.example.shitzbank.domain.usecase

import com.example.shitzbank.domain.model.TransactionResponse
import com.example.shitzbank.domain.repository.TransactionRepository
import javax.inject.Inject

/**
 * Сценарий использования (Use Case) для получения всех транзакций за указанный период.
 *
 * Возвращает список всех транзакций для конкретного счета за заданный диапазон дат,
 * отсортированных в обратном порядке (от новых к старым).
 *
 * @param transactionRepository Репозиторий для доступа к данным транзакций. Внедряется через Hilt/Dagger.
 */
class GetTransactionsUseCase
    @Inject
    constructor(
        private val transactionRepository: TransactionRepository,
    ) {
        /**
         * Выполняет операцию получения всех транзакций для указанного счета и периода.
         *
         * @param accountId Идентификатор счета, для которого нужно получить транзакции.
         * @param startDate Строка, представляющая начальную дату периода (например, "ГГГГ-ММ-ДД").
         * @param endDate Строка, представляющая конечную дату периода (например, "ГГГГ-ММ-ДД").
         * @return Список объектов [TransactionResponse], представляющих транзакции за указанный период,
         * отсортированных в обратном порядке (от новых к старым).
         * Может вернуть пустой список, если транзакции отсутствуют.
         * @throws Exception Может выбросить исключение, если операция получения транзакций завершится неудачей
         * (например, из-за сетевой ошибки или неверного формата даты).
         */
        suspend fun execute(
            accountId: Int,
            startDate: String,
            endDate: String,
        ): List<TransactionResponse> {
            return transactionRepository.getTransactionsForPeriod(accountId, startDate, endDate).reversed()
        }
    }
