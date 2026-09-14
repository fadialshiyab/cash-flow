package com.sheyab.cashflow.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sheyab.cashflow.data.models.BankAccount
import com.sheyab.cashflow.data.models.Transaction
import com.sheyab.cashflow.data.models.TransactionType
import com.sheyab.cashflow.ui.components.GlassCard
import com.sheyab.cashflow.ui.theme.*
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    accounts: List<BankAccount>,
    transactions: List<Transaction>,
    onAddTransaction: () -> Unit,
    onNavigateToAnalytics: () -> Unit
) {
    val totalBalance = accounts.sumOf { it.balance }
    val currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Brush.linearGradient(listOf(AccentBlue, AccentCyan))),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("$", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("CashFlow", color = TextPrimary, fontWeight = FontWeight.Bold)
                    }
                },
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
            // Hero Balance Card
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    cornerRadius = 24.dp
                ) {
                    Text(
                        text = "Total Balance",
                        color = TextSecondary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = currencyFormatter.format(totalBalance),
                        color = TextPrimary,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        QuickActionButton(icon = Icons.Default.Send, label = "Transfer", onClick = {})
                        QuickActionButton(icon = Icons.Default.Add, label = "Deposit", onClick = onAddTransaction)
                        QuickActionButton(icon = Icons.Default.Analytics, label = "Analytics", onClick = onNavigateToAnalytics)
                        QuickActionButton(icon = Icons.Default.Receipt, label = "Bills", onClick = {})
                    }
                }
            }

            // Recent Transactions Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Recent Transactions",
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "See All",
                        color = AccentBlue,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Transaction items
            items(transactions.take(8)) { tx ->
                TransactionRow(tx = tx)
            }
        }
    }
}

@Composable
fun QuickActionButton(icon: ImageVector, label: String, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(
            onClick = onClick,
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(AccentBlue.copy(alpha = 0.15f))
        ) {
            Icon(imageVector = icon, contentDescription = label, tint = AccentBlue)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = label, color = TextSecondary, fontSize = 12.sp)
    }
}

@Composable
fun TransactionRow(tx: Transaction) {
    val currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US)
    val dateFormatter = SimpleDateFormat("MMM d, h:mm a", Locale.getDefault())

    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = 16.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(Color(android.graphics.Color.parseColor(tx.category.defaultColorHex)).copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (tx.type == TransactionType.INCOME) Icons.Default.ArrowDownward else Icons.Default.ShoppingBag,
                    contentDescription = tx.category.displayName,
                    tint = Color(android.graphics.Color.parseColor(tx.category.defaultColorHex)),
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = tx.title,
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp
                )
                Text(
                    text = "${tx.category.displayName} • ${dateFormatter.format(Date(tx.date))}",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }

            val amountText = if (tx.type == TransactionType.INCOME) "+${currencyFormatter.format(tx.amount)}" else "-${currencyFormatter.format(tx.amount)}"
            val amountColor = if (tx.type == TransactionType.INCOME) AccentGreen else TextPrimary

            Text(
                text = amountText,
                color = amountColor,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        }
    }
}