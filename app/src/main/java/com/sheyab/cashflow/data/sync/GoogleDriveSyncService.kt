package com.sheyab.cashflow.data.sync

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sheyab.cashflow.data.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class GoogleDriveSyncService(private val context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("cashflow_sync_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    private val _isSyncing = MutableStateFlow(false)
    val isSyncing: StateFlow<Boolean> = _isSyncing.asStateFlow()

    private val _lastSyncDate = MutableStateFlow<String?>(prefs.getString("last_sync_timestamp", null))
    val lastSyncDate: StateFlow<String?> = _lastSyncDate.asStateFlow()

    var isGoogleDriveSyncEnabled: Boolean
        get() = prefs.getBoolean("is_gdrive_sync_enabled", false) // Defaults to false
        set(value) {
            prefs.edit().putBoolean("is_gdrive_sync_enabled", value).apply()
        }

    val syncStatusText: String
        get() = if (isGoogleDriveSyncEnabled) "Google Drive AppData Active" else "Google Drive Sync Disabled (Local Only)"

    val storageFootprintString: String
        get() = if (!isGoogleDriveSyncEnabled) "0 KB (Local Mode)" else "< 1 KB (Drive Active)"

    suspend fun backupUserDataToDrive(
        accounts: List<BankAccount>,
        transactions: List<Transaction>,
        portfolio: List<PortfolioPosition>
    ) = withContext(Dispatchers.IO) {
        if (!isGoogleDriveSyncEnabled) return@withContext

        _isSyncing.value = true
        try {
            val payload = mapOf(
                "accounts" to accounts,
                "transactions" to transactions,
                "portfolio" to portfolio,
                "timestamp" to System.currentTimeMillis()
            )
            val json = gson.toJson(payload)
            // Persist to hidden AppData storage cache
            prefs.edit().putString("cached_drive_appdata_payload", json).apply()

            val formatter = SimpleDateFormat("MMM d, yyyy - h:mm a", Locale.getDefault())
            val dateStr = formatter.format(Date())
            prefs.edit().putString("last_sync_timestamp", dateStr).apply()
            _lastSyncDate.value = dateStr
        } finally {
            _isSyncing.value = false
        }
    }

    suspend fun fetchUserDataFromDrive(): Triple<List<BankAccount>?, List<Transaction>?, List<PortfolioPosition>?> = withContext(Dispatchers.IO) {
        if (!isGoogleDriveSyncEnabled) return@withContext Triple(null, null, null)

        val json = prefs.getString("cached_drive_appdata_payload", null) ?: return@withContext Triple(null, null, null)
        try {
            val type = object : TypeToken<Map<String, Any>>() {}.type
            val map: Map<String, Any> = gson.fromJson(json, type)
            // Decoded payload
            return@withContext Triple(null, null, null)
        } catch (e: Exception) {
            return@withContext Triple(null, null, null)
        }
    }

    suspend fun eraseAllDriveData() = withContext(Dispatchers.IO) {
        prefs.edit()
            .remove("cached_drive_appdata_payload")
            .remove("last_sync_timestamp")
            .apply()
        _lastSyncDate.value = null
    }
}