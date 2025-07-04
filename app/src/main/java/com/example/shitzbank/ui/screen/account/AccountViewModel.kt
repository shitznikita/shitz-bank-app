package com.example.shitzbank.ui.screen.account

import androidx.lifecycle.viewModelScope
import com.example.shitzbank.common.ResultState
import com.example.shitzbank.common.network.ConnectionStatus
import com.example.shitzbank.common.network.NetworkMonitor
import com.example.shitzbank.common.network.NetworkMonitorViewModel
import com.example.shitzbank.domain.model.Account
import com.example.shitzbank.domain.usecase.GetDefaultAccountUseCase
import com.example.shitzbank.domain.usecase.UpdateAccountByIdUseCase
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
        private val updateAccountByIdUseCase: UpdateAccountByIdUseCase,
        networkMonitor: NetworkMonitor,
    ) : NetworkMonitorViewModel(networkMonitor) {
        private val _accountState = MutableStateFlow<ResultState<Account>>(ResultState.Loading)
        val accountState: StateFlow<ResultState<Account>> = _accountState.asStateFlow()

        private val _editableAccountName = MutableStateFlow<String>("")
        val editableAccountName: StateFlow<String> = _editableAccountName.asStateFlow()

        private val _showEditDialog = MutableStateFlow(false)
        val showEditDialog: StateFlow<Boolean> = _showEditDialog.asStateFlow()

        private var currentEditableAccount: Account? = null

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
                currentEditableAccount = account
                _editableAccountName.value = account.name
            }
        }

        fun showEditAccountDialog(show: Boolean) {
            _showEditDialog.value = show
        }

        fun onAccountNameChanged(newName: String) {
            _editableAccountName.value = newName
        }

        fun saveAccountChanges() {
            currentEditableAccount?.let { account ->
                viewModelScope.launch {
                    if (networkStatus.value is ConnectionStatus.Unavailable) {
                        return@launch
                    }

                    _accountState.value = ResultState.Loading

                    val editAccount = updateAccountByIdUseCase.execute(
                        id = account.id,
                        name = _editableAccountName.value,
                        account.balance, account.currency
                    )

                    _accountState.value = ResultState.Success(editAccount)
                    currentEditableAccount = editAccount
                    _showEditDialog.value = false
                }
            }
        }
    }
