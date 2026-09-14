package com.sheyab.cashflow.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sheyab.cashflow.data.models.*

@Entity(tableName = "accounts")
data class AccountEntity(
    @PrimaryKey val id: String,
    val name: String,
    val type: String,
    val balance: Double,
    val accountNumber: String,
    val colorHex: String,
    val isBiometricLocked: Boolean
) {
    fun toDomain(): BankAccount = BankAccount(
        id = id,
        name = name,
        type = try { CardType.valueOf(type) } catch (e: Exception) { CardType.CHECKING },
        balance = balance,
        accountNumber = accountNumber,
        colorHex = colorHex,
        isBiometricLocked = isBiometricLocked
    )

    companion object {
        fun fromDomain(account: BankAccount): AccountEntity = AccountEntity(
            id = account.id,
            name = account.name,
            type = account.type.name,
            balance = account.balance,
            accountNumber = account.accountNumber,
            colorHex = account.colorHex,
            isBiometricLocked = account.isBiometricLocked
        )
    }
}

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey val id: String,
    val title: String,
    val amount: Double,
    val category: String,
    val type: String,
    val date: Long,
    val accountId: String,
    val notes: String
) {
    fun toDomain(): Transaction = Transaction(
        id = id,
        title = title,
        amount = amount,
        category = BudgetCategory.fromString(category),
        type = try { TransactionType.valueOf(type) } catch (e: Exception) { TransactionType.EXPENSE },
        date = date,
        accountId = accountId,
        notes = notes
    )

    companion object {
        fun fromDomain(tx: Transaction): TransactionEntity = TransactionEntity(
            id = tx.id,
            title = tx.title,
            amount = tx.amount,
            category = tx.category.name,
            type = tx.type.name,
            date = tx.date,
            accountId = tx.accountId,
            notes = tx.notes
        )
    }
}

@Entity(tableName = "category_budgets")
data class CategoryBudgetEntity(
    @PrimaryKey val categoryName: String,
    val monthlyBudgetLimit: Double
)

@Entity(tableName = "portfolio_positions")
data class PortfolioEntity(
    @PrimaryKey val id: String,
    val symbol: String,
    val name: String,
    val shares: Double,
    val currentPrice: Double,
    val avgCost: Double
) {
    fun toDomain(): PortfolioPosition = PortfolioPosition(
        id = id,
        symbol = symbol,
        name = name,
        shares = shares,
        currentPrice = currentPrice,
        avgCost = avgCost
    )

    companion object {
        fun fromDomain(pos: PortfolioPosition): PortfolioEntity = PortfolioEntity(
            id = pos.id,
            symbol = pos.symbol,
            name = pos.name,
            shares = pos.shares,
            currentPrice = pos.currentPrice,
            avgCost = pos.avgCost
        )
    }
}