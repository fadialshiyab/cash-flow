package com.sheyab.cashflow.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sheyab.cashflow.ui.theme.*
import java.text.NumberFormat
import java.util.Locale

@Composable
fun MonthlySavingsHeroCard(
    income: Double,
    actualSpending: Double
) {
    val savedAmount = (income - actualSpending).coerceAtLeast(0.0)
    val savingsRate = if (income > 0) (savedAmount / income) * 100.0 else 0.0
    val currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US)

    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = 22.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(AccentGreen.copy(alpha = 0.25f), AccentCyan.copy(alpha = 0.25f))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.TrendingUp,
                    contentDescription = "Savings",
                    tint = AccentGreen,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = String.format("+%.1f%%", savingsRate),
                        color = AccentGreen,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 22.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Savings Rate",
                        color = TextSecondary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Text(
                    text = "${currencyFormatter.format(savedAmount)} saved this month",
                    color = TextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun BudgetVsActualVsIncomeChart(
    income: Double,
    budget: Double,
    actualSpending: Double,
    modifier: Modifier = Modifier
) {
    val currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US)
    val maxValue = maxOf(income, budget, actualSpending, 1.0)

    GlassCard(
        modifier = modifier.fillMaxWidth(),
        cornerRadius = 22.dp
    ) {
        Text(
            text = "Monthly Performance (3-Way Comparison)",
            color = TextPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
        Text(
            text = "Income vs. Budget Limit vs. Actual Spending",
            color = TextSecondary,
            fontSize = 12.sp,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            ComparisonBar(
                title = "Income",
                amount = income,
                maxAmount = maxValue,
                gradient = listOf(AccentGreen, Color(0xFF34D399))
            )
            ComparisonBar(
                title = "Budget",
                amount = budget,
                maxAmount = maxValue,
                gradient = listOf(AccentBlue, AccentCyan)
            )
            ComparisonBar(
                title = "Actual",
                amount = actualSpending,
                maxAmount = maxValue,
                gradient = if (actualSpending > budget) listOf(AccentRed, Color(0xFFF87171)) else listOf(AccentOrange, Color(0xFFFBBF24))
            )
        }
    }
}

@Composable
private fun ComparisonBar(
    title: String,
    amount: Double,
    maxAmount: Double,
    gradient: List<Color>
) {
    val targetFraction = (amount / maxAmount).toFloat().coerceIn(0.05f, 1f)
    val animatedFraction by animateFloatAsState(targetValue = targetFraction, label = "bar_anim")
    val currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(80.dp)
    ) {
        Text(
            text = currencyFormatter.format(amount),
            color = TextPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp
        )
        Spacer(modifier = Modifier.height(6.dp))

        Box(
            modifier = Modifier
                .width(44.dp)
                .height((130 * animatedFraction).dp)
                .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
                .background(Brush.verticalGradient(colors = gradient))
        )

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            color = TextSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}