package com.example.shitzbank.data.repository

import android.util.Log
import com.example.shitzbank.common.CoroutineDispatchers
import com.example.shitzbank.common.network.ConnectionStatus
import com.example.shitzbank.common.network.NetworkMonitor
import com.example.shitzbank.common.network.retryWithBackoff
import com.example.shitzbank.data.local.dao.AccountDao
import com.example.shitzbank.data.local.dao.CategoryDao
import com.example.shitzbank.data.network.ShmrFinanceApi
import com.example.shitzbank.domain.model.Category
import com.example.shitzbank.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import okio.IOException
import java.net.UnknownHostException
import javax.inject.Inject

class CategoryRepositoryImpl
    @Inject
    constructor(
        private val apiService: ShmrFinanceApi,
        private val coroutineDispatchers: CoroutineDispatchers,
        private val categoryDao: CategoryDao, // Inject CategoryDao
        private val networkMonitor: NetworkMonitor, // Inject NetworkMonitor
    ) : CategoryRepository {
    private suspend fun isNetworkAvailable(): Boolean {
        return networkMonitor.connectionStatus.firstOrNull() is ConnectionStatus.Available
    }

    override suspend fun getCategories(): List<Category> {
        return withContext(coroutineDispatchers.io) {
            if (isNetworkAvailable()) {
                try {
                    val categoriesDto = retryWithBackoff {
                        apiService.getCategories()
                    }
                    // Clear old categories and insert new ones for full sync
                    categoryDao.deleteAllCategories() // Or smarter merge logic
                    categoryDao.insertCategoryEntities(categoriesDto.map { it.toEntity() }) // Assuming toEntity mapper from DTO to Entity
                    Log.d("CategoryRepo", "Categories synchronized from API and saved to Room.")
                } catch (e: Exception) {
                    Log.e("CategoryRepo", "Failed to synchronize categories from API: ${e.message}. Using cached data.", e)
                    // This catch block handles UnknownHostException, IOException, etc.
                }
            } else {
                Log.d("CategoryRepo", "No network available for getCategories. Using cached data.")
            }
            // Always return data from Room
            val categories = categoryDao.getCategories()
            Log.d("CategoryRepo", "Loaded ${categories.size} categories from Room.")
            categories
        }
    }

    override suspend fun getCategoriesByType(
        isIncome: Boolean
    ): List<Category> {
        return withContext(coroutineDispatchers.io) {
            // For filtered queries, first try to sync all, then filter locally.
            // Alternatively, you could have a separate API endpoint for filtered categories,
            // but usually syncing all and filtering locally is more robust for offline-first.
            if (isNetworkAvailable()) {
                try {
                    val categoriesDto = retryWithBackoff {
                        // Sync all categories first to ensure local cache is up-to-date
                        apiService.getCategories()
                    }
                    categoryDao.deleteAllCategories()
                    categoryDao.insertCategoryEntities(categoriesDto.map { it.toEntity() })
                    Log.d("CategoryRepo", "All categories synchronized from API for type filter.")
                } catch (e: Exception) {
                    Log.e("CategoryRepo", "Failed to synchronize all categories from API: ${e.message}. Using cached data for filter.", e)
                }
            } else {
                Log.d("CategoryRepo", "No network for getCategoriesByType. Using cached data.")
            }
            // Always return filtered data from Room
            val categories = categoryDao.getCategoryEntitiesByType(isIncome).map { it.toDomain() }
            Log.d("CategoryRepo", "Loaded ${categories.size} categories by type (isIncome=$isIncome) from Room.")
            categories
        }
    }
    }
