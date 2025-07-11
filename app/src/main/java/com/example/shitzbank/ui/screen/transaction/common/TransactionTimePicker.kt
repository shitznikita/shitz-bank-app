package com.example.shitzbank.ui.screen.transaction.common

import android.app.TimePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.shitzbank.R
import java.time.LocalTime
import androidx.compose.material3.TextButton
import com.example.shitzbank.ui.common.composable.CommonText
import androidx.compose.material3.TimePickerDialog

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

    TimePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onTimeSelected(
                    LocalTime.of(timePickerState.hour, timePickerState.minute)
                )
            }) {
                CommonText(
                    text = stringResource(R.string.ok),
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                CommonText(
                    text = stringResource(R.string.cancel),
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    ) {
        TimePicker(state = timePickerState)
    }
}