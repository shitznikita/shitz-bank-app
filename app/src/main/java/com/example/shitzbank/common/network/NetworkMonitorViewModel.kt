package com.example.shitzbank.common.network

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class NetworkMonitorViewModel(
    private val networkMonitor: NetworkMonitor,
) : ViewModel() {
    private val _networkStatus = MutableStateFlow<ConnectionStatus>(ConnectionStatus.Unavailable)
    val networkStatus: StateFlow<ConnectionStatus> = _networkStatus.asStateFlow()

    init {
        viewModelScope.launch {
            networkMonitor.connectionStatus.collect { status ->
                _networkStatus.value = status
            }
        }
    }
}
