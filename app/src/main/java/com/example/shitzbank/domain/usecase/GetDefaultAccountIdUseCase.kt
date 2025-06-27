package com.example.shitzbank.domain.usecase

import com.example.shitzbank.domain.repository.AccountRepository
import javax.inject.Inject

/**
 * Сценарий использования (Use Case) для получения идентификатора счета по умолчанию.
 *
 * Этот Use Case предназначен для получения ID первого доступного счета,
 * предполагая, что первый счет в списке является счетом "по умолчанию" или основным.
 *
 * @param accountRepository Репозиторий для доступа к данным счетов. Внедряется через Hilt/Dagger.
 */
class GetDefaultAccountIdUseCase
    @Inject
    constructor(
        private val accountRepository: AccountRepository,
    ) {
        /**
         * Выполняет операцию получения идентификатора первого счета.
         *
         * @return [Int] идентификатор первого счета в списке.
         * @throws NoSuchElementException Если список счетов пуст (то есть нет доступных счетов).
         * @throws Exception Другие исключения, которые могут возникнуть при получении счетов из репозитория
         * (например, сетевые ошибки).
         */
        suspend fun execute(): Int {
            val accounts = accountRepository.getAccounts()
            return accounts.first().id
        }
    }
