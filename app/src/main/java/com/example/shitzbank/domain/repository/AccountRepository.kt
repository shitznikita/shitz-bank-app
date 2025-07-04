package com.example.shitzbank.domain.repository

import com.example.shitzbank.domain.model.Account
import com.example.shitzbank.domain.model.AccountCreateRequest

/**
 * Интерфейс репозитория для управления данными счетов ([Account]).
 * Определяет операции, связанные с созданием и получением информации о банковских счетах.
 *
 * Абстрагирует доменный слой от деталей реализации хранения и получения данных.
 */
interface AccountRepository {
    /**
     * Создает новый банковский счет на основе предоставленных данных.
     *
     * @param request Объект [AccountCreateRequest], содержащий данные, необходимые для создания счета.
     * @return Объект [Account], представляющий только что созданный счет.
     * @throws Exception Может выбросить исключение в случае ошибки создания счета
     * (например, сетевой ошибки, ошибки валидации).
     */
    suspend fun createAccount(request: AccountCreateRequest): Account

    /**
     * Получает список всех банковских счетов.
     *
     * @return Список объектов [Account], представляющих все доступные счета.
     * Может вернуть пустой список, если счета отсутствуют.
     * @throws Exception Может выбросить исключение в случае ошибки получения счетов (например, сетевой ошибки).
     */
    suspend fun getAccounts(): List<Account>

    suspend fun updateAccount(id: Int, request: AccountCreateRequest): Account
}
