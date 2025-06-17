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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.shitzbank.ui.navigation.Screen
import com.example.shitzbank.screen.expenses.ui.ExpensesScreen
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.shitzbank.screen.articles.ui.ArticlesScreen
import com.example.shitzbank.ui.navigation.getScreen
import com.example.shitzbank.screen.incomes.ui.IncomesScreen
import com.example.shitzbank.screen.settings.ui.SettingsScreen
import com.example.shitzbank.screen.wallet.ui.WalletScreen
import com.example.shitzbank.ui.common.CommonText
import com.example.shitzbank.ui.navigation.screens
import com.example.shitzbank.ui.viewmodel.MainViewModel

@Composable
fun App(viewModel: MainViewModel) {
    val navController = rememberNavController()

    Scaffold(
        topBar = { AppTopBar(navController) },
        bottomBar = { AppBottomNavigationBar(navController) },
        floatingActionButton = { AppFloatingActionButton(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Expenses.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Expenses.route) { ExpensesScreen(viewModel) }
            composable(Screen.Incomes.route) { IncomesScreen(viewModel) }
            composable(Screen.Wallet.route) { WalletScreen(viewModel) }
            composable(Screen.Articles.route) { ArticlesScreen(viewModel) }
            composable(Screen.Settings.route) { SettingsScreen() }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route

    val currentScreen = getScreen(currentDestination)

    val title = currentScreen?.title() ?: stringResource(R.string.app_name)
    val action = currentScreen?.action()

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
        actions = {
            if (action != null) {
                IconButton(onClick = {  }) {
                    Icon(
                        imageVector = action,
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
    val currentDestination = navBackStackEntry?.destination?.route

    BottomAppBar {
        screens.forEach { screen ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = screen.icon(),
                        contentDescription = screen.route,
                        tint = if (currentDestination == screen.route)
                            MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurface
                    )
                },
                selected = currentDestination === screen.route,
                label = {
                    CommonText(
                        text = screen.label(),
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
                    navController.navigate(screen.route) {
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
    val currentDestination = navBackStackEntry?.destination?.route

    val currentScreen = getScreen(currentDestination)

    val action = currentScreen?.action()

    if (action != null) {
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