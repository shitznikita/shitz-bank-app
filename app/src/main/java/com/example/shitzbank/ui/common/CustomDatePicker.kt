package com.example.shitzbank.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.shitzbank.R

/**
 * Composable-функция для отображения настраиваемого диалога выбора даты (DatePickerDialog).
 *
 * Предоставляет улучшенный пользовательский интерфейс для выбора даты, включая
 * кнопки "Очистить", "Отмена" и "ОК", а также кастомизированные цвета.
 *
 * @param datePickerState Состояние [DatePickerState], которое управляет выбранной датой.
 * @param onDateSelected Лямбда-функция, вызываемая при подтверждении выбора даты.
 * Принимает [Long?] - выбранную дату в миллисекундах с начала эпохи, или `null`, если дата очищена.
 * @param onDismiss Лямбда-функция, вызываемая при отмене или закрытии диалога.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDatePicker(
    datePickerState: DatePickerState,
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit,
) {
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis = null
                }) {
                    CommonText(
                        text = stringResource(R.string.clear),
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TextButton(onClick = onDismiss) {
                        CommonText(
                            text = stringResource(R.string.cancel),
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                    TextButton(onClick = {
                        onDateSelected(datePickerState.selectedDateMillis)
                        onDismiss()
                    }) {
                        CommonText(
                            text = stringResource(R.string.ok),
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
            }
        },
        colors =
            DatePickerDefaults.colors(
                containerColor = MaterialTheme.colorScheme.secondary,
            ),
    ) {
        DatePicker(
            state = datePickerState,
            colors =
                DatePickerDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    subheadContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    navigationContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    weekdayContentColor = MaterialTheme.colorScheme.onSurface,
                    dayContentColor = MaterialTheme.colorScheme.onSurface,
                    selectedDayContentColor = MaterialTheme.colorScheme.onSurface,
                ),
            title = null,
            headline = null,
            showModeToggle = false,
        )
    }
}
