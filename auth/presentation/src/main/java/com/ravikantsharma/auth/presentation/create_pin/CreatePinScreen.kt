package com.ravikantsharma.auth.presentation.create_pin

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ravikantsharma.auth.presentation.R
import com.ravikantsharma.auth.presentation.create_pin.component.CreatePinScreenComponent
import com.ravikantsharma.auth.presentation.navigation.model.CreatePinData
import com.ravikantsharma.designsystem.ExpenseManagerTheme
import com.ravikantsharma.designsystem.LoginIcon
import com.ravikantsharma.designsystem.components.ExManagerEnterPin
import com.ravikantsharma.designsystem.components.ExManagerPinPad
import com.ravikantsharma.designsystem.components.ExManagerSnackBarHost
import com.ravikantsharma.designsystem.components.ExManagerTopBar
import com.ravikantsharma.ui.ObserveAsEvent
import org.koin.androidx.compose.koinViewModel

@Composable
fun CreatePinScreenRoot(
    modifier: Modifier = Modifier,
    onNavigateToConfirmScreen: (CreatePinData) -> Unit,
    onNavigateToRegisterScreen: () -> Unit,
    viewModel: CreatePinViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }

    ObserveAsEvent(viewModel.events) { event ->
        when (event) {
            is CreatePinEvent.NavigateToConfirmPinScreen -> onNavigateToConfirmScreen(event.screenData)
            CreatePinEvent.NavigateToRegisterScreen -> onNavigateToRegisterScreen()
            else -> Unit
        }
    }

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
@Preview
fun PreviewCreatePinScreen() {
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