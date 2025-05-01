package com.ravikantsharma.core.presentation.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ravikantsharma.core.presentation.designsystem.ExpenseManagerTheme
import com.ravikantsharma.core.presentation.designsystem.primaryFixed

@Composable
fun LargestTransactionView(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    amount: String,
    date: String,
    emptyTransactionsText: String = "Your largest transaction will appear here",
    isEmptyTransactions: Boolean = false
) {
    Box(
        modifier = modifier
            .background(
                color = primaryFixed,
                shape = RoundedCornerShape(16.dp)
            )
            .defaultMinSize(minHeight = 72.dp)
            .padding(horizontal = 12.dp)
    ) {
        if (isEmptyTransactions) {
            Text(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .align(Alignment.Center),
                text = emptyTransactionsText,
                style = MaterialTheme.typography.titleMedium
            )
        } else {
            Row(
                modifier = modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        modifier = Modifier.padding(top = 2.dp),
                        text = description,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            fontSize = 12.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.width(24.dp))

                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = amount,
                        style = MaterialTheme.typography.titleLarge
                    )

                    Text(
                        modifier = Modifier.padding(top = 2.dp),
                        text = date,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            fontSize = 12.sp
                        )
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewLargestTransactionView() {
    ExpenseManagerTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            LargestTransactionView(
                modifier = Modifier.padding(12.dp),
                title = "Adobe Yearly",
                description = "Largest transaction",
                amount = "-\$59.99",
                date = "Jan 7, 2025",
                isEmptyTransactions = false
            )
        }
    }
}

@Preview
@Composable
fun PreviewLargestTransactionEmptyView() {
    ExpenseManagerTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            LargestTransactionView(
                modifier = Modifier.padding(12.dp),
                title = "Adobe Yearly",
                description = "Largest transaction",
                amount = "-\$59.99",
                date = "Jan 7, 2025",
                isEmptyTransactions = true
            )
        }
    }
}