package com.sheyab.cashflow

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sheyab.cashflow.data.models.BankAccount
import com.sheyab.cashflow.data.models.Transaction
import com.sheyab.cashflow.ui.navigation.Screen
import com.sheyab.cashflow.ui.screens.*
import com.sheyab.cashflow.ui.theme.*
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val app by lazy { application as CashFlowApplication }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CashFlowTheme {
                MainAppContainer(app = app)
            }
        }
    }
}

@Composable
fun MainAppContainer(app: CashFlowApplication) {
    val navController = rememberNavController()
    val accounts by app.repository.accountsFlow.collectAsState(initial = emptyList())
    val transactions by app.repository.transactionsFlow.collectAsState(initial = emptyList())
    val budgets by app.repository.budgetsFlow.collectAsState(initial = emptyMap())
    val coroutineScope = rememberCoroutineScope()

    val navItems = listOf(
        Screen.Dashboard,
        Screen.Analytics,
        Screen.Cards,
        Screen.Budgets,
        Screen.Settings
    )

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            NavigationBar(
                containerColor = BgDark,
                contentColor = TextPrimary
            ) {
                navItems.forEach { screen ->
                    val selected = currentRoute == screen.route
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = selected,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = AccentBlue,
                            selectedTextColor = AccentBlue,
                            unselectedIconColor = TextMuted,
                            unselectedTextColor = TextMuted,
                            indicatorColor = AccentBlue.copy(alpha = 0.15f)
                        )
                    )
                }
            }
        },
        containerColor = BgDark
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(Screen.Dashboard.route) {
                DashboardScreen(
                    accounts = accounts,
                    transactions = transactions,
                    onAddTransaction = {},
                    onNavigateToAnalytics = {
                        navController.navigate(Screen.Analytics.route)
                    }
                )
            }
            composable(Screen.Analytics.route) {
                AnalyticsScreen(
                    transactions = transactions,
                    budgetLimits = budgets.mapKeys { it.key.name }
                )
            }
            composable(Screen.Cards.route) {
                CardsScreen(
                    accounts = accounts,
                    onAddCard = {}
                )
            }
            composable(Screen.Budgets.route) {
                CategoriesBudgetsScreen(
                    budgets = budgets
                )
            }
            composable(Screen.Settings.route) {
                SettingsScreen(
                    syncService = app.syncService,
                    onEraseAllData = {
                        coroutineScope.launch {
                            app.repository.eraseAllData()
                        }
                    }
                )
            }
        }
    }
}