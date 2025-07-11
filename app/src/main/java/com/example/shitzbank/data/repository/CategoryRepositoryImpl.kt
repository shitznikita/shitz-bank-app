package com.example.shitzbank.data.repository

import com.example.shitzbank.common.CoroutineDispatchers
import com.example.shitzbank.common.network.retryWithBackoff
import com.example.shitzbank.data.network.ShmrFinanceApi
import com.example.shitzbank.domain.model.Category
import com.example.shitzbank.domain.repository.CategoryRepository
import kotlinx.coroutines.withContext
import okio.IOException
import java.net.UnknownHostException
import javax.inject.Inject

class CategoryRepositoryImpl
    @Inject
    constructor(
        private val apiService: ShmrFinanceApi,
        private val coroutineDispatchers: CoroutineDispatchers,
    ) : CategoryRepository {
        override suspend fun getCategories(): List<Category> {
            return withContext(coroutineDispatchers.io) {
                try {
                    val categoriesDto =
                        retryWithBackoff {
                            apiService.getCategories()
                        }
                    categoriesDto.map { it.toDomain() }
                } catch (e: UnknownHostException) {
                    println("No internet connection or unknown host: ${e.message}")
                    emptyList()
                } catch (e: IOException) {
                    println("Network error: ${e.message}")
                    emptyList()
                }
            }
        }

        override suspend fun getCategoriesByType(
            isIncome: Boolean
        ): List<Category> {
            return withContext(coroutineDispatchers.io) {
                try {
                    val categoriesDto =
                        retryWithBackoff {
                            apiService.getCategoriesByType(isIncome)
                        }
                    categoriesDto.map { it.toDomain() }
                } catch (e: UnknownHostException) {
                    println("No internet connection or unknown host: ${e.message}")
                    emptyList()
                } catch (e: IOException) {
                    println("Network error: ${e.message}")
                    emptyList()
                }
            }
        }
    }
