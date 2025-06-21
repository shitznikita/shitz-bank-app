package com.example.shitzbank.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun <T> CommonLazyColumn(
    modifier: Modifier = Modifier,
    topItem: (@Composable () -> Unit)? = null,
    itemsList: List<T>,
    itemTemplate: @Composable (T) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        topItem?.let { it() }
        LazyColumn {
            itemsIndexed(itemsList) { index, item ->
                itemTemplate(item)
            }
        }
    }
}