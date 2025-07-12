package com.example.shitzbank.ui.navigation.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.shitzbank.ui.navigation.Screen

/**
 * [Composable]-функция расширения для [NavController], которая предоставляет текущий маршрут
 * навигации в виде [String].
 *
 * Эта функция полезна для определения текущего экрана или динамической адаптации UI
 * на основе текущего местоположения в навигационном графе.
 *
 * Обрабатывает специфический маршрут "history/{isIncome}", извлекая фактическое значение `isIncome`
 * из аргументов, чтобы предоставить более точный маршрут.
 * Также обрабатывает маршрут "transaction/{isIncome}/{id}", извлекая `isIncome` и `id`.
 * В случае, если маршрут не определен, возвращает строковый ресурс, соответствующий
 * маршруту экрана [com.example.shitzbank.ui.navigation.Screen.Expenses].
 *
 * @receiver [NavController], для которого необходимо получить текущий маршрут.
 * @return [String], представляющая текущий маршрут навигации.
 */
@Composable
fun NavController.currentRouteAsState(): String {
    val navBackStackEntry by currentBackStackEntryAsState()
    val routePattern = navBackStackEntry?.destination?.route

    return when {
        routePattern == "history/{isIncome}" -> {
            val isIncome = navBackStackEntry?.arguments?.getBoolean("isIncome")
            "history/$isIncome"
        }
        routePattern == "transaction/{isIncome}/{id}" -> {
            val isIncome = navBackStackEntry?.arguments?.getBoolean("isIncome")
            "transaction/$isIncome/"
        }
        else -> {
            routePattern ?: stringResource(Screen.Expenses.routeResId)
        }
    }
}
