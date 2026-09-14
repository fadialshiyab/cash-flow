package com.sheyab.cashflow.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.sheyab.cashflow.data.sync.GoogleDriveSyncService
import com.sheyab.cashflow.ui.components.GlassCard
import com.sheyab.cashflow.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    syncService: GoogleDriveSyncService,
    onEraseAllData: () -> Unit
) {
    var isDriveSyncEnabled by remember { mutableStateOf(syncService.isGoogleDriveSyncEnabled) }
    var showEraseDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings", color = TextPrimary, fontWeight = FontWeight.Bold) },
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
            // App Header & Icon Card
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    cornerRadius = 22.dp
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Brush.linearGradient(listOf(AccentBlue, AccentCyan))),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("$", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 28.sp)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("CashFlow", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("Version 1.4 (Android Build 1)", color = TextSecondary, fontSize = 12.sp)
                    }
                }
            }

            // Google Drive Cloud Sync Section (Off by Default)
            item {
                Text(
                    text = "Cloud Sync & Backup",
                    color = TextSecondary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    cornerRadius = 18.dp
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CloudSync,
                                contentDescription = "Google Drive",
                                tint = if (isDriveSyncEnabled) AccentGreen else TextMuted,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Sync with Google Drive", color = TextPrimary, fontWeight = FontWeight.SemiBold)
                                Text(
                                    if (isDriveSyncEnabled) "Active (Hidden AppData Container)" else "Disabled by default (Local Only)",
                                    color = TextSecondary,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        Switch(
                            checked = isDriveSyncEnabled,
                            onCheckedChange = { checked ->
                                isDriveSyncEnabled = checked
                                syncService.isGoogleDriveSyncEnabled = checked
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = AccentBlue
                            )
                        )
                    }
                }
            }

            // Privacy & Security Info
            item {
                Text(
                    text = "Data Protection & Privacy",
                    color = TextSecondary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                GlassCard(modifier = Modifier.fillMaxWidth(), cornerRadius = 18.dp) {
                    SettingsInfoRow(
                        icon = Icons.Default.Lock,
                        title = "Offline-First Engine",
                        subtitle = "All accounts and transactions are stored locally on your device's Room DB."
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    SettingsInfoRow(
                        icon = Icons.Default.Fingerprint,
                        title = "Biometric Security",
                        subtitle = "Lock sensitive card details with Android BiometricPrompt."
                    )
                }
            }

            // Danger Zone (Erase Local & Cloud Data)
            item {
                Text(
                    text = "Danger Zone",
                    color = AccentRed,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { showEraseDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = AccentRed.copy(alpha = 0.18f)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Erase", tint = AccentRed)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Erase Local & Cloud Data", color = AccentRed, fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    if (showEraseDialog) {
        AlertDialog(
            onDismissRequest = { showEraseDialog = false },
            title = { Text("Erase All Data?", color = TextPrimary) },
            text = { Text("This will permanently remove all accounts, transactions, and backups. This action cannot be undone.", color = TextSecondary) },
            confirmButton = {
                TextButton(
                    onClick = {
                        onEraseAllData()
                        showEraseDialog = false
                    }
                ) {
                    Text("Erase Everything", color = AccentRed, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEraseDialog = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            },
            containerColor = BgCardDark
        )
    }
}

@Composable
fun SettingsInfoRow(icon: ImageVector, title: String, subtitle: String) {
    Row(verticalAlignment = Alignment.Top) {
        Icon(imageVector = icon, contentDescription = title, tint = AccentCyan, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(title, color = TextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Text(subtitle, color = TextSecondary, fontSize = 12.sp)
        }
    }
}