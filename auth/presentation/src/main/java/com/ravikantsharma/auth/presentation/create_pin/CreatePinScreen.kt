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
    onNavigateToConfirmScreen: () -> Unit,
    onNavigateToRegisterScreen: () -> Unit,
    viewModel: CreatePinViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }

    ObserveAsEvent(viewModel.events) { event ->
        when (event) {
            CreatePinEvent.NavigateToConfirmPinScreen -> onNavigateToConfirmScreen()
            CreatePinEvent.NavigateToRegisterScreen -> onNavigateToRegisterScreen()
        }
    }

    CreatePinScreen(
        modifier = modifier,
        uiState = uiState,
        snackBarHostState = snackBarHostState,
        onAction = viewModel::onAction
    )

}

@Composable
fun CreatePinScreen(
    modifier: Modifier = Modifier,
    uiState: CreatePinState,
    snackBarHostState: SnackbarHostState,
    onAction: (CreatePinAction) -> Unit
) {
    Scaffold(
        containerColor = Color.Transparent,
        snackbarHost = {
            ExManagerSnackBarHost(snackBarHostState)
        },
        topBar = {
            ExManagerTopBar(
                onStartIconClick = {
                    onAction(CreatePinAction.OnBackPressed)
                }
            )
        }
    ) { contentPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    imageVector = LoginIcon,
                    contentDescription = stringResource(R.string.login_button_content_description)
                )
                Text(
                    text = stringResource(R.string.create_pin_headline),
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(top = 20.dp)
                )
                Text(
                    text = stringResource(R.string.create_pin_sub_headline),
                    modifier = Modifier.padding(top = 8.dp)
                )
                ExManagerEnterPin(
                    pin = uiState.pin,
                    modifier = Modifier.padding(
                        top = 36.dp,
                        start = 46.dp,
                        end = 46.dp
                    )
                )

                ExManagerPinPad(
                    modifier = Modifier.padding(top = 32.dp),
                    hasBiometricButton = true,
                    onNumberPressedClicked = {
                        onAction(CreatePinAction.OnNumberPressed(it))
                    },
                    onDeletePressedClicked = {
                        onAction(CreatePinAction.OnDeletePressed)
                    }
                )
            }
        }
    }
}

@Composable
@Preview
fun PreviewCreatePinScreen() {
    ExpenseManagerTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            CreatePinScreen(
                uiState = CreatePinState(),
                snackBarHostState = SnackbarHostState(),
                onAction = {}
            )
        }
    }
}