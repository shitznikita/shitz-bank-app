package com.example.shitzbank.di

import com.example.shitzbank.data.repository.AccountRepositoryImpl
import com.example.shitzbank.data.repository.CategoryRepositoryImpl
import com.example.shitzbank.data.repository.TransactionRepositoryImpl
import com.example.shitzbank.domain.repository.AccountRepository
import com.example.shitzbank.domain.repository.CategoryRepository
import com.example.shitzbank.domain.repository.TransactionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DomainModule {
    @Binds
    @Singleton
    abstract fun bindTransactionRepository(transactionRepositoryImpl: TransactionRepositoryImpl): TransactionRepository

    @Binds
    @Singleton
    abstract fun bindAccountRepository(accountRepositoryImpl: AccountRepositoryImpl): AccountRepository

    @Binds
    @Singleton
    abstract fun bindCategoryRepository(categoryRepositoryImpl: CategoryRepositoryImpl): CategoryRepository
}
