package com.sheyab.cashflow.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sheyab.cashflow.data.models.BankAccount
import com.sheyab.cashflow.ui.components.GlassCard
import com.sheyab.cashflow.ui.theme.*
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardsScreen(
    accounts: List<BankAccount>,
    onAddCard: () -> Unit
) {
    val currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cards & Accounts", color = TextPrimary, fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = onAddCard) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Add Card", tint = AccentBlue)
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
            items(accounts) { acc ->
                val cardColor = try { Color(android.graphics.Color.parseColor(acc.colorHex)) } catch (e: Exception) { AccentBlue }
                
                GlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(170.dp),
                    cornerRadius = 20.dp
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(acc.name, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            if (acc.isBiometricLocked) {
                                Icon(imageVector = Icons.Default.Lock, contentDescription = "Locked", tint = AccentGreen, modifier = Modifier.size(18.dp))
                            }
                        }

                        Text(
                            text = currencyFormatter.format(acc.balance),
                            color = TextPrimary,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 24.sp
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(acc.accountNumber, color = TextSecondary, fontSize = 13.sp)
                            Text(acc.type.displayName, color = cardColor, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}