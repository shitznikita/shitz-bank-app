package com.example.shitzbank.ui.screen.transaction.common.sheet

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.example.shitzbank.domain.model.Category
import com.example.shitzbank.ui.common.composable.CommonListItem
import com.example.shitzbank.ui.common.composable.CommonText
import com.example.shitzbank.ui.common.composable.LeadIcon
import com.example.shitzbank.ui.common.composable.SelectionBottomSheet

@Composable
fun CategorySelectionBottomSheet(
    categories: List<Category>,
    onCategorySelected: (Category) -> Unit,
    onDismiss: () -> Unit
) {
    SelectionBottomSheet(
        itemsList = categories,
        itemContent = { category, modifier ->
            CommonListItem(
                modifier = modifier,
                lead = {
                    LeadIcon(
                        label = category.emoji,
                        backgroundColor = MaterialTheme.colorScheme.secondary
                    )
                },
                content = {
                    CommonText(
                        text = category.name,
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            )
        },
        onItemClick = onCategorySelected,
        onDismiss = onDismiss
    )
}