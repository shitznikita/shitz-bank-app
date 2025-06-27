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
        routeResId = R.string.expenses_route, // Возможно, это ошибка, и должно быть другое значение?
    )

    /**
     * Иконка действия для просмотра истории транзакций по расходам.
     */
    data object ExpensesHistoryAction : ActionIcon(
        iconResId = R.drawable.ic_transaction_history,
        routeResId = R.string.expenses_history_route,
    )

    /**
     * Иконка действия для просмотра истории транзакций по доходам.
     */
    data object IncomesHistoryAction : ActionIcon(
        iconResId = R.drawable.ic_transaction_history,
        routeResId = R.string.incomes_history_route,
    )
}

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

/**
 * Запечатанный класс, определяющий различные экраны в приложении,
 * их маршруты, заголовки, связанные иконки и действия.
 *
 * @property routeResId Идентификатор строкового ресурса для маршрута экрана.
 * @property titleResId Идентификатор строкового ресурса для заголовка экрана.
 * @property action Опциональная [ActionIcon], связанная с этим экраном (например, для AppBar).
 * @property bottomNavigationIcon Опциональная [BottomNavigationIcon], если экран представлен в нижней навигации.
 * @property backNavigationIcon Опциональная [BackNavigationIcon], если экран имеет кнопку "Назад".
 * @property relatedRoutesResIds Список идентификаторов строковых ресурсов для маршрутов,
 * которые логически связаны с этим экраном (например, история транзакций).
 */
sealed class Screen(
    val routeResId: Int,
    val titleResId: Int,
    val action: ActionIcon? = null,
    val bottomNavigationIcon: BottomNavigationIcon? = null,
    val backNavigationIcon: BackNavigationIcon? = null,
    val relatedRoutesResIds: List<Int> = emptyList(),
) {
    /**
     * Composable-функция для получения строкового маршрута экрана из ресурсов.
     * @return [String] маршрута.
     */
    @Composable
    fun getRoute() = stringResource(routeResId)

    /**
     * Composable-функция для получения строкового заголовка экрана из ресурсов.
     * @return [String] заголовка.
     */
    @Composable
    fun getTitle() = stringResource(titleResId)

    /**
     * Экран "Расходы".
     */
    data object Expenses : Screen(
        routeResId = R.string.expenses_route,
        titleResId = R.string.expenses_header,
        action = ActionIcon.ExpensesAction,
        bottomNavigationIcon = BottomNavigationIcon.ExpensesIcon,
        relatedRoutesResIds = listOf(R.string.expenses_history_route),
    )

    /**
     * Экран "Доходы".
     */
    data object Incomes : Screen(
        routeResId = R.string.incomes_route,
        titleResId = R.string.incomes_header,
        action = ActionIcon.IncomesAction,
        bottomNavigationIcon = BottomNavigationIcon.IncomesIcon,
        relatedRoutesResIds = listOf(R.string.incomes_history_route),
    )

    /**
     * Экран "Счет" (Кошелек).
     */
    data object Account : Screen(
        routeResId = R.string.account_route,
        titleResId = R.string.wallet_header,
        action = ActionIcon.AccountAction,
        bottomNavigationIcon = BottomNavigationIcon.AccountIcon,
    )

    /**
     * Экран "Категории".
     */
    data object Categories : Screen(
        routeResId = R.string.categories_route,
        titleResId = R.string.articles_header,
        bottomNavigationIcon = BottomNavigationIcon.CategoriesIcon,
    )

    /**
     * Экран "Настройки".
     */
    data object Settings : Screen(
        routeResId = R.string.settings_route,
        titleResId = R.string.settings_header,
        bottomNavigationIcon = BottomNavigationIcon.SettingsIcon,
    )

    /**
     * Экран "История расходов".
     */
    data object ExpensesHistory : Screen(
        routeResId = R.string.expenses_history_route,
        titleResId = R.string.history_header,
        action = ActionIcon.ExpensesHistoryAction,
        backNavigationIcon = BackNavigationIcon.ExpensesHistoryBack,
    )

    /**
     * Экран "История доходов".
     */
    data object IncomesHistory : Screen(
        routeResId = R.string.incomes_history_route,
        titleResId = R.string.history_header,
        action = ActionIcon.IncomesHistoryAction,
        backNavigationIcon = BackNavigationIcon.IncomesHistoryBack,
    )
}

/**
 * Вспомогательная функция для получения объекта [Screen] по его строковому маршруту.
 *
 * @param route Строковый маршрут экрана.
 * @return Соответствующий объект [Screen]. В случае неопределенного маршрута возвращает [Screen.Expenses] по умолчанию.
 */
fun getScreen(route: String): Screen {
    return when (route) {
        "expenses" -> Screen.Expenses
        "incomes" -> Screen.Incomes
        "account" -> Screen.Account
        "categories" -> Screen.Categories
        "settings" -> Screen.Settings
        "history/false" -> Screen.ExpensesHistory // Маршрут для истории расходов
        "history/true" -> Screen.IncomesHistory // Маршрут для истории доходов
        else -> Screen.Expenses // Маршрут по умолчанию
    }
}

/**
 * Список всех экранов, которые должны быть представлены в нижней навигационной панели.
 */
val screens =
    listOf(
        Screen.Expenses,
        Screen.Incomes,
        Screen.Account,
        Screen.Categories,
        Screen.Settings,
    )
