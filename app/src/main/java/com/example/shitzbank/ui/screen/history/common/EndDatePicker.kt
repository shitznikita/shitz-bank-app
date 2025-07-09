package com.example.shitzbank.ui.screen.history.common

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import com.example.shitzbank.common.utils.datetime.toEpochDayMillis
import com.example.shitzbank.common.utils.datetime.toLocalDate
import com.example.shitzbank.ui.common.composable.CustomDatePicker
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EndDatePicker(
    currentStartDate: LocalDate,
    currentEndDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit,
) {
    val endDatePickerState =
        rememberDatePickerState(
            initialSelectedDateMillis = currentEndDate.toEpochDayMillis(),
            selectableDates =
                object : SelectableDates {
                    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                        val selectedDate = Instant.ofEpochMilli(utcTimeMillis).atZone(ZoneId.systemDefault()).toLocalDate()
                        return !selectedDate.isBefore(currentStartDate)
                    }
                },
        )

    CustomDatePicker(
        datePickerState = endDatePickerState,
        onDateSelected = { newDateMillis ->
            if (newDateMillis != null) {
                onDateSelected(newDateMillis.toLocalDate())
            } else {
                onDateSelected(LocalDate.now())
            }
        },
        onDismiss = onDismiss,
    )
}