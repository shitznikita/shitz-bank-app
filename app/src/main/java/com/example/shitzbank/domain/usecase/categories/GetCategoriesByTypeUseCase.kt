package com.example.shitzbank.domain.usecase.categories

import com.example.shitzbank.domain.model.Category
import com.example.shitzbank.domain.repository.CategoryRepository
import javax.inject.Inject

class GetCategoriesByTypeUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {

    suspend fun execute(isIncome: Boolean): List<Category> {
        return categoryRepository.getCategoriesByType(isIncome)
    }

}