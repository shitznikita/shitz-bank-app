package com.example.shitzbank.ui.common.composable

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun PercentageDisplay(
    percentage: String,
    modifier: Modifier = Modifier,
) {
    CommonText(
        modifier = modifier,
        text = percentage,
        textAlign = TextAlign.Start,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onPrimary,
    )
}