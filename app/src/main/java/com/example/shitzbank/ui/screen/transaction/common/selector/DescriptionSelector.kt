package com.example.shitzbank.ui.screen.transaction.common.selector

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.shitzbank.R
import com.example.shitzbank.ui.common.composable.CommonText
import com.example.shitzbank.ui.common.trail.NameTrailingContent
import com.example.shitzbank.ui.screen.transaction.common.SelectorListItem

@Composable
fun DescriptionSelector(
    currentComment: String?,
    onClick: () -> Unit
) {
    SelectorListItem(
        content = {
            CommonText(
                text = currentComment ?: "",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.bodyLarge
            )
        },
        trail = {
            NameTrailingContent(
                name = ""
            )
        },
        onClick = onClick
    )
}