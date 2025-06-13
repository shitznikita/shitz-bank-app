package com.example.shitzbank.ui.common

import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun CommonListItem(
    modifier: Modifier = Modifier,
    lead: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit,
    trail: (@Composable () -> Unit)? = null,
    backgroundColor: Color = Color.White
) {
    ListItem(
        modifier = modifier,
        leadingContent = lead,
        headlineContent = content,
        trailingContent = trail,
        colors = ListItemDefaults.colors(backgroundColor)
    )
    HorizontalDivider()
}