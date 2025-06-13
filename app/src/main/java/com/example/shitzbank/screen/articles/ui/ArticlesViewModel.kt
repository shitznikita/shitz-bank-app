package com.example.shitzbank.screen.articles.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shitzbank.data.MockRepository
import com.example.shitzbank.domain.ResultState
import com.example.shitzbank.domain.model.Category
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ArticlesViewModel : ViewModel() {
    private val repository = MockRepository()

    private val _categories = MutableStateFlow<ResultState<List<Category>>>(ResultState.Loading)
    val categories: StateFlow<ResultState<List<Category>>> = _categories

    init {
        loadArticles()
    }

    private fun loadArticles() {
        viewModelScope.launch {
            try {
                val loadedCategories = repository.getCategories()
                _categories.value = ResultState.Success(loadedCategories)
            } catch (e: Exception) {
                _categories.value = ResultState.Error(e.message, e)
            }
        }
    }
}