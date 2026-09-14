package com.sheyab.cashflow.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sheyab.cashflow.data.models.Transaction
import com.sheyab.cashflow.data.models.TransactionType
import com.sheyab.cashflow.ui.components.BudgetVsActualVsIncomeChart
import com.sheyab.cashflow.ui.components.MonthlySavingsHeroCard
import com.sheyab.cashflow.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(
    transactions: List<Transaction>,
    budgetLimits: Map<String, Double>
) {
    val totalIncome = transactions.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }.ifZero(5000.0)
    val totalExpenses = transactions.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }.ifZero(3380.0)
    val totalBudget = budgetLimits.values.sum().ifZero(3800.0)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cash Flow Analytics", color = TextPrimary, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BgDark)
            )
        },
        containerColor = BgDark
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Monthly Savings Hero Card
            item {
                MonthlySavingsHeroCard(
                    income = totalIncome,
                    actualSpending = totalExpenses
                )
            }

            // 2. Budget vs Actual vs Income 3-Way Chart
            item {
                BudgetVsActualVsIncomeChart(
                    income = totalIncome,
                    budget = totalBudget,
                    actualSpending = totalExpenses
                )
            }
        }
    }
}

private fun Double.ifZero(default: Double): Double = if (this == 0.0) default else this