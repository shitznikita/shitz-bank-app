package com.example.shitzbank.screen.settings.ui

import androidx.compose.foundation.clickable
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shitzbank.R
import com.example.shitzbank.screen.settings.domain.SettingsItem
import com.example.shitzbank.screen.settings.domain.SettingsTitles
import com.example.shitzbank.ui.common.CommonLazyColumn
import com.example.shitzbank.ui.common.CommonListItem
import com.example.shitzbank.ui.theme.AddButtonGreen

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
        modifier = Modifier,
        content = { Text(item.title) },
        trail = {
            Switch(
                checked = item.isChecked,
                onCheckedChange = { newCheckedState ->
                    viewModel.onSwitchChanged(item.id, newCheckedState)
                },
                colors = SwitchDefaults.colors(
                    checkedTrackColor = AddButtonGreen
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
        modifier = Modifier.clickable {  },
        content = { Text(item.title) },
        trail = {
            Icon(
                ImageVector.vectorResource(R.drawable.ic_chevron_right),
                null
            )
        }
    )
}