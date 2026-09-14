package com.sheyab.cashflow.data.repository

import android.content.Context
import com.sheyab.cashflow.data.local.*
import com.sheyab.cashflow.data.models.*
import com.sheyab.cashflow.data.sync.GoogleDriveSyncService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FinanceRepository(
    private val dao: FinanceDao,
    val syncService: GoogleDriveSyncService
) {
    val accountsFlow: Flow<List<BankAccount>> = dao.getAllAccountsFlow().map { entities ->
        entities.map { it.toDomain() }
    }

    val transactionsFlow: Flow<List<Transaction>> = dao.getAllTransactionsFlow().map { entities ->
        entities.map { it.toDomain() }
    }

    val portfolioFlow: Flow<List<PortfolioPosition>> = dao.getAllPortfolioFlow().map { entities ->
        entities.map { it.toDomain() }
    }

    val budgetsFlow: Flow<Map<BudgetCategory, Double>> = dao.getAllBudgetsFlow().map { list ->
        list.associate { BudgetCategory.fromString(it.categoryName) to it.monthlyBudgetLimit }
    }

    suspend fun seedInitialDataIfEmpty() = withContext(Dispatchers.IO) {
        val existing = dao.getAllAccounts()
        if (existing.isEmpty()) {
            val initialAccounts = listOf(
                BankAccount(
                    name = "Main Checking",
                    type = CardType.CHECKING,
                    balance = 8450.00,
                    accountNumber = "•••• 4829",
                    colorHex = "#3B82F6",
                    isBiometricLocked = false
                ),
                BankAccount(
                    name = "High-Yield Savings",
                    type = CardType.SAVINGS,
                    balance = 14200.00,
                    accountNumber = "•••• 9104",
                    colorHex = "#10B981",
                    isBiometricLocked = true
                ),
                BankAccount(
                    name = "Platinum Rewards Card",
                    type = CardType.CREDIT,
                    balance = -2200.00,
                    accountNumber = "•••• 1182",
                    colorHex = "#8B5CF6",
                    isBiometricLocked = true
                )
            )
            dao.insertAccounts(initialAccounts.map { AccountEntity.fromDomain(it) })

            val now = System.currentTimeMillis()
            val day = 86400000L
            val initialTransactions = listOf(
                Transaction(title = "Salary Deposit", amount = 5000.00, category = BudgetCategory.INCOME, type = TransactionType.INCOME, date = now - (day * 2), accountId = initialAccounts[0].id),
                Transaction(title = "Apple Store", amount = 249.00, category = BudgetCategory.SHOPPING, type = TransactionType.EXPENSE, date = now - (day * 3), accountId = initialAccounts[0].id),
                Transaction(title = "Whole Foods Market", amount = 135.20, category = BudgetCategory.FOOD, type = TransactionType.EXPENSE, date = now - (day * 4), accountId = initialAccounts[0].id),
                Transaction(title = "Apartment Rent", amount = 1800.00, category = BudgetCategory.HOUSING, type = TransactionType.EXPENSE, date = now - (day * 10), accountId = initialAccounts[0].id),
                Transaction(title = "Blue Bottle Coffee", amount = 6.50, category = BudgetCategory.FOOD, type = TransactionType.EXPENSE, date = now, accountId = initialAccounts[0].id)
            )
            dao.insertTransactions(initialTransactions.map { TransactionEntity.fromDomain(it) })

            val initialBudgets = listOf(
                CategoryBudgetEntity(BudgetCategory.FOOD.name, 500.0),
                CategoryBudgetEntity(BudgetCategory.HOUSING.name, 2000.0),
                CategoryBudgetEntity(BudgetCategory.TRANSPORT.name, 200.0),
                CategoryBudgetEntity(BudgetCategory.SHOPPING.name, 300.0),
                CategoryBudgetEntity(BudgetCategory.ENTERTAINMENT.name, 150.0)
            )
            dao.insertBudgets(initialBudgets)
        }
    }

    suspend fun addTransaction(tx: Transaction) = withContext(Dispatchers.IO) {
        dao.insertTransaction(TransactionEntity.fromDomain(tx))
        // Trigger drive backup in background if enabled
        syncService.backupUserDataToDrive(
            accounts = dao.getAllAccounts().map { it.toDomain() },
            transactions = dao.getAllTransactions().map { it.toDomain() },
            portfolio = dao.getAllPortfolio().map { it.toDomain() }
        )
    }

    suspend fun addAccount(acc: BankAccount) = withContext(Dispatchers.IO) {
        dao.insertAccount(AccountEntity.fromDomain(acc))
        syncService.backupUserDataToDrive(
            accounts = dao.getAllAccounts().map { it.toDomain() },
            transactions = dao.getAllTransactions().map { it.toDomain() },
            portfolio = dao.getAllPortfolio().map { it.toDomain() }
        )
    }

    suspend fun updateBudget(category: BudgetCategory, limit: Double) = withContext(Dispatchers.IO) {
        dao.insertBudget(CategoryBudgetEntity(category.name, limit))
    }

    suspend fun eraseAllData() = withContext(Dispatchers.IO) {
        dao.clearAccounts()
        dao.clearTransactions()
        dao.clearBudgets()
        dao.clearPortfolio()
        syncService.eraseAllDriveData()
    }
}