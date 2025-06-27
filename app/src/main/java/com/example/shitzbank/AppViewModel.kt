package com.example.shitzbank

import com.example.shitzbank.common.network.NetworkMonitor
import com.example.shitzbank.common.network.NetworkMonitorViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppViewModel
    @Inject
    constructor(
        networkMonitor: NetworkMonitor,
    ) : NetworkMonitorViewModel(networkMonitor)
