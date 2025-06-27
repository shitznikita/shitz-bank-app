package com.example.shitzbank.ui.screen.account

import androidx.lifecycle.viewModelScope
import com.example.shitzbank.common.ResultState
import com.example.shitzbank.common.network.ConnectionStatus
import com.example.shitzbank.common.network.NetworkMonitor
import com.example.shitzbank.common.network.NetworkMonitorViewModel
import com.example.shitzbank.domain.model.Account
import com.example.shitzbank.domain.usecase.GetDefaultAccountUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel
    @Inject
    constructor(
        private val getDefaultAccountUseCase: GetDefaultAccountUseCase,
        networkMonitor: NetworkMonitor,
    ) : NetworkMonitorViewModel(networkMonitor) {
        private val _accountState = MutableStateFlow<ResultState<Account>>(ResultState.Loading)
        val accountState: StateFlow<ResultState<Account>> = _accountState.asStateFlow()

        init {
            viewModelScope.launch {
                networkStatus.collect { status ->
                    if (status is ConnectionStatus.Available) {
                        loadAccount()
                    }
                }
            }
        }

        fun loadAccount() {
            viewModelScope.launch {
                if (networkStatus.value is ConnectionStatus.Unavailable) {
                    return@launch
                }

                _accountState.value = ResultState.Loading

                val account = getDefaultAccountUseCase.execute()
                _accountState.value = ResultState.Success(account)
            }
        }
    }
