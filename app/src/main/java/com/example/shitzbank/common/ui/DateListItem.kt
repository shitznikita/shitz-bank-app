package com.example.shitzbank.common.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.shitzbank.common.utils.formatDate
import java.time.LocalDate

@Composable
fun DateListItem(
    modifier: Modifier,
    content: String,
    date: LocalDate
) {
    CommonListItem(
        modifier = modifier,
        backgroundColor = MaterialTheme.colorScheme.secondary,
        content = {
            CommonText(
                text = content,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
        },
        trail = {
            CommonText(
                text = formatDate(date),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    )
}