package com.ravikantsharma.dashboard.presentation.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ravikantsharma.ui.ObserveAsEvent
import org.koin.androidx.compose.koinViewModel

@Composable
fun DashboardScreenRoot(
    viewModel: DashboardViewModel = koinViewModel()
) {
    ObserveAsEvent(viewModel.events) {
        when(it) {
            DashboardEvent.NavigateTest -> {

            }
        }
    }

    DashboardScreen {
        viewModel.triggerPin()
    }
}

@Composable
fun DashboardScreen(
    onClick: () -> Unit
) {
    Column {
        Text("Da")
        Button(onClick = onClick) {
            Text("Navigate")
        }
    }
}

@Composable
@Preview
fun PreviewDashboardScreen() {
    DashboardScreen {
    }
}
