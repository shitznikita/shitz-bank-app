package com.example.shitzbank.ui.screen.transaction.common

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import java.time.LocalTime
import com.example.shitzbank.ui.common.composable.CustomTimePickerDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionTimePicker(
    currentTime: LocalTime,
    onTimeSelected: (LocalTime) -> Unit,
    onDismiss: () -> Unit
) {
    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.hour,
        initialMinute = currentTime.minute,
        is24Hour = true
    )

    CustomTimePickerDialog(
        onDismiss = onDismiss,
        onConfirm = {
            onTimeSelected(
                LocalTime.of(timePickerState.hour, timePickerState.minute)
            )
        }
    ) {
        TimePicker(
            state = timePickerState,
            colors = TimePickerDefaults.colors(
                containerColor = MaterialTheme.colorScheme.secondary,
                clockDialColor = MaterialTheme.colorScheme.surfaceVariant,
                selectorColor = MaterialTheme.colorScheme.primary,
                timeSelectorSelectedContentColor = MaterialTheme.colorScheme.onPrimary,
                timeSelectorUnselectedContentColor = MaterialTheme.colorScheme.onSurface,
                timeSelectorSelectedContainerColor = MaterialTheme.colorScheme.primary,
                periodSelectorSelectedContainerColor = MaterialTheme.colorScheme.secondary,
                periodSelectorSelectedContentColor = MaterialTheme.colorScheme.onPrimary,
                periodSelectorUnselectedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                periodSelectorUnselectedContentColor = MaterialTheme.colorScheme.onSurface
            )
        )
    }
}