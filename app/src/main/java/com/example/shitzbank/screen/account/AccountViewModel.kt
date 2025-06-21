package com.example.shitzbank.screen.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shitzbank.common.ResultState
import com.example.shitzbank.common.network.ConnectionStatus
import com.example.shitzbank.common.network.NetworkMonitor
import com.example.shitzbank.domain.model.Account
import com.example.shitzbank.domain.usecase.GetDefaultAccountUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val getDefaultAccountUseCase: GetDefaultAccountUseCase,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {
    private val _accountState = MutableStateFlow<ResultState<Account>>(ResultState.Loading)
    val accountState: StateFlow<ResultState<Account>> = _accountState.asStateFlow()

    private val _networkStatus = MutableStateFlow<ConnectionStatus>(ConnectionStatus.Unavailable)
    val networkStatus: StateFlow<ConnectionStatus> = _networkStatus.asStateFlow()

    init {
        viewModelScope.launch {
            networkMonitor.connectionStatus.collect { status ->
                _networkStatus.value = status
                if (status is ConnectionStatus.Available) {
                    loadAccount()
                }
            }
        }
    }

    fun loadAccount() {
        viewModelScope.launch {
            if (_networkStatus.value is ConnectionStatus.Unavailable) {
                return@launch
            }

            _accountState.value = ResultState.Loading

            try {
                val account = getDefaultAccountUseCase.execute()
                _accountState.value = ResultState.Success(account!!)
            } catch (e: Exception) {
                _accountState.value = ResultState.Error("Error: ${e.message}")
            }
        }
    }
}