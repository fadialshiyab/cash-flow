package com.sheyab.cashflow.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FinanceDao {
    // Accounts
    @Query("SELECT * FROM accounts")
    fun getAllAccountsFlow(): Flow<List<AccountEntity>>

    @Query("SELECT * FROM accounts")
    suspend fun getAllAccounts(): List<AccountEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(account: AccountEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccounts(accounts: List<AccountEntity>)

    @Delete
    suspend fun deleteAccount(account: AccountEntity)

    // Transactions
    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun getAllTransactionsFlow(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions ORDER BY date DESC")
    suspend fun getAllTransactions(): List<TransactionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransactions(transactions: List<TransactionEntity>)

    @Delete
    suspend fun deleteTransaction(transaction: TransactionEntity)

    // Budgets
    @Query("SELECT * FROM category_budgets")
    fun getAllBudgetsFlow(): Flow<List<CategoryBudgetEntity>>

    @Query("SELECT * FROM category_budgets")
    suspend fun getAllBudgets(): List<CategoryBudgetEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBudget(budget: CategoryBudgetEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBudgets(budgets: List<CategoryBudgetEntity>)

    // Portfolio
    @Query("SELECT * FROM portfolio_positions")
    fun getAllPortfolioFlow(): Flow<List<PortfolioEntity>>

    @Query("SELECT * FROM portfolio_positions")
    suspend fun getAllPortfolio(): List<PortfolioEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPortfolio(positions: List<PortfolioEntity>)

    // Danger Zone
    @Query("DELETE FROM accounts")
    suspend fun clearAccounts()

    @Query("DELETE FROM transactions")
    suspend fun clearTransactions()

    @Query("DELETE FROM category_budgets")
    suspend fun clearBudgets()

    @Query("DELETE FROM portfolio_positions")
    suspend fun clearPortfolio()
}