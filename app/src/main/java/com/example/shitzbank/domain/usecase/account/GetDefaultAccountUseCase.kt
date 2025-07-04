package com.example.shitzbank.domain.usecase.account

import com.example.shitzbank.domain.model.Account
import com.example.shitzbank.domain.repository.AccountRepository
import javax.inject.Inject

/**
 * Сценарий использования (Use Case) для получения счета по умолчанию.
 *
 * Этот Use Case предназначен для получения первого доступного счета,
 * предполагая, что первый счет в списке является счетом "по умолчанию" или основным.
 *
 * @param accountRepository Репозиторий для доступа к данным счетов. Внедряется через Hilt/Dagger.
 */
class GetDefaultAccountUseCase
    @Inject
    constructor(
        private val accountRepository: AccountRepository,
    ) {
        /**
         * Выполняет операцию получения первого счета.
         *
         * @return Объект [Account], представляющий первый счет в списке.
         * @throws NoSuchElementException Если список счетов пуст (то есть нет доступных счетов).
         * @throws Exception Другие исключения, которые могут возникнуть при получении счетов из репозитория
         * (например, сетевые ошибки).
         */
        suspend fun execute(): Account {
            val accounts = accountRepository.getAccounts()
            return accounts.first()
        }
    }
