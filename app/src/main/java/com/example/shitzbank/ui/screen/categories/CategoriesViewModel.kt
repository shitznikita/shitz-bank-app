package com.example.shitzbank.ui.screen.categories

import androidx.lifecycle.viewModelScope
import com.example.shitzbank.common.ResultState
import com.example.shitzbank.common.network.ConnectionStatus
import com.example.shitzbank.common.network.NetworkMonitor
import com.example.shitzbank.common.network.NetworkMonitorViewModel
import com.example.shitzbank.domain.model.Category
import com.example.shitzbank.domain.usecase.GetCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel
    @Inject
    constructor(
        private val getCategoriesUseCase: GetCategoriesUseCase,
        networkMonitor: NetworkMonitor,
    ) : NetworkMonitorViewModel(networkMonitor) {
        private val _categoriesState = MutableStateFlow<ResultState<List<Category>>>(ResultState.Loading)
        val categoriesState: StateFlow<ResultState<List<Category>>> = _categoriesState.asStateFlow()

        init {
            viewModelScope.launch {
                networkStatus.collect { status ->
                    if (status is ConnectionStatus.Available) {
                        loadCategories()
                    }
                }
            }
        }

        fun loadCategories() {
            viewModelScope.launch {
                if (networkStatus.value is ConnectionStatus.Unavailable) {
                    return@launch
                }

                _categoriesState.value = ResultState.Loading

                val categories = getCategoriesUseCase.execute()
                _categoriesState.value = ResultState.Success(categories)
            }
        }
    }
