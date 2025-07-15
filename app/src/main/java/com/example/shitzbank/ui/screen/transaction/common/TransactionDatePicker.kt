package com.example.shitzbank.ui.screen.transaction.common

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import com.example.shitzbank.common.utils.datetime.toEpochDayMillis
import com.example.shitzbank.common.utils.datetime.toLocalDate
import com.example.shitzbank.ui.common.picker.CustomDatePicker
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionDatePicker(
    currentDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = currentDate.toEpochDayMillis()
    )

    CustomDatePicker(
        datePickerState = datePickerState,
        onDateSelected = { newDateMillis ->
            if (newDateMillis != null) {
                onDateSelected(newDateMillis.toLocalDate())
            } else {
                onDateSelected(LocalDate.now().withDayOfMonth(1))
            }
        },
        onDismiss = onDismiss,
    )
}