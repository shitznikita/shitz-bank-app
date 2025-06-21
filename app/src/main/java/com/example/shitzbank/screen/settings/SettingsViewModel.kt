package com.example.shitzbank.screen.settings.ui

import androidx.lifecycle.ViewModel
import com.example.shitzbank.screen.settings.domain.SettingsItem
import com.example.shitzbank.screen.settings.domain.SettingsTitles
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class SettingsState(
    val items: List<SettingsItem> = emptyList()
)

class SettingsViewModel : ViewModel() {
    private val _settingsState = MutableStateFlow(SettingsState())
    val settingsState = _settingsState.asStateFlow()

    fun loadSettings(titles: SettingsTitles) {
        _settingsState.update {
            it.copy(
                items = listOf(
                    SettingsItem.SwitchSetting(
                        id = "dark_theme",
                        title = titles.darkTheme,
                        isChecked = false
                    ),
                    SettingsItem.NavigationSetting(id = "main_color", title = titles.mainColor),
                    SettingsItem.NavigationSetting(id = "sounds", title = titles.sounds),
                    SettingsItem.NavigationSetting(id = "haptics", title = titles.haptics),
                    SettingsItem.NavigationSetting(id = "code_password", title = titles.codePassword),
                    SettingsItem.NavigationSetting(id = "sync", title = titles.sync),
                    SettingsItem.NavigationSetting(id = "language", title = titles.language),
                    SettingsItem.NavigationSetting(id = "about_app", title = titles.aboutApp)
                )
            )
        }
    }

    fun onSwitchChanged(itemId: String, isChecked: Boolean) {
        _settingsState.update { currentState ->
            val updatedItems = currentState.items.map { setting ->
                if (setting.id == itemId && setting is SettingsItem.SwitchSetting) {
                    setting.copy(isChecked = isChecked)
                } else {
                    setting
                }
            }
            currentState.copy(items = updatedItems)
        }
    }
}