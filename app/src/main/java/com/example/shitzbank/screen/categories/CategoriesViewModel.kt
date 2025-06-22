package com.example.shitzbank.screen.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shitzbank.common.ResultState
import com.example.shitzbank.common.network.ConnectionStatus
import com.example.shitzbank.common.network.NetworkMonitor
import com.example.shitzbank.domain.model.Category
import com.example.shitzbank.domain.usecase.GetCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {
    private val _categoriesState = MutableStateFlow<ResultState<List<Category>>>(ResultState.Loading)
    val categoriesState: StateFlow<ResultState<List<Category>>> = _categoriesState.asStateFlow()

    private val _networkStatus = MutableStateFlow<ConnectionStatus>(ConnectionStatus.Unavailable)
    val networkStatus: StateFlow<ConnectionStatus> = _networkStatus.asStateFlow()

    init {
        viewModelScope.launch {
            networkMonitor.connectionStatus.collect { status ->
                _networkStatus.value = status
                if (status is ConnectionStatus.Available) {
                    loadCategories()
                }
            }
        }
    }

    fun loadCategories() {
        viewModelScope.launch {
            if (_networkStatus.value is ConnectionStatus.Unavailable) {
                return@launch
            }

            _categoriesState.value = ResultState.Loading

            try {
                val categories = getCategoriesUseCase.execute()
                _categoriesState.value = ResultState.Success(categories)
            } catch (e: Exception) {
                _categoriesState.value = ResultState.Error("Error: ${e.message}")
            }
        }
    }
}