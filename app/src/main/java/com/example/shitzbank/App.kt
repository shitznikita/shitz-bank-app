package com.example.shitzbank

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.shitzbank.common.network.ConnectionStatus
import com.example.shitzbank.ui.common.composable.CommonText
import com.example.shitzbank.ui.navigation.Screen
import com.example.shitzbank.ui.navigation.utils.currentRouteAsState
import com.example.shitzbank.ui.navigation.getScreen
import com.example.shitzbank.ui.navigation.screens
import com.example.shitzbank.ui.navigation.utils.ActionConfig
import com.example.shitzbank.ui.navigation.utils.LocalTopAppBarAction
import com.example.shitzbank.ui.screen.account.AccountScreen
import com.example.shitzbank.ui.screen.analysis.AnalysisScreen
import com.example.shitzbank.ui.screen.categories.CategoriesScreen
import com.example.shitzbank.ui.screen.expenses.ExpensesScreen
import com.example.shitzbank.ui.screen.history.TransactionsHistoryScreen
import com.example.shitzbank.ui.screen.incomes.IncomesScreen
import com.example.shitzbank.ui.screen.settings.SettingsScreen
import com.example.shitzbank.ui.screen.transaction.TransactionScreen

@Composable
fun App(viewModel: AppViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    val hostState = remember { SnackbarHostState() }

    val networkStatus by viewModel.networkStatus.collectAsStateWithLifecycle()
    val networkLostMessage = stringResource(R.string.no_internet)

    LaunchedEffect(networkStatus) {
        if (networkStatus is ConnectionStatus.Unavailable) {
            hostState.showSnackbar(
                message = networkLostMessage,
                duration = SnackbarDuration.Indefinite,
            )
        } else {
            hostState.currentSnackbarData?.dismiss()
        }
    }

    val providedTopAppBarAction = remember { mutableStateOf<ActionConfig?>(null) }

    CompositionLocalProvider(LocalTopAppBarAction provides providedTopAppBarAction.value) {
        Scaffold(
            snackbarHost = { SnackbarHost(hostState) },
            topBar = { AppTopBar(navController) },
            bottomBar = { AppBottomNavigationBar(navController) },
            floatingActionButton = { AppFloatingActionButton(navController) },
        ) { innerPadding ->
            val expensesRoute = Screen.Expenses.getRoute()
            val incomesRoute = Screen.Incomes.getRoute()
            val accountRoute = Screen.Account.getRoute()
            val categoriesRoute = Screen.Categories.getRoute()
            val settingsRoute = Screen.Settings.getRoute()

            NavHost(
                navController = navController,
                startDestination = stringResource(Screen.Expenses.routeResId),
                modifier = Modifier.padding(innerPadding),
            ) {
                composable(expensesRoute) {
                    LaunchedEffect(Unit) { providedTopAppBarAction.value = null }
                    ExpensesScreen(navController)
                }
                composable(incomesRoute) {
                    LaunchedEffect(Unit) { providedTopAppBarAction.value = null }
                    IncomesScreen(navController) }
                composable(accountRoute) {
                    LaunchedEffect(Unit) { providedTopAppBarAction.value = null }
                    AccountScreen() }
                composable(categoriesRoute) {
                    LaunchedEffect(Unit) { providedTopAppBarAction.value = null }
                    CategoriesScreen() }
                composable(settingsRoute) {
                    LaunchedEffect(Unit) { providedTopAppBarAction.value = null }
                    SettingsScreen() }
                composable(
                    route = "history/{isIncome}",
                    arguments = listOf(navArgument("isIncome") { type = NavType.BoolType }),
                ) {
                    LaunchedEffect(Unit) { providedTopAppBarAction.value = null }
                    TransactionsHistoryScreen(navController = navController)
                }
                composable(
                    route = "transaction/{isIncome}/{id}",
                    arguments = listOf(
                        navArgument("isIncome") { type = NavType.BoolType },
                        navArgument("id") { type = NavType.IntType }
                    )
                ) {
                    TransactionScreen(
                        navController = navController,
                        onSetTopBarAction = { actionConfig ->
                            providedTopAppBarAction.value = actionConfig
                        }
                    )
                }
                composable(
                    route = "transaction/{isIncome}/{isNew}",
                    arguments = listOf(
                        navArgument("isIncome") { type = NavType.BoolType },
                        navArgument("isNew") { type = NavType.BoolType }
                    )
                ) {
                    TransactionScreen(
                        navController = navController,
                        onSetTopBarAction = { actionConfig ->
                            providedTopAppBarAction.value = actionConfig
                        }
                    )
                }
                composable(
                    route = "analysis/{isIncome}",
                    arguments = listOf(navArgument("isIncome") { type = NavType.BoolType })
                ) {
                    LaunchedEffect(Unit) { providedTopAppBarAction.value = null }
                    AnalysisScreen(navController = navController)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(navController: NavController) {
    val currentDestination = navController.currentRouteAsState()

    val currentScreen = getScreen(currentDestination)

    val title = currentScreen.getTitle()
    val defaultAction = currentScreen.action
    val customAction = LocalTopAppBarAction.current
    val backNavigation = currentScreen.backNavigationIcon

    CenterAlignedTopAppBar(
        colors =
            if (currentScreen == Screen.ExpensesAnalysis
                || currentScreen == Screen.IncomesAnalysis) {
                TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                )
            },
        title = {
            CommonText(
                text = title,
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.titleLarge,
            )
        },
        navigationIcon = {
            if (backNavigation != null) {
                AppNavigationIcon(navController)
            }
        },
        actions = {
            val action: ActionConfig? = customAction ?: run {
                defaultAction?.let {
                    val actionRoute = it.getRoute()
                    val actionIcon = it.getIcon()
                    ActionConfig(
                        onClick = { navController.navigate(actionRoute) },
                        icon = actionIcon
                    )
                }
            }

            if (action != null) {
                IconButton(onClick = action.onClick) {
                    Icon(
                        imageVector = action.icon,
                        contentDescription = title,
                    )
                }
            }
        },
    )
}

@Composable
fun AppBottomNavigationBar(navController: NavController) {
    val currentDestination = navController.currentRouteAsState()

    BottomAppBar {
        screens.forEach { screen ->
            val screenRoute = screen.getRoute()

            val bottomNavigationIcon = screen.bottomNavigationIcon!!.getIcon()
            val bottomNavigationLabel = screen.bottomNavigationIcon.getLabel()

            val stringResourcesForRelatedRoutes = screen.relatedRoutesResIds.map { stringResource(it) }

            val relatedRoutes =
                remember(stringResourcesForRelatedRoutes) {
                    stringResourcesForRelatedRoutes
                }

            val isSelected = currentDestination == screenRoute || relatedRoutes.contains(currentDestination)

            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = bottomNavigationIcon,
                        contentDescription = bottomNavigationLabel,
                        tint =
                            if (isSelected) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            },
                    )
                },
                selected = isSelected,
                label = {
                    CommonText(
                        text = bottomNavigationLabel,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.labelMedium,
                    )
                },
                colors =
                    NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurface,
                        indicatorColor = MaterialTheme.colorScheme.secondary,
                    ),
                onClick = {
                    navController.navigate(screenRoute) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                },
            )
        }
    }
}

@Composable
fun AppFloatingActionButton(navController: NavController) {
    val currentDestination = navController.currentRouteAsState()

    val currentScreen = getScreen(currentDestination)

    val navigationRoute: String? = when (currentScreen) {
        Screen.Expenses -> "transaction/false/true"
        Screen.Incomes -> "transaction/true/true"
        else -> null
    }

    if (navigationRoute != null) {
        FloatingActionButton(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = Color.White,
            modifier = Modifier.clip(CircleShape),
            onClick = {
                navController.navigate(navigationRoute)
            },
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(R.string.add)
            )
        }
    }
}

@Composable
fun AppNavigationIcon(navController: NavController) {
    val currentDestination = navController.currentRouteAsState()

    val currentScreen = getScreen(currentDestination)

    val backNavigation = currentScreen.backNavigationIcon

    if (backNavigation != null) {
        val backNavigationIcon = backNavigation.getIcon()

        IconButton(
            onClick = { navController.navigateUp() },
        ) {
            Icon(
                imageVector = backNavigationIcon,
                contentDescription = stringResource(R.string.back)
            )
        }
    }
}
