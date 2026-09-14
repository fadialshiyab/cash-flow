package com.sheyab.cashflow.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sheyab.cashflow.ui.theme.*

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 20.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(Color.White.copy(alpha = 0.12f), Color.White.copy(alpha = 0.03f))
                ),
                shape = RoundedCornerShape(cornerRadius)
            ),
        colors = CardDefaults.cardColors(containerColor = BgCardDark),
        shape = RoundedCornerShape(cornerRadius)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            content = content
        )
    }
}