package com.example.shitzbank.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.example.shitzbank.R

/**
 * Запечатанный класс, определяющий иконки и текстовые метки для элементов нижней навигационной панели.
 * Каждый объект представляет собой конкретную иконку с связанными ресурсами.
 *
 * @property iconResId Идентификатор ресурса векторной иконки (Drawable).
 * @property labelResId Идентификатор строкового ресурса для метки иконки.
 */
sealed class BottomNavigationIcon(
    val iconResId: Int,
    val labelResId: Int,
) {
    /**
     * Composable-функция для получения [ImageVector] иконки из ресурсов.
     * @return [ImageVector] иконки.
     */
    @Composable
    fun getIcon() = ImageVector.vectorResource(iconResId)

    /**
     * Composable-функция для получения строковой метки иконки из ресурсов.
     * @return [String] метки.
     */
    @Composable
    fun getLabel() = stringResource(labelResId)

    /**
     * Иконка для раздела "Расходы".
     */
    data object ExpensesIcon : BottomNavigationIcon(
        iconResId = R.drawable.ic_downtrend,
        labelResId = R.string.expenses_icon,
    )

    /**
     * Иконка для раздела "Доходы".
     */
    data object IncomesIcon : BottomNavigationIcon(
        iconResId = R.drawable.ic_uptrend,
        labelResId = R.string.incomes_icon,
    )

    /**
     * Иконка для раздела "Счета" (Кошелек).
     */
    data object AccountIcon : BottomNavigationIcon(
        iconResId = R.drawable.ic_wallet,
        labelResId = R.string.wallet_icon,
    )

    /**
     * Иконка для раздела "Категории".
     */
    data object CategoriesIcon : BottomNavigationIcon(
        iconResId = R.drawable.ic_articles,
        labelResId = R.string.articles_icon,
    )

    /**
     * Иконка для раздела "Настройки".
     */
    data object SettingsIcon : BottomNavigationIcon(
        iconResId = R.drawable.ic_settings,
        labelResId = R.string.settings_icon,
    )
}