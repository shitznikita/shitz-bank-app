package com.example.shitzbank.ui.screen.transaction.common.selector

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.shitzbank.R
import com.example.shitzbank.common.utils.datetime.formatTime
import com.example.shitzbank.ui.common.composable.CommonText
import com.example.shitzbank.ui.screen.transaction.common.SelectorListItem
import java.time.LocalTime

@Composable
fun TimeSelector(
    selectedTime: LocalTime,
    onClick: () -> Unit
) {
    SelectorListItem(
        content = {
            CommonText(
                text = stringResource(R.string.time),
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.bodyLarge
            )
        },
        trail = {
            CommonText(
                text = formatTime(selectedTime),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimary,
            )
        },
        onClick = onClick
    )
}