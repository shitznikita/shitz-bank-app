package com.example.shitzbank.screen.settings

sealed class SettingsItem {
    abstract val id: String
    abstract val title: String

    data class SwitchSetting(
        override val id: String,
        override val title: String,
        val isChecked: Boolean
    ) : SettingsItem()

    data class NavigationSetting(
        override val id: String,
        override val title: String
    ) : SettingsItem()
}

data class SettingsTitles(
    val darkTheme: String,
    val mainColor: String,
    val sounds: String,
    val haptics: String,
    val codePassword: String,
    val sync: String,
    val language: String,
    val aboutApp: String
)