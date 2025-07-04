package com.example.shitzbank.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.example.shitzbank.R

/**
 * Запечатанный класс, определяющий иконки и связанные маршруты для кнопки "Назад" в навигации.
 *
 * @property iconResId Идентификатор ресурса векторной иконки (Drawable).
 * @property routeResId Идентификатор строкового ресурса для маршрута, куда следует вернуться.
 */
sealed class BackNavigationIcon(
    val iconResId: Int,
    val routeResId: Int,
) {
    /**
     * Composable-функция для получения [ImageVector] иконки из ресурсов.
     * @return [ImageVector] иконки.
     */
    @Composable
    fun getIcon() = ImageVector.vectorResource(iconResId)

    /**
     * Composable-функция для получения строкового маршрута, куда следует вернуться, из ресурсов.
     * @return [String] маршрута.
     */
    @Composable
    fun getRoute() = stringResource(routeResId)

    /**
     * Иконка и маршрут для возврата к экрану расходов из истории расходов.
     */
    data object ExpensesHistoryBack : BackNavigationIcon(
        iconResId = R.drawable.ic_back,
        routeResId = R.string.expenses_route,
    )

    /**
     * Иконка и маршрут для возврата к экрану доходов из истории доходов.
     */
    data object IncomesHistoryBack : BackNavigationIcon(
        iconResId = R.drawable.ic_back,
        routeResId = R.string.incomes_route,
    )
}