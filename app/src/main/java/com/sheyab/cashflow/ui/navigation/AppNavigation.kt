package com.sheyab.cashflow.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Dashboard : Screen("dashboard", "Home", Icons.Default.Home)
    object Analytics : Screen("analytics", "Analytics", Icons.Default.BarChart)
    object Cards : Screen("cards", "Cards", Icons.Default.CreditCard)
    object Budgets : Screen("budgets", "Budgets", Icons.Default.PieChart)
    object Settings : Screen("settings", "Settings", Icons.Default.Settings)
    object Onboarding : Screen("onboarding", "Welcome", Icons.Default.Star)
}