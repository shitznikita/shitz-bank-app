package com.example.shitzbank.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.example.shitzbank.R

sealed class BottomNavigationIcon(
    val iconResId: Int,
    val labelResId: Int
) {
    @Composable
    fun getIcon() = ImageVector.vectorResource(iconResId)

    @Composable
    fun getLabel() = stringResource(labelResId)

    data object ExpensesIcon : BottomNavigationIcon(
        iconResId = R.drawable.ic_downtrend,
        labelResId = R.string.expenses_icon
    )

    data object IncomesIcon : BottomNavigationIcon(
        iconResId = R.drawable.ic_uptrend,
        labelResId = R.string.incomes_icon
    )

    data object AccountIcon : BottomNavigationIcon(
        iconResId = R.drawable.ic_wallet,
        labelResId = R.string.wallet_icon
    )

    data object CategoriesIcon : BottomNavigationIcon(
        iconResId = R.drawable.ic_articles,
        labelResId = R.string.articles_icon
    )

    data object SettingsIcon : BottomNavigationIcon(
        iconResId = R.drawable.ic_settings,
        labelResId = R.string.settings_icon
    )
}

sealed class ActionIcon(
    val iconResId: Int,
    val routeResId: Int
) {
    @Composable
    fun getIcon() = ImageVector.vectorResource(iconResId)

    @Composable
    fun getRoute() = stringResource(routeResId)

    data object ExpensesAction : ActionIcon(
        iconResId = R.drawable.ic_history,
        routeResId = R.string.expenses_history_route
    )

    data object IncomesAction : ActionIcon(
        iconResId = R.drawable.ic_history,
        routeResId = R.string.incomes_history_route
    )

    data object AccountAction : ActionIcon(
        iconResId = R.drawable.ic_edit,
        routeResId = R.string.expenses_route
    )

    data object ExpensesHistoryAction : ActionIcon(
        iconResId = R.drawable.ic_transaction_history,
        routeResId = R.string.expenses_history_route
    )

    data object IncomesHistoryAction : ActionIcon(
        iconResId = R.drawable.ic_transaction_history,
        routeResId = R.string.incomes_history_route
    )
}

sealed class BackNavigationIcon(
    val iconResId: Int,
    val routeResId: Int
) {
    @Composable
    fun getIcon() = ImageVector.vectorResource(iconResId)

    @Composable
    fun getRoute() = stringResource(routeResId)

    data object ExpensesHistoryBack : BackNavigationIcon(
        iconResId = R.drawable.ic_back,
        routeResId = R.string.expenses_route
    )

    data object IncomesHistoryBack : BackNavigationIcon(
        iconResId = R.drawable.ic_back,
        routeResId = R.string.incomes_route
    )
}

sealed class Screen(
    val routeResId: Int,
    val titleResId: Int,
    val action: ActionIcon? = null,
    val bottomNavigationIcon: BottomNavigationIcon? = null,
    val backNavigationIcon: BackNavigationIcon? = null,
    val relatedRoutesResIds: List<Int> = emptyList()
) {
    @Composable
    fun getRoute() = stringResource(routeResId)

    @Composable
    fun getTitle() = stringResource(titleResId)

    data object Expenses : Screen(
        routeResId = R.string.expenses_route,
        titleResId = R.string.expenses_header,
        action = ActionIcon.ExpensesAction,
        bottomNavigationIcon = BottomNavigationIcon.ExpensesIcon,
        relatedRoutesResIds = listOf(R.string.expenses_history_route)
    )

    data object Incomes : Screen(
        routeResId = R.string.incomes_route,
        titleResId = R.string.incomes_header,
        action = ActionIcon.IncomesAction,
        bottomNavigationIcon = BottomNavigationIcon.IncomesIcon,
        relatedRoutesResIds = listOf(R.string.incomes_history_route)
    )

    data object Account : Screen(
        routeResId = R.string.account_route,
        titleResId = R.string.wallet_header,
        action = ActionIcon.AccountAction,
        bottomNavigationIcon = BottomNavigationIcon.AccountIcon
    )

    data object Categories : Screen(
        routeResId = R.string.categories_route,
        titleResId = R.string.articles_header,
        bottomNavigationIcon = BottomNavigationIcon.CategoriesIcon
    )

    data object Settings : Screen(
        routeResId = R.string.settings_route,
        titleResId = R.string.settings_header,
        bottomNavigationIcon = BottomNavigationIcon.SettingsIcon
    )

    data object ExpensesHistory : Screen(
        routeResId = R.string.expenses_history_route,
        titleResId = R.string.history_header,
        action = ActionIcon.ExpensesHistoryAction,
        backNavigationIcon = BackNavigationIcon.ExpensesHistoryBack
    )

    data object IncomesHistory : Screen(
        routeResId = R.string.incomes_history_route,
        titleResId = R.string.history_header,
        action = ActionIcon.IncomesHistoryAction,
        backNavigationIcon = BackNavigationIcon.IncomesHistoryBack
    )
}

fun getScreen(route: String): Screen {
    return when (route) {
        "expenses" -> Screen.Expenses
        "incomes" -> Screen.Incomes
        "account" -> Screen.Account
        "categories" -> Screen.Categories
        "settings" -> Screen.Settings
        "history/false" -> Screen.ExpensesHistory
        "history/true" -> Screen.IncomesHistory
        else -> Screen.Expenses
    }
}

val screens = listOf(
    Screen.Expenses,
    Screen.Incomes,
    Screen.Account,
    Screen.Categories,
    Screen.Settings
)