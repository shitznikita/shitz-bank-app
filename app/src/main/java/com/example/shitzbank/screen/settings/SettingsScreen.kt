package com.example.shitzbank.screen.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shitzbank.R
import com.example.shitzbank.common.ui.CommonLazyColumn
import com.example.shitzbank.common.ui.CommonListItem
import com.example.shitzbank.common.ui.CommonText

@Composable
fun SettingsScreen(viewModel: SettingsViewModel = viewModel()) {
    val titles = SettingsTitles(
        darkTheme = stringResource(R.string.settings_dark_theme),
        mainColor = stringResource(R.string.settings_main_color),
        sounds = stringResource(R.string.settings_sounds),
        haptics = stringResource(R.string.settings_haptics),
        codePassword = stringResource(R.string.settings_code_password),
        sync = stringResource(R.string.settings_sync),
        language = stringResource(R.string.settings_language),
        aboutApp = stringResource(R.string.settings_about_app)
    )

    val settingsState by viewModel.settingsState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadSettings(titles)
    }

    CommonLazyColumn(
        itemsList = settingsState.items,
        itemTemplate = { item ->
            when (item) {
                is SettingsItem.SwitchSetting -> {
                    SwitchableListItem(
                        viewModel = viewModel,
                        item = item
                    )
                }
                is SettingsItem.NavigationSetting -> {
                    NavigationListItem(
                        item = item
                    )
                }
            }
        }
    )
}

@Composable
fun SwitchableListItem(
    viewModel: SettingsViewModel,
    item: SettingsItem.SwitchSetting
) {
    CommonListItem(
        modifier = Modifier.height(56.dp),
        content = {
            CommonText(
                text = item.title,
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.bodyLarge
            )
        },
        trail = {
            Switch(
                checked = item.isChecked,
                onCheckedChange = { newCheckedState ->
                    viewModel.onSwitchChanged(item.id, newCheckedState)
                },
                colors = SwitchDefaults.colors(
                    checkedTrackColor = MaterialTheme.colorScheme.primary,
                    checkedThumbColor = MaterialTheme.colorScheme.background
                )
            )
        }
    )
}

@Composable
fun NavigationListItem(
    item: SettingsItem.NavigationSetting
) {
    CommonListItem(
        modifier = Modifier.height(56.dp).clickable {  },
        content = {
            CommonText(
                text = item.title,
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.bodyLarge
            )
        },
        trail = {
            Icon(
                ImageVector.vectorResource(R.drawable.ic_chevron_right),
                null
            )
        }
    )
}