package com.example.shitzbank.ui.screen.account.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.shitzbank.ui.common.composable.CommonListItem

@Composable
fun AccountListItem(
    lead: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit,
    trail: (@Composable () -> Unit),
    onClick: () -> Unit = {}
) {
    CommonListItem(
        modifier =
            Modifier.background(MaterialTheme.colorScheme.secondary)
                .clickable(onClick = onClick),
        lead = lead,
        content = content,
        trail = trail,
        backgroundColor = MaterialTheme.colorScheme.secondary,
    )
}