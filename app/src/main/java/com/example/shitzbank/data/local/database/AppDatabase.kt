package com.example.shitzbank.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.shitzbank.data.local.dao.AccountDao
import com.example.shitzbank.data.local.dao.CategoryDao
import com.example.shitzbank.data.local.dao.TransactionDao
import com.example.shitzbank.data.local.entity.AccountEntity
import com.example.shitzbank.data.local.entity.CategoryEntity
import com.example.shitzbank.data.local.entity.TransactionEntity

@Database(
    entities = [AccountEntity::class, CategoryEntity::class, TransactionEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao
    abstract fun categoryDao(): CategoryDao
    abstract fun transactionDao(): TransactionDao
}