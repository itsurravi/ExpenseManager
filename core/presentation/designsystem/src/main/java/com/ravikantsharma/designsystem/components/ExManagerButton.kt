package com.ravikantsharma.designsystem.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ExManagerButton(
    modifier: Modifier = Modifier,
    buttonText: String,
    onClick: () -> Unit,
) {

    Button(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp),
        onClick = onClick,
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = buttonText,
            style = MaterialTheme.typography.titleMedium
        )
    }
}