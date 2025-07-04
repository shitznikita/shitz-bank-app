package com.example.shitzbank.domain.usecase.account

import com.example.shitzbank.domain.model.Account
import com.example.shitzbank.domain.model.AccountCreateRequest
import com.example.shitzbank.domain.repository.AccountRepository
import javax.inject.Inject

/**
 * Сценарий использования (Use Case) для создания нового банковского счета.
 * Инкапсулирует бизнес-логику, необходимую для выполнения этой операции,
 * взаимодействуя с [AccountRepository] для сохранения данных.
 *
 * @param accountRepository Репозиторий для доступа к данным счетов. Внедряется через Hilt/Dagger.
 */
class CreateAccountUseCase
    @Inject
    constructor(
        private val accountRepository: AccountRepository,
    ) {
        /**
         * Выполняет операцию создания нового банковского счета.
         *
         * @param name Название нового счета.
         * @param balance Начальный баланс нового счета.
         * @param currency Валюта нового счета.
         * @return Созданный объект [Account].
         * @throws Exception Может выбросить исключение, если операция создания счета завершится неудачей
         * (например, из-за сетевой ошибки или проблем на сервере).
         */
        suspend fun execute(
            name: String,
            balance: Double,
            currency: String,
        ): Account {
            val request = AccountCreateRequest(name, balance, currency)
            return accountRepository.createAccount(request)
        }
    }
