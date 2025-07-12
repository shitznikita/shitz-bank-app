package com.example.shitzbank.ui.screen.transaction.common.selector

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.shitzbank.domain.model.Category
import com.example.shitzbank.ui.common.composable.CommonText
import com.example.shitzbank.ui.common.trail.NameTrailingContent
import com.example.shitzbank.ui.screen.transaction.common.SelectorListItem
import com.example.shitzbank.R

@Composable
fun CategorySelector(
    selectedCategory: Category?,
    onClick: () -> Unit
) {
    SelectorListItem(
        content = {
            CommonText(
                text = stringResource(R.string.category),
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.bodyLarge
            )
        },
        trail = {
            NameTrailingContent(
                name = selectedCategory?.name ?: ""
            )
        },
        onClick = onClick
    )
}