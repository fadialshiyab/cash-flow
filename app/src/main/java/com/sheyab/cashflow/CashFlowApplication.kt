package com.sheyab.cashflow

import android.app.Application
import com.sheyab.cashflow.data.local.AppDatabase
import com.sheyab.cashflow.data.repository.FinanceRepository
import com.sheyab.cashflow.data.sync.GoogleDriveSyncService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CashFlowApplication : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    val syncService by lazy { GoogleDriveSyncService(this) }
    val repository by lazy { FinanceRepository(database.financeDao(), syncService) }

    override fun onCreate() {
        super.onCreate()
        CoroutineScope(Dispatchers.IO).launch {
            repository.seedInitialDataIfEmpty()
        }
    }
}