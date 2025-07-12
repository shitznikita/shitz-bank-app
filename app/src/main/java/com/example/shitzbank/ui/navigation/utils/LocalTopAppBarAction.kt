package com.example.shitzbank.ui.navigation.utils

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.vector.ImageVector

data class ActionConfig(
    val onClick: () -> Unit,
    val icon: ImageVector
)

val LocalTopAppBarAction = staticCompositionLocalOf<ActionConfig?> { null }