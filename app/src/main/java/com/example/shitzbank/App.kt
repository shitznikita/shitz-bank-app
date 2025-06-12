package com.example.shitzbank

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.shitzbank.data.navigation.Screen
import com.example.shitzbank.expenses.ui.ExpensesScreen
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.shitzbank.articles.ui.ArticlesScreen
import com.example.shitzbank.data.navigation.getScreen
import com.example.shitzbank.incomes.ui.IncomesScreen
import com.example.shitzbank.settings.ui.SettingsScreen
import com.example.shitzbank.ui.theme.HeaderGreen
import com.example.shitzbank.ui.theme.LightGreen
import com.example.shitzbank.wallet.ui.WalletScreen

@Composable
fun App() {
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
        bottomBar = { BottomNavigationBar(navController, screens) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Expenses.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Expenses.route) { ExpensesScreen() }
            composable(Screen.Incomes.route) { IncomesScreen() }
            composable(Screen.Wallet.route) { WalletScreen() }
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

    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = HeaderGreen,
            titleContentColor = Color.Black
        ),
        title = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = title,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        },
        navigationIcon = {
            if (action != null) {
                Spacer(Modifier.size(48.dp))
            }
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
fun BottomNavigationBar(
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