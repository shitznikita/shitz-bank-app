package com.example.shitzbank.ui.screen.transaction.common.selector

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.shitzbank.R
import com.example.shitzbank.common.utils.datetime.formatDate
import com.example.shitzbank.ui.common.composable.CommonText
import com.example.shitzbank.ui.screen.transaction.common.SelectorListItem
import java.time.LocalDate

@Composable
fun DateSelector(
    selectedDate: LocalDate,
    onClick: () -> Unit
) {
    SelectorListItem(
        content = {
            CommonText(
                text = stringResource(R.string.date),
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.bodyLarge
            )
        },
        trail = {
            CommonText(
                text = formatDate(selectedDate),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimary,
            )
        },
        onClick = onClick
    )
}