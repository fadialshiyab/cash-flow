package com.sheyab.cashflow.data.models

import java.util.Date
import java.util.UUID

enum class CardType(val displayName: String) {
    CHECKING("Checking Account"),
    SAVINGS("Savings Account"),
    CREDIT("Credit Card"),
    INVESTMENT("Brokerage Portfolio"),
    CASH("Physical Cash")
}

enum class TransactionType {
    INCOME, EXPENSE, TRANSFER
}

enum class BudgetCategory(val displayName: String, val iconName: String, val defaultColorHex: String) {
    FOOD("Groceries & Dining", "restaurant", "#10B981"),
    HOUSING("Housing & Rent", "home", "#3B82F6"),
    TRANSPORT("Transport & Fuel", "directions_car", "#8B5CF6"),
    ENTERTAINMENT("Entertainment", "movie", "#EC4899"),
    SHOPPING("Shopping", "shopping_bag", "#F59E0B"),
    UTILITIES("Utilities & Bills", "bolt", "#F97316"),
    INCOME("Salary & Income", "payments", "#10B981"),
    INVESTMENT("Investments", "trending_up", "#06B6D4"),
    OTHER("Other Expenses", "category", "#6B7280");

    companion object {
        fun fromString(value: String): BudgetCategory {
            return entries.find { it.name.equals(value, ignoreCase = true) } ?: OTHER
        }
    }
}

data class BankAccount(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val type: CardType,
    val balance: Double,
    val accountNumber: String,
    val colorHex: String,
    val isBiometricLocked: Boolean = false
)

data class Transaction(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val amount: Double,
    val category: BudgetCategory,
    val type: TransactionType,
    val date: Long = System.currentTimeMillis(),
    val accountId: String,
    val notes: String = ""
)

data class PortfolioPosition(
    val id: String = UUID.randomUUID().toString(),
    val symbol: String,
    val name: String,
    val shares: Double,
    val currentPrice: Double,
    val avgCost: Double
) {
    val totalValue: Double get() = shares * currentPrice
    val totalGain: Double get() = (currentPrice - avgCost) * shares
    val gainPercent: Double get() = if (avgCost > 0) ((currentPrice - avgCost) / avgCost) * 100 else 0.0
}

data class MonthlyAnalytics(
    val monthName: String,
    val income: Double,
    val budget: Double,
    val actualSpending: Double
) {
    val savedAmount: Double get() = (income - actualSpending).coerceAtLeast(0.0)
    val savingsRate: Double get() = if (income > 0) (savedAmount / income) * 100.0 else 0.0
}