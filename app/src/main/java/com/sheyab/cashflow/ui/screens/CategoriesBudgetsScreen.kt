package com.sheyab.cashflow.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sheyab.cashflow.data.models.BudgetCategory
import com.sheyab.cashflow.ui.components.GlassCard
import com.sheyab.cashflow.ui.theme.*
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesBudgetsScreen(
    budgets: Map<BudgetCategory, Double>
) {
    val currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Smart Budgets", color = TextPrimary, fontWeight = FontWeight.Bold) },
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
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(budgets.entries.toList()) { entry ->
                GlassCard(modifier = Modifier.fillMaxWidth(), cornerRadius = 16.dp) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(entry.key.displayName, color = TextPrimary, fontWeight = FontWeight.SemiBold)
                        Text(currencyFormatter.format(entry.value), color = AccentGreen, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}