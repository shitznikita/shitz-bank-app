package com.example.shitzbank.ui.screen.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shitzbank.R
import com.example.shitzbank.ui.common.composable.CommonLazyColumn
import com.example.shitzbank.ui.screen.settings.common.NavigationListItem
import com.example.shitzbank.ui.screen.settings.common.SettingsItem
import com.example.shitzbank.ui.screen.settings.common.SettingsTitles
import com.example.shitzbank.ui.screen.settings.common.SwitchableListItem

@Composable
fun SettingsScreen(viewModel: SettingsViewModel = viewModel()) {
    val titles =
        SettingsTitles(
            darkTheme = stringResource(R.string.settings_dark_theme),
            mainColor = stringResource(R.string.settings_main_color),
            sounds = stringResource(R.string.settings_sounds),
            haptics = stringResource(R.string.settings_haptics),
            codePassword = stringResource(R.string.settings_code_password),
            sync = stringResource(R.string.settings_sync),
            language = stringResource(R.string.settings_language),
            aboutApp = stringResource(R.string.settings_about_app),
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
                        item = item,
                    )
                }
                is SettingsItem.NavigationSetting -> {
                    NavigationListItem(
                        item = item,
                    )
                }
            }
        },
    )
}
