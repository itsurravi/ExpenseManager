package com.ravikantsharma.auth.presentation.create_pin

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ravikantsharma.designsystem.ExpenseManagerTheme
import com.ravikantsharma.ui.ObserveAsEvent
import org.koin.androidx.compose.koinViewModel

@Composable
fun CreatePinScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: CreatePinViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ObserveAsEvent(viewModel.events) { event ->
        when (event) {
            CreatePinEvent.NavigateToConfirmPinScreen -> Unit
        }
    }

    CreatePinScreen(
        modifier = modifier,
        uiState = uiState
    )

}

@Composable
fun CreatePinScreen(
    modifier: Modifier = Modifier,
    uiState: CreatePinState
) {
    Text("Create Pin Screen")
}

@Composable
@Preview
fun PreviewCreatePinScreen() {
    ExpenseManagerTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            CreatePinScreen(uiState = CreatePinState())
        }
    }
}