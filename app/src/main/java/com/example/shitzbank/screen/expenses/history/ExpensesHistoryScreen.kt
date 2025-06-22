package com.example.shitzbank.screen.expenses.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.example.shitzbank.R
import com.example.shitzbank.common.ui.CommonLazyColumn
import com.example.shitzbank.common.ui.CommonListItem
import com.example.shitzbank.common.ui.CommonText
import com.example.shitzbank.common.ui.DateListItem
import com.example.shitzbank.common.ui.LeadIcon
import com.example.shitzbank.common.ui.PriceDisplay
import com.example.shitzbank.common.ui.ResultStateHandler
import com.example.shitzbank.common.ui.TotalAmountListItem
import com.example.shitzbank.common.ui.TrailingContent
import com.example.shitzbank.common.utils.formatDateTime
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ExpensesHistoryScreen(
    viewModel: ExpensesHistoryViewModel = hiltViewModel()
) {
    val expensesState by viewModel.expensesState.collectAsState()
    val totalExpense by viewModel.totalExpense.collectAsState()
    val startDate by viewModel.startDate.collectAsState()
    val endDate by viewModel.endDate.collectAsState()

    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }


    ResultStateHandler(
        state = expensesState,
        onSuccess = { data ->
            CommonLazyColumn(
                topItem = {
                    DateListItem(
                        modifier = Modifier.clickable { showStartDatePicker = true },
                        content = "Начало",
                        date = startDate,
                    )
                    DateListItem(
                        modifier = Modifier.clickable { showEndDatePicker = true },
                        content = "Конец",
                        date = endDate
                    )
                    TotalAmountListItem(totalExpense)
                },
                itemsList = data,
                itemTemplate = { item ->
                    CommonListItem(
                        modifier = Modifier.clickable {  },
                        lead = {
                            LeadIcon(
                                label = item.category.emoji,
                                backgroundColor = MaterialTheme.colorScheme.secondary
                            )
                        },
                        content = {
                            CommonText(
                                text = item.category.name,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        },
                        supportingContent = if (item.comment != null) {
                            {
                                CommonText(
                                    text = item.comment,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSecondary
                                )
                            }
                        } else null,
                        trail = {
                            Column {
                                TrailingContent(
                                    content = {
                                        Column(
                                            horizontalAlignment = Alignment.End
                                        ) {
                                            PriceDisplay(
                                                amount = item.amount,
                                                currency = item.account.currency
                                            )
                                            CommonText(
                                                text = formatDateTime(item.transactionDate),
                                                color = MaterialTheme.colorScheme.onSecondary,
                                                style = MaterialTheme.typography.bodySmall
                                            )
                                        }
                                    },
                                    icon = {
                                        Icon(ImageVector.vectorResource(R.drawable.drill_in), null)
                                    }
                                )
                            }
                        }
                    )
                }
            )
        }
    )

    if (showStartDatePicker) {
        CustomDatePicker(
            selectedDate = startDate.toEpochDayMillis(),
            onDateSelected = { newDateMillis ->
                if (newDateMillis != null) {
                    viewModel.setStartDate(newDateMillis.toLocalDate())
                } else {
                    viewModel.setStartDate(LocalDate.now().withDayOfMonth(1))
                }
                showStartDatePicker = false
            },
            onDismiss = { showStartDatePicker = false }
        )
    }

    if (showEndDatePicker) {
        CustomDatePicker(
            selectedDate = endDate.toEpochDayMillis(),
            onDateSelected = { newDateMillis ->
                if (newDateMillis != null) {
                    viewModel.setEndDate(newDateMillis.toLocalDate())
                } else {
                    viewModel.setEndDate(LocalDate.now())
                }
                showEndDatePicker = false
            },
            onDismiss = { showEndDatePicker = false }
        )
    }
}

fun LocalDate.toEpochDayMillis(): Long {
    return this.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
}

fun Long.toLocalDate(): LocalDate {
    return Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDate()
}

enum class ButtonType {
    OK, CANCEL, CLEAR
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PickerButton(
    buttonType: ButtonType,
    onDismiss: () -> Unit,
    onDateSelected: ((Long?) -> Unit)? = null,
    datePickerState: DatePickerState? = null
) {
    TextButton(onClick = {
        when (buttonType) {
            ButtonType.OK -> onDateSelected?.invoke(datePickerState?.selectedDateMillis)
            ButtonType.CANCEL -> onDismiss()
            ButtonType.CLEAR -> onDateSelected?.invoke(null)
        }
        onDismiss()
    },
        colors = ButtonDefaults.textButtonColors(
            contentColor = MaterialTheme.colorScheme.onPrimary
        )) {
        Text(
            when (buttonType) {
                ButtonType.OK -> "ОК"
                ButtonType.CANCEL -> "Отмена"
                ButtonType.CLEAR -> "Очистить"
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDatePicker(
    selectedDate: Long?,
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = selectedDate)

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            DatePickerButtons(
                onDateSelected = onDateSelected,
                onDismiss = onDismiss,
                datePickerState = datePickerState
            )
        },
        colors = DatePickerDefaults.colors(
            containerColor = MaterialTheme.colorScheme.secondary,
            selectedDayContainerColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        DatePicker(
            state = datePickerState,
            title = null,
            headline = null,
            showModeToggle = false,
            colors = DatePickerDefaults.colors(
                containerColor = MaterialTheme.colorScheme.secondary,
                selectedDayContainerColor = MaterialTheme.colorScheme.primary,
                selectedYearContainerColor = MaterialTheme.colorScheme.primary,
                selectedYearContentColor = MaterialTheme.colorScheme.onPrimary,
                todayContentColor = MaterialTheme.colorScheme.onPrimary,
                todayDateBorderColor = MaterialTheme.colorScheme.onPrimary,
                selectedDayContentColor = MaterialTheme.colorScheme.onPrimary
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerButtons(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit,
    datePickerState: DatePickerState
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Кнопка "Очистить"
        PickerButton(
            buttonType = ButtonType.CLEAR,
            onDismiss = onDismiss,
            onDateSelected = onDateSelected
        )

        Row {
            // Кнопка "Отмена"
            PickerButton(buttonType = ButtonType.CANCEL, onDismiss = onDismiss)

            // Кнопка "ОК"
            PickerButton(
                buttonType = ButtonType.OK,
                onDismiss = onDismiss,
                onDateSelected = onDateSelected,
                datePickerState = datePickerState
            )
        }
    }
}