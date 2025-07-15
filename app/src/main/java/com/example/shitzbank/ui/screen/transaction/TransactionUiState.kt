package com.example.shitzbank.ui.screen.transaction

import com.example.shitzbank.domain.model.AccountBrief
import com.example.shitzbank.domain.model.Category
import java.time.LocalDate
import java.time.LocalTime

data class TransactionUiState(
    val id: Int? = null,
    val account: AccountBrief? = null,
    val category: Category? = null,
    val amount: String? = null,
    val date: LocalDate = LocalDate.now(),
    val time: LocalTime = LocalTime.now(),
    val comment: String? = null,
    val isIncome: Boolean = false,
    val isNewTransaction: Boolean = true
)