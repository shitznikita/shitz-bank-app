package com.example.shitzbank.ui.screen.categories

import androidx.lifecycle.viewModelScope
import com.example.shitzbank.common.ResultState
import com.example.shitzbank.common.network.ConnectionStatus
import com.example.shitzbank.common.network.NetworkMonitor
import com.example.shitzbank.common.network.NetworkMonitorViewModel
import com.example.shitzbank.domain.model.Category
import com.example.shitzbank.domain.usecase.categories.GetCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class CategoriesViewModel
    @Inject
    constructor(
        private val getCategoriesUseCase: GetCategoriesUseCase,
        networkMonitor: NetworkMonitor,
    ) : NetworkMonitorViewModel(networkMonitor) {
        private val _categoriesState = MutableStateFlow<ResultState<List<Category>>>(ResultState.Loading)
        val categoriesState: StateFlow<ResultState<List<Category>>> = _categoriesState.asStateFlow()

        private val _searchQuery = MutableStateFlow("")
        val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

        private var allCategories: List<Category> = emptyList()

        private var searchJob: Job? = null

        init {
            viewModelScope.launch {
//                networkStatus.collect { status ->
//                    if (status is ConnectionStatus.Available) {
//                        loadCategories()
//                    }
//                }
                loadCategories()
            }

            _searchQuery
                .debounce(300L)
                .distinctUntilChanged()
                .onEach { query ->
                    filterCategories(query)
                }
                .launchIn(viewModelScope)
        }

        fun loadCategories() {
            viewModelScope.launch {
//                if (networkStatus.value is ConnectionStatus.Unavailable) {
//                    return@launch
//                }

                _categoriesState.value = ResultState.Loading

                val categories = getCategoriesUseCase.execute()
                allCategories = categories
                _categoriesState.value = ResultState.Success(categories)
            }
        }

        fun onSearchQueryChanged(query: String) {
            _searchQuery.value = query
        }

        private fun filterCategories(query: String) {
            searchJob?.cancel()
            searchJob = viewModelScope.launch {
                val filteredList = if (query.isBlank()) {
                    allCategories
                } else {
                    allCategories.filter { category ->
                        category.name.contains(query, ignoreCase = true) ||
                                category.emoji.contains(query, ignoreCase = true)
                    }
                }
                _categoriesState.value = ResultState.Success(filteredList)
            }
        }
    }
