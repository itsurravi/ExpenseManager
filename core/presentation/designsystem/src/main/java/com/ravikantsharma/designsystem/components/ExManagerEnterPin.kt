package com.ravikantsharma.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ravikantsharma.designsystem.ExpenseManagerTheme

@Composable
fun ExManagerEnterPin(
    modifier: Modifier = Modifier,
    pinMaxLength: Int = 5,
    isLocked: Boolean = false,
    pin: String
) {
    Row(
        modifier = modifier
            .padding(vertical = 19.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        List(pinMaxLength) { index ->
            CirclePin(
                isEnabled = index < pin.length,
                isLocked = isLocked
            )
        }
    }
}

@Composable
private fun CirclePin(
    isEnabled: Boolean,
    isLocked: Boolean
) {
    Box(
        modifier = Modifier
            .size(18.dp)
            .clip(CircleShape)
            .background(
                when {
                    isLocked -> MaterialTheme.colorScheme.onBackground.copy(alpha = 0.05f) // Lighter when locked
                    isEnabled -> MaterialTheme.colorScheme.primary
                    else -> MaterialTheme.colorScheme.onBackground.copy(alpha = 0.12f)
                }
            )
    )
}

@Preview
@Composable
private fun PreviewEnterPin() {
    ExpenseManagerTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            ExManagerEnterPin(
                isLocked = true,
                pin = "123",
            )
        }
    }
}