package com.example.shitzbank

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.shitzbank.domain.navigation.Screen
import com.example.shitzbank.screen.expenses.ui.ExpensesScreen
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.shitzbank.screen.articles.ui.ArticlesScreen
import com.example.shitzbank.domain.navigation.getScreen
import com.example.shitzbank.screen.incomes.ui.IncomesScreen
import com.example.shitzbank.screen.settings.ui.SettingsScreen
import com.example.shitzbank.ui.theme.AddButtonGreen
import com.example.shitzbank.ui.theme.HeaderGreen
import com.example.shitzbank.ui.theme.LightGreen
import com.example.shitzbank.screen.wallet.ui.WalletScreen
import com.example.shitzbank.screen.wallet.ui.WalletViewModel

@Composable
fun App(viewModel: WalletViewModel = viewModel()) {
    val navController = rememberNavController()

    val screens = listOf(
        Screen.Expenses,
        Screen.Incomes,
        Screen.Wallet,
        Screen.Articles,
        Screen.Settings
    )

    Scaffold(
        topBar = { AppTopBar(navController) },
        bottomBar = { AppBottomNavigationBar(navController, screens) },
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
            composable(Screen.Articles.route) { ArticlesScreen() }
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
            containerColor = HeaderGreen,
            titleContentColor = Color.Black
        ),
        title = {
            Text(
                text = title,
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
fun AppBottomNavigationBar(
    navController: NavController,
    screens: List<Screen>
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route

    NavigationBar (
        modifier = Modifier.padding(horizontal = 8.dp)
    ){
        screens.forEach { screen ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = screen.icon(),
                        contentDescription = screen.route,
                        tint = if (currentDestination == screen.route) HeaderGreen else Color.Black
                    )
                },
                selected = currentDestination === screen.route,
                label = { Text(screen.label()) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.Green,
                    unselectedIconColor = Color.Black,
                    indicatorColor = LightGreen
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
            containerColor = AddButtonGreen,
            contentColor = Color.White,
            modifier = Modifier.clip(CircleShape),
            onClick = {}
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add")
        }
    }
}