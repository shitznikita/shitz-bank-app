package com.example.shitzbank.domain.repository

import com.example.shitzbank.domain.model.Category

interface CategoryRepository {

    suspend fun getCategories(): List<Category>

}