package com.example.shitzbank.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.example.shitzbank.R

/**
 * Запечатанный класс, определяющий иконки и связанные маршруты для действий (например, в AppBar).
 *
 * @property iconResId Идентификатор ресурса векторной иконки (Drawable).
 * @property routeResId Идентификатор строкового ресурса для маршрута, куда ведет это действие.
 */
sealed class ActionIcon(
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
     * Composable-функция для получения строкового маршрута действия из ресурсов.
     * @return [String] маршрута.
     */
    @Composable
    fun getRoute() = stringResource(routeResId)

    /**
     * Иконка действия для перехода к истории расходов.
     */
    data object ExpensesAction : ActionIcon(
        iconResId = R.drawable.ic_history,
        routeResId = R.string.expenses_history_route,
    )

    /**
     * Иконка действия для перехода к истории доходов.
     */
    data object IncomesAction : ActionIcon(
        iconResId = R.drawable.ic_history,
        routeResId = R.string.incomes_history_route,
    )

    /**
     * Иконка действия для редактирования (например, счета). Ведет на экран расходов.
     */
    data object AccountAction : ActionIcon(
        iconResId = R.drawable.ic_edit,
        routeResId = R.string.expenses_route // TODO: переход на экран редактирования
    )

    /**
     * Иконка действия для просмотра истории транзакций по расходам.
     */
    data object ExpensesHistoryAction : ActionIcon(
        iconResId = R.drawable.ic_transaction_history,
        routeResId = R.string.expenses_analysis_route
    )

    /**
     * Иконка действия для просмотра истории транзакций по доходам.
     */
    data object IncomesHistoryAction : ActionIcon(
        iconResId = R.drawable.ic_transaction_history,
        routeResId = R.string.incomes_analysis_route
    )

    data object ExpenseAction : ActionIcon(
        iconResId = R.drawable.ic_done,
        routeResId = R.string.expenses_route
    )

    data object IncomeAction : ActionIcon(
        iconResId = R.drawable.ic_done,
        routeResId = R.string.incomes_route
    )
}