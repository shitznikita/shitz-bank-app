package com.example.shitzbank.domain.usecase

import com.example.shitzbank.common.network.retryWithBackoff
import com.example.shitzbank.domain.model.Category
import com.example.shitzbank.domain.repository.CategoryRepository
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {

    suspend fun execute(): List<Category> {
        return retryWithBackoff {
            categoryRepository.getCategories()
        }
    }

}