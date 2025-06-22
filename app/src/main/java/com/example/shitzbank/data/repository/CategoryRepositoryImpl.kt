package com.example.shitzbank.data.repository

import com.example.shitzbank.data.network.ShmrFinanceApi
import com.example.shitzbank.domain.model.Category
import com.example.shitzbank.domain.repository.CategoryRepository
import okio.IOException
import java.net.UnknownHostException
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val apiService: ShmrFinanceApi
) : CategoryRepository {

    override suspend fun getCategories(): List<Category> {
        return try {
            apiService.getCategories()
        } catch (e: UnknownHostException) {
            println("No internet connection or unknown host: ${e.message}")
            emptyList()
        } catch (e: IOException) {
            println("Network error: ${e.message}")
            emptyList()
        } catch (e: Exception) {
            println("An unexpected error occurred: ${e.message}")
            emptyList()
        }
    }

}