package com.example.shitzbank

import android.app.Application
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.NetworkType
import com.example.shitzbank.data.worker.SyncWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class ShmrFinanceApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: androidx.hilt.work.HiltWorkerFactory // Полное имя класса

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        setupPeriodicSyncWork()
    }

    private fun setupPeriodicSyncWork() {
        val syncWorkRequest = PeriodicWorkRequestBuilder<SyncWorker>(
            repeatInterval = 2, // 2 часа
            repeatIntervalTimeUnit = TimeUnit.HOURS
        )
            .setConstraints(
                androidx.work.Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED) // Требуется интернет
                    .build()
            )
            .setBackoffCriteria( // Рекомендуется для повторных попыток
                androidx.work.BackoffPolicy.LINEAR,
                androidx.work.PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS,
                TimeUnit.MILLISECONDS
            )
            .addTag(SyncWorker.WORK_NAME) // Добавляем тег для идентификации
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            SyncWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP, // Если задача уже запланирована, оставляем существующую
            syncWorkRequest
        )
        println("WorkManager: Periodic sync work scheduled.")
    }
}