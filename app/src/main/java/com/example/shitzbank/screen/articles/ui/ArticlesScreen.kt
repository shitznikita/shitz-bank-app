package com.example.shitzbank.screen.articles.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import com.example.shitzbank.ui.common.CommonLazyColumn
import com.example.shitzbank.R
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import com.example.shitzbank.ui.common.CommonListItem
import com.example.shitzbank.ui.common.LeadIcon
import com.example.shitzbank.ui.common.ResultStateHandler
import com.example.shitzbank.ui.theme.LightGreen
import com.example.shitzbank.ui.viewmodel.MainViewModel

@Composable
fun ArticlesScreen(viewModel: MainViewModel) {
    val mock by viewModel.categories.collectAsState()

    var searchQuery by remember { mutableStateOf("") }

    ResultStateHandler(
        state = mock,
        onSuccess = { data ->
            CommonLazyColumn(
                topItem = {
                    SearchInput(
                        modifier = Modifier.fillMaxWidth()
                            .background(color = MaterialTheme.colorScheme.surfaceContainer),
                        query = searchQuery,
                        onQueryChange = { searchQuery = it },
                        onSearchClick = {}
                    )
                },
                itemsList = data,
                itemTemplate = { item ->
                    CommonListItem(
                        modifier = Modifier.height(70.dp),
                        lead = { LeadIcon(label = item.icon, backgroundColor = LightGreen) },
                        content = {
                            Box {
                                Text(item.title)
                            }
                        }
                    )
                }
            )
        }
    )
}

@Composable
fun SearchInput(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier,
        shape = RectangleShape,
        placeholder = {
            Text(
                stringResource(R.string.articles_find)
            )
        },
        trailingIcon = {
            IconButton(onClick = onSearchClick) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Find"
                )
            }
        },
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            cursorColor = MaterialTheme.colorScheme.onSecondary,
            unfocusedBorderColor = MaterialTheme.colorScheme.surfaceContainer,
            focusedBorderColor = MaterialTheme.colorScheme.surfaceContainer
        )
    )
}