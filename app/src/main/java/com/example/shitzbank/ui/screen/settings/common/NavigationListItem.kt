package com.example.shitzbank.ui.screen.settings.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.shitzbank.R
import com.example.shitzbank.ui.common.CommonListItem
import com.example.shitzbank.ui.common.CommonText

@Composable
fun NavigationListItem(item: SettingsItem.NavigationSetting) {
    CommonListItem(
        modifier = Modifier.height(56.dp).clickable { },
        content = {
            CommonText(
                text = item.title,
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.bodyLarge,
            )
        },
        trail = {
            Icon(
                ImageVector.vectorResource(R.drawable.ic_chevron_right),
                null,
            )
        },
    )
}