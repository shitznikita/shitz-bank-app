package com.example.shitzbank.ui.screen.settings.common

/**
 * Запечатанный класс, представляющий различные типы элементов, которые могут быть отображены на экране настроек.
 * Это позволяет гибко определять различные виды настроек (например, переключатели, навигационные элементы).
 */
sealed class SettingsItem {
    abstract val id: String
    abstract val title: String

    /**
     * Элемент настройки с переключателем (toggle switch).
     *
     * @property id Уникальный идентификатор настройки.
     * @property title Заголовок настройки.
     * @property isChecked Текущее состояние переключателя (включен или выключен).
     */
    data class SwitchSetting(
        override val id: String,
        override val title: String,
        val isChecked: Boolean,
    ) : SettingsItem()

    /**
     * Элемент настройки, который ведет к другому экрану или разделу (навигационный элемент).
     *
     * @property id Уникальный идентификатор настройки.
     * @property title Заголовок настройки.
     */
    data class NavigationSetting(
        override val id: String,
        override val title: String,
    ) : SettingsItem()
}

/**
 * Класс данных, содержащий строковые заголовки для различных настроек.
 *
 * Используется для централизованного управления текстами настроек,
 * что облегчает их локализацию и изменение.
 *
 * @property darkTheme Заголовок для настройки "Темная тема".
 * @property mainColor Заголовок для настройки "Основной цвет".
 * @property sounds Заголовок для настройки "Звуки".
 * @property haptics Заголовок для настройки "Вибрация".
 * @property codePassword Заголовок для настройки "Код-пароль".
 * @property sync Заголовок для настройки "Синхронизация".
 * @property language Заголовок для настройки "Язык".
 * @property aboutApp Заголовок для настройки "О приложении".
 */
data class SettingsTitles(
    val darkTheme: String,
    val mainColor: String,
    val sounds: String,
    val haptics: String,
    val codePassword: String,
    val sync: String,
    val language: String,
    val aboutApp: String,
)
