package com.ravikantsharma.dashboard.presentation.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun DashboardScreenRoot(modifier: Modifier = Modifier) {
    DashboardScreen()
}

@Composable
fun DashboardScreen() {
    Column {
        Text("Da")
        Button(onClick = {

        }) {
            Text("Navigate")
        }
    }
}

@Composable
@Preview
fun PreviewDashboardScreen() {
    DashboardScreen()
}