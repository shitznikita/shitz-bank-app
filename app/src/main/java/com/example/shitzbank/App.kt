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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.shitzbank.navigation.Screen
import com.example.shitzbank.screen.expenses.ExpensesScreen
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.shitzbank.common.network.ConnectionStatus
import com.example.shitzbank.navigation.getScreen
import com.example.shitzbank.screen.incomes.IncomesScreen
import com.example.shitzbank.screen.settings.SettingsScreen
import com.example.shitzbank.common.ui.CommonText
import com.example.shitzbank.navigation.screens
import com.example.shitzbank.screen.account.AccountScreen
import com.example.shitzbank.screen.categories.CategoriesScreen
import com.example.shitzbank.screen.expenses.history.ExpensesHistoryScreen

@Composable
fun App(
    viewModel: AppViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }

    val networkStatus by viewModel.networkStatus.collectAsState()

    LaunchedEffect(networkStatus) {
        if (networkStatus is ConnectionStatus.Unavailable) {
            snackbarHostState.showSnackbar(
                message = "Отсутствует подключение к интернету",
                duration = SnackbarDuration.Indefinite
            )
        } else {
            snackbarHostState.currentSnackbarData?.dismiss()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = { AppTopBar(navController) },
        bottomBar = { AppBottomNavigationBar(navController) },
        floatingActionButton = { AppFloatingActionButton(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = stringResource(Screen.Expenses.routeResId),
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("expenses") { ExpensesScreen() }
            composable("incomes") { IncomesScreen() }
            composable("account") { AccountScreen() }
            composable("categories") { CategoriesScreen() }
            composable("settings") { SettingsScreen() }
            composable("expenses_history") { ExpensesHistoryScreen() }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route ?: stringResource(Screen.Expenses.routeResId)

    val currentScreen = getScreen(currentDestination)

    val title = currentScreen.getTitle()
    val action = currentScreen.action
    val backNavigation = currentScreen.backNavigationIcon

    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        title = {
            CommonText(
                text = title,
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.titleLarge
            )
        },
        navigationIcon = {
            if (backNavigation != null) {
                AppNavigationIcon(navController)
            }
        },
        actions = {
            if (action != null) {
                val actionRoute = action.getRoute()
                val actionIcon = action.getIcon()

                IconButton(onClick = { navController.navigate(actionRoute) }) {
                    Icon(
                        imageVector = actionIcon,
                        contentDescription = title
                    )
                }
            }
        }
    )
}

@Composable
fun AppBottomNavigationBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route ?: stringResource(Screen.Expenses.routeResId)

    BottomAppBar {
        screens.forEach { screen ->
            val screenRoute = screen.getRoute()

            val bottomNavigationIcon = screen.bottomNavigationIcon!!.getIcon()
            val bottomNavigationLabel = screen.bottomNavigationIcon.getLabel()

            val relatedRoutes = screen.relatedRoutesResIds.map { stringResource(it) }

            val isSelected = currentDestination == screenRoute || relatedRoutes.contains(currentDestination)

            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = bottomNavigationIcon,
                        contentDescription = bottomNavigationLabel,
                        tint =
                        if (isSelected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurface
                    )
                },
                selected = isSelected,
                label = {
                    CommonText(
                        text = bottomNavigationLabel,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurface,
                    indicatorColor = MaterialTheme.colorScheme.secondary
                ),
                onClick = {
                    navController.navigate(screenRoute) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

@Composable
fun AppFloatingActionButton(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route ?: stringResource(Screen.Expenses.routeResId)

    val currentScreen = getScreen(currentDestination)

    val action = currentScreen.action

    if (currentScreen in screens && action != null) {
        FloatingActionButton(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = Color.White,
            modifier = Modifier.clip(CircleShape),
            onClick = {}
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add")
        }
    }
}

@Composable
fun AppNavigationIcon(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route ?: stringResource(Screen.Expenses.routeResId)

    val currentScreen = getScreen(currentDestination)

    val backNavigation = currentScreen.backNavigationIcon

    if (backNavigation != null) {
        val backNavigationIcon = backNavigation.getIcon()

        IconButton(
            onClick = { navController.navigateUp() }
        ) {
            Icon(
                imageVector = backNavigationIcon,
                contentDescription = null
            )
        }
    }
}