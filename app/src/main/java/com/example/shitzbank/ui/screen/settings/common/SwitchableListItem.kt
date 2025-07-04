package com.example.shitzbank.ui.screen.settings.common

import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.shitzbank.ui.common.CommonListItem
import com.example.shitzbank.ui.common.CommonText
import com.example.shitzbank.ui.screen.settings.SettingsViewModel

@Composable
fun SwitchableListItem(
    viewModel: SettingsViewModel,
    item: SettingsItem.SwitchSetting,
) {
    CommonListItem(
        modifier = Modifier.height(56.dp),
        content = {
            CommonText(
                text = item.title,
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.bodyLarge,
            )
        },
        trail = {
            Switch(
                checked = item.isChecked,
                onCheckedChange = { newCheckedState ->
                    viewModel.onSwitchChanged(item.id, newCheckedState)
                },
                colors =
                    SwitchDefaults.colors(
                        checkedTrackColor = MaterialTheme.colorScheme.primary,
                        checkedThumbColor = MaterialTheme.colorScheme.background,
                    ),
            )
        },
    )
}