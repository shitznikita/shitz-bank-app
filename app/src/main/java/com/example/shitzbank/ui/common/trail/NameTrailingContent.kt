package com.example.shitzbank.ui.common.trail

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.example.shitzbank.R
import com.example.shitzbank.ui.common.composable.CommonText
import com.example.shitzbank.ui.common.composable.TrailingContent

@Composable
fun NameTrailingContent(
    name: String
) {
    TrailingContent(
        content = {
            CommonText(
                text = name,
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.bodyLarge
            )
        },
        icon = {
            Icon(
                ImageVector.vectorResource(R.drawable.drill_in),
                null
            )
        }
    )
}