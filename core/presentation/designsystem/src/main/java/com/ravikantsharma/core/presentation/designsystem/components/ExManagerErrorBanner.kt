package com.ravikantsharma.core.presentation.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ravikantsharma.core.presentation.designsystem.ExManagerWhite

@Composable
fun ExManagerErrorBanner(
    modifier: Modifier = Modifier,
    text: String
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.error),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.padding(
                vertical = 12.dp,
                horizontal = 16.dp
            ),
            text = text,
            style = MaterialTheme.typography.labelMedium.copy(
                color = ExManagerWhite
            )
        )
    }
}