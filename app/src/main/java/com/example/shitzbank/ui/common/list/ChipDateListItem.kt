package com.example.shitzbank.ui.common.list

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.shitzbank.common.utils.datetime.formatDate
import com.example.shitzbank.ui.common.composable.CommonListItem
import com.example.shitzbank.ui.common.composable.CommonText
import java.time.LocalDate

@Composable
fun ChipDateListItem(
    modifier: Modifier,
    onClick: () -> Unit,
    content: String,
    date: LocalDate,
    backgroundColor: Color = MaterialTheme.colorScheme.secondary
) {
    CommonListItem(
        modifier = modifier,
        backgroundColor = backgroundColor,
        content = {
            CommonText(
                text = content,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimary,
            )
        },
        trail = {
            SuggestionChip(
                onClick = onClick,
                label = {
                    CommonText(
                        text = formatDate(date),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                },
                colors = SuggestionChipDefaults.suggestionChipColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    labelColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(16.dp),
                border = SuggestionChipDefaults.suggestionChipBorder(
                    enabled = false
                )
            )
        },
    )
}