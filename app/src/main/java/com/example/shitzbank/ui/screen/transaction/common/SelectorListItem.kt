package com.example.shitzbank.ui.screen.transaction.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.shitzbank.ui.common.composable.CommonListItem

@Composable
fun SelectorListItem(
    content: @Composable () -> Unit,
    trail: (@Composable () -> Unit),
    onClick: () -> Unit = {}
) {
    CommonListItem(
        modifier = Modifier.height(68.dp)
            .clickable(onClick = onClick),
        content = content,
        trail = trail
    )
}