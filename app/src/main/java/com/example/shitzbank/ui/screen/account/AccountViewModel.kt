package com.example.shitzbank.ui.screen.account

import androidx.lifecycle.viewModelScope
import com.example.shitzbank.common.ResultState
import com.example.shitzbank.common.network.ConnectionStatus
import com.example.shitzbank.common.network.NetworkMonitor
import com.example.shitzbank.common.network.NetworkMonitorViewModel
import com.example.shitzbank.domain.model.Account
import com.example.shitzbank.domain.usecase.account.GetDefaultAccountUseCase
import com.example.shitzbank.domain.usecase.account.UpdateAccountByIdUseCase
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

        private val _editableAccountName = MutableStateFlow("")
        val editableAccountName: StateFlow<String> = _editableAccountName.asStateFlow()

        private val _showEditNameDialog = MutableStateFlow(false)
        val showEditNameDialog: StateFlow<Boolean> = _showEditNameDialog.asStateFlow()

        private val _showCurrencyBottomSheet = MutableStateFlow(false)
        val showCurrencyBottomSheet: StateFlow<Boolean> = _showCurrencyBottomSheet.asStateFlow()

        private var currentEditableAccount: Account? = null

        init {
            viewModelScope.launch {
//                networkStatus.collect { status ->
//                    if (status is ConnectionStatus.Available) {
//                        loadAccount()
//                    }
//                }
                loadAccount()
            }
        }

        fun loadAccount() {
            viewModelScope.launch {
//                if (networkStatus.value is ConnectionStatus.Unavailable) {
//                    return@launch
//                }

                _accountState.value = ResultState.Loading

                val account = getDefaultAccountUseCase.execute()
                setAccount(account)
            }
        }

        fun showEditNameDialog(show: Boolean) {
            _showEditNameDialog.value = show
        }

        fun showCurrencyBottomSheet(show: Boolean) {
            _showCurrencyBottomSheet.value = show
        }

        fun onAccountNameChanged(newName: String) {
            _editableAccountName.value = newName
        }

        fun saveNameChanges() {
            currentEditableAccount?.let { account ->
                viewModelScope.launch {
//                    if (networkStatus.value is ConnectionStatus.Unavailable) {
//                        return@launch
//                    }

                    val editAccount = updateAccountByIdUseCase.execute(
                        id = account.id,
                        name = _editableAccountName.value,
                        balance = account.balance,
                        currency = account.currency
                    )

                    setAccount(editAccount)
                    _showEditNameDialog.value = false
                }
            }
        }

        fun saveCurrencyChanges(newCurrencyCode: String) {
            currentEditableAccount?.let { account ->
                viewModelScope.launch {
//                    if (networkStatus.value is ConnectionStatus.Unavailable) {
//                        return@launch
//                    }

                    val editAccount = updateAccountByIdUseCase.execute(
                        id = account.id,
                        name = account.name,
                        balance = account.balance,
                        currency = newCurrencyCode
                    )

                    setAccount(editAccount)
                    _showCurrencyBottomSheet.value = false
                }
            }
        }

        private fun setAccount(account: Account) {
            _accountState.value = ResultState.Success(account)
            currentEditableAccount = account
            _editableAccountName.value = account.name
        }
    }
