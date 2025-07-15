package com.example.shitzbank.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.shitzbank.R

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
        relatedRoutesResIds = listOf(
            R.string.expenses_history_route,
            R.string.expense_route,
            R.string.expense_old_route,
            R.string.expense_new_route
        )
    )

    /**
     * Экран "Доходы".
     */
    data object Incomes : Screen(
        routeResId = R.string.incomes_route,
        titleResId = R.string.incomes_header,
        action = ActionIcon.IncomesAction,
        bottomNavigationIcon = BottomNavigationIcon.IncomesIcon,
        relatedRoutesResIds = listOf(
            R.string.incomes_history_route,
            R.string.income_route,
            R.string.income_old_route,
            R.string.income_new_route
        )
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

    data object Expense : Screen(
        routeResId = R.string.expense_route,
        titleResId = R.string.expense_header,
        action = ActionIcon.ExpenseAction,
        backNavigationIcon = BackNavigationIcon.ExpenseClose
    )

    data object Income : Screen(
        routeResId = R.string.income_route,
        titleResId = R.string.income_header,
        action = ActionIcon.IncomeAction,
        backNavigationIcon = BackNavigationIcon.IncomeClose
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
        "transaction/false/" -> Screen.Expense
        "transaction/true/" -> Screen.Income
        "transaction/false/false" -> Screen.Expense
        "transaction/false/true" -> Screen.Expense
        "transaction/true/false" -> Screen.Income
        "transaction/true/true" -> Screen.Income
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
