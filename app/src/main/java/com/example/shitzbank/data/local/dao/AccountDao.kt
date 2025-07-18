package com.example.shitzbank.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.shitzbank.data.dtos.AccountDto
import com.example.shitzbank.data.local.entity.AccountEntity
import com.example.shitzbank.domain.model.Account

@Dao
interface AccountDao {
    /**
     * Вставляет список счетов в базу данных.
     * При конфликте (счет с таким же ID уже существует) заменяет существующий.
     * Принимает список доменных моделей Account.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccounts(accounts: List<AccountDto>) {
        insertAccountsEntities(accounts.map { it.toEntity() })
    }

    /**
     * Вспомогательный приватный метод для Room, принимает List<AccountEntity>
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccountsEntities(accounts: List<AccountEntity>)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createAccount(account: AccountDto) {
        insertAccountEntity(account.toEntity())
    }

    /**
     * Вспомогательный приватный метод для Room, принимает AccountEntity
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccountEntity(account: AccountEntity)

    /**
     * Получает все счета из базы данных.
     * Возвращает список доменных моделей Account.
     */
    @Query("SELECT * FROM accounts")
    suspend fun getAccounts(): List<Account> { // Изменено на suspend и возвращает List<Account>
        return getAccountsEntities().map { it.toDomain() }
    }

    /**
     * Вспомогательный приватный метод для Room, возвращает List<AccountEntity>
     */
    @Query("SELECT * FROM accounts")
    suspend fun getAccountsEntities(): List<AccountEntity>


    /**
     * Обновляет существующий счет в базе данных.
     * Принимает ID счета и доменную модель AccountCreateRequest.
     *
     * Аналогично createAccount, DAO не должен самостоятельно манипулировать запросами,
     * которые не соответствуют Entity напрямую. DAO должен получить уже готовую Entity.
     *
     * *Предпочтительный сценарий:*
     * Метод updateAccount в DAO будет принимать **полный `Account` (доменную модель)**.
     * Репозиторий будет отвечать за преобразование `AccountCreateRequest` и `id`
     * в полный `Account` (возможно, путем загрузки существующего Account,
     * обновления его полей из `AccountCreateRequest` и сохранения обновленного).
     */
    @Update
    suspend fun updateAccount(account: AccountDto) {
        updateAccountEntity(account.toEntity())
    }

    /**
     * Вспомогательный приватный метод для Room, принимает AccountEntity
     */
    @Update
    suspend fun updateAccountEntity(account: AccountEntity)

    /**
     * Получает счет по его ID.
     * Возвращает доменную модель Account или null.
     */
    @Query("SELECT * FROM accounts WHERE id = :accountId")
    suspend fun getAccountById(accountId: Int): Account? {
        return getAccountEntityById(accountId)?.toDomain()
    }

    /**
     * Вспомогательный приватный метод для Room, возвращает AccountEntity
     */
    @Query("SELECT * FROM accounts WHERE id = :accountId")
    suspend fun getAccountEntityById(accountId: Int): AccountEntity?

    /**
     * Удаляет счет по его ID.
     * Возвращает количество удаленных строк (1, если успешно, 0, если нет).
     */
    @Query("DELETE FROM accounts WHERE id = :accountId")
    suspend fun deleteAccountById(accountId: Int): Int

    /**
     * Удаляет все счета из базы данных.
     */
    @Query("DELETE FROM accounts")
    suspend fun deleteAllAccounts()

}