package com.example.shitzbank.data.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.example.shitzbank.R

sealed class NavigationIcon(
    val iconResId: Int,
    val labelResId: Int
) {
    data object ExpensesIcon : NavigationIcon(
        iconResId = R.drawable.ic_downtrend,
        labelResId = R.string.expenses_icon
    )

    data object IncomesIcon : NavigationIcon(
        iconResId = R.drawable.ic_uptrend,
        labelResId = R.string.incomes_icon
    )

    data object WalletIcon : NavigationIcon(
        iconResId = R.drawable.ic_wallet,
        labelResId = R.string.wallet_icon
    )

    data object ArticlesIcon : NavigationIcon(
        iconResId = R.drawable.ic_articles,
        labelResId = R.string.articles_icon
    )

    data object SettingsIcon : NavigationIcon(
        iconResId = R.drawable.ic_settings,
        labelResId = R.string.settings_icon
    )
}

sealed class Action(
    val actionResId: Int,
    val isExist: Boolean
) {
    data object ExpensesAction : Action(
        actionResId = R.drawable.ic_history,
        isExist = true
    )

    data object IncomesAction : Action(
        actionResId = R.drawable.ic_history,
        isExist = true
    )

    data object WalletAction : Action(
        actionResId = R.drawable.ic_edit,
        isExist = true
    )

    data object ArticlesAction : Action(
        actionResId = R.drawable.ic_launcher_background,
        isExist = false
    )

    data object SettingsAction : Action(
        actionResId = R.drawable.ic_launcher_background,
        isExist = false
    )
}

sealed class Screen(
    val route: String,
    val titleResId: Int,
    val action: Action,
    val navigationIcon: NavigationIcon
) {
    @Composable
    fun icon() = ImageVector.vectorResource(navigationIcon.iconResId)

    @Composable
    fun label() = stringResource(navigationIcon.labelResId)

    @Composable
    fun title() = stringResource(titleResId)

    @Composable
    fun action(): ImageVector? = if (action.isExist) ImageVector.vectorResource(action.actionResId) else null

    data object Expenses : Screen(
        route = "expenses",
        titleResId = R.string.expenses_header,
        action = Action.ExpensesAction,
        navigationIcon = NavigationIcon.ExpensesIcon
    )

    data object Incomes : Screen(
        route = "incomes",
        titleResId = R.string.incomes_header,
        action = Action.IncomesAction,
        navigationIcon = NavigationIcon.IncomesIcon
    )

    data object Wallet : Screen(
        route = "wallet",
        titleResId = R.string.wallet_header,
        action = Action.WalletAction,
        navigationIcon = NavigationIcon.WalletIcon
    )

    data object Articles : Screen(
        route = "articles",
        titleResId = R.string.articles_header,
        action = Action.ArticlesAction,
        navigationIcon = NavigationIcon.ArticlesIcon
    )

    data object Settings : Screen(
        route = "settings",
        titleResId = R.string.settings_header,
        action = Action.SettingsAction,
        navigationIcon = NavigationIcon.SettingsIcon
    )
}

fun getScreen(route: String?): Screen? {
    return when (route) {
        Screen.Expenses.route -> Screen.Expenses
        Screen.Incomes.route -> Screen.Incomes
        Screen.Wallet.route -> Screen.Wallet
        Screen.Articles.route -> Screen.Articles
        Screen.Settings.route -> Screen.Settings
        else -> null
    }
}