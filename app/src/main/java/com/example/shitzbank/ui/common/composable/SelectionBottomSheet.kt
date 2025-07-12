package com.example.shitzbank.ui.common.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.shitzbank.R

@Composable
fun <T> SelectionBottomSheet(
    itemsList: List<T>,
    itemContent: @Composable (T, Modifier) -> Unit,
    onItemClick: (T) -> Unit,
    onDismiss: () -> Unit
) {
    Column {
        CommonLazyColumn(
            modifier = Modifier.weight(1f, fill = false),
            itemsList = itemsList,
            itemTemplate = { item ->
                itemContent(
                    item,
                    Modifier.height(68.dp)
                        .clickable { onItemClick(item) }
                )
            }
        )

        CommonListItem(
            modifier = Modifier.height(68.dp)
                .clickable(onClick = onDismiss),
            lead = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_cancel),
                    contentDescription = stringResource(R.string.cancel),
                    tint = MaterialTheme.colorScheme.background
                )
            },
            content = {
                CommonText(
                    text = stringResource(R.string.cancel),
                    color = MaterialTheme.colorScheme.background,
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            backgroundColor = MaterialTheme.colorScheme.error
        )
    }
}