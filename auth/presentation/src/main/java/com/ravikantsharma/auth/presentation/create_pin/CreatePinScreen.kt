package com.ravikantsharma.auth.presentation.create_pin

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ravikantsharma.auth.presentation.R
import com.ravikantsharma.auth.presentation.create_pin.component.CreatePinScreenComponent
import com.ravikantsharma.ui.navigation.CreatePinScreenData
import com.ravikantsharma.core.presentation.designsystem.ExpenseManagerTheme
import com.ravikantsharma.ui.ObserveAsEvents
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel

@Composable
fun CreatePinScreenRoot(
    modifier: Modifier = Modifier,
    onNavigateToConfirmScreen: (CreatePinScreenData) -> Unit,
    onBackClick: () -> Unit,
    viewModel: CreatePinViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }

    EventHandler(
        events = viewModel.events,
        onNavigateToConfirmScreen = onNavigateToConfirmScreen,
        onBackClick = onBackClick
    )

    CreatePinScreenComponent(
        modifier = modifier,
        headlineResId = R.string.create_pin_headline,
        subHeadlineResId = R.string.create_pin_sub_headline,
        snackbarHostState = snackBarHostState,
        uiState = uiState,
        onAction = viewModel::onAction,
    )
}

@Composable
private fun EventHandler(
    events: Flow<CreatePinEvent>,
    onNavigateToConfirmScreen: (CreatePinScreenData) -> Unit,
    onBackClick: () -> Unit
) {
    ObserveAsEvents(events) { event ->
        when (event) {
            is CreatePinEvent.NavigateToConfirmPinScreen -> onNavigateToConfirmScreen(event.screenData)
            CreatePinEvent.OnBackClick -> onBackClick()
            else -> Unit
        }
    }
}

@Composable
@Preview
private fun PreviewCreatePinScreen() {
    ExpenseManagerTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            CreatePinScreenComponent(
                uiState = CreatePinState(),
                snackbarHostState = SnackbarHostState(),
                onAction = {},
                headlineResId = R.string.create_pin_headline,
                subHeadlineResId = R.string.create_pin_sub_headline,
            )
        }
    }
}