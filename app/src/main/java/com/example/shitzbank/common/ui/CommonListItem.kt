package com.example.shitzbank.common.ui

import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun CommonListItem(
    modifier: Modifier = Modifier,
    lead: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit,
    supportingContent: (@Composable () -> Unit)? = null,
    trail: (@Composable () -> Unit)? = null,
    backgroundColor: Color = MaterialTheme.colorScheme.background
) {
    ListItem(
        modifier = modifier,
        leadingContent = lead,
        headlineContent = content,
        supportingContent = supportingContent,
        trailingContent = trail,
        colors = ListItemDefaults.colors(backgroundColor)
    )
    HorizontalDivider()
}