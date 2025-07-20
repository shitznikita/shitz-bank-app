package com.example.shitzbank.data.repository

import com.example.shitzbank.common.CoroutineDispatchers
import com.example.shitzbank.common.network.ConnectionStatus
import com.example.shitzbank.common.network.NetworkMonitor
import com.example.shitzbank.common.network.retryWithBackoff
import com.example.shitzbank.data.local.dao.CategoryDao
import com.example.shitzbank.data.network.ShmrFinanceApi
import com.example.shitzbank.domain.model.Category
import com.example.shitzbank.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val apiService: ShmrFinanceApi,
    private val coroutineDispatchers: CoroutineDispatchers,
    private val categoryDao: CategoryDao,
    private val networkMonitor: NetworkMonitor,
) : CategoryRepository {
    private suspend fun isNetworkAvailable(): Boolean {
        return networkMonitor.connectionStatus.firstOrNull() is ConnectionStatus.Available
    }

    override suspend fun getCategories(): List<Category> {
        return withContext(coroutineDispatchers.io) {
            if (isNetworkAvailable()) {
                val categoriesDto = retryWithBackoff {
                    apiService.getCategories()
                }
                categoryDao.deleteAllCategories()
                categoryDao.insertCategoryEntities(categoriesDto.map { it.toEntity() })
            }
            val categories = categoryDao.getCategories()
            categories
        }
    }

    override suspend fun getCategoriesByType(
        isIncome: Boolean
    ): List<Category> {
        return withContext(coroutineDispatchers.io) {
            if (isNetworkAvailable()) {
                val categoriesDto = retryWithBackoff {
                    apiService.getCategories()
                }
                categoryDao.deleteAllCategories()
                categoryDao.insertCategoryEntities(categoriesDto.map { it.toEntity() })
            }
            val categories = categoryDao.getCategoryEntitiesByType(isIncome).map { it.toDomain() }
            categories
        }
    }
}
