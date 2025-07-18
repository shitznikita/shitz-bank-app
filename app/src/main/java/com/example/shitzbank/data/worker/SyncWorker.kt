package com.example.shitzbank.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.shitzbank.data.repository.TransactionRepositoryImpl // Используем реализацию напрямую
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

// Необходимо для инъекции зависимостей в Worker
@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    // Инжектируем репозиторий, который содержит логику синхронизации
    private val transactionRepository: TransactionRepositoryImpl // Используем конкретную реализацию
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        // Логика синхронизации
        try {
            val syncedTransactions = transactionRepository.syncPendingTransactions()
            if (syncedTransactions.isNotEmpty()) {
                println("SyncWorker: Successfully synced ${syncedTransactions.size} pending transactions.")
            } else {
                println("SyncWorker: No pending transactions to sync or all synced.")
            }
            Result.success()
        } catch (e: Exception) {
            println("SyncWorker: Failed to sync pending transactions: ${e.message}")
            if (e is IOException) { // Например, проблемы с сетью
                Result.retry() // Повторить попытку
            } else {
                Result.failure() // Другие ошибки, которые, возможно, не решатся повторной попыткой
            }
        }
    }

    companion object {
        const val WORK_NAME = "TransactionSyncWorker"
    }
}