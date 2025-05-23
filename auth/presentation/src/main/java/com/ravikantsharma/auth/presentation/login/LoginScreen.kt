package com.ravikantsharma.auth.presentation.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ravikantsharma.auth.presentation.R
import com.ravikantsharma.auth.presentation.login.component.ExManagerClickableText
import com.ravikantsharma.core.presentation.designsystem.ExpenseManagerTheme
import com.ravikantsharma.core.presentation.designsystem.LoginIcon
import com.ravikantsharma.core.presentation.designsystem.components.ExManagerScaffold
import com.ravikantsharma.core.presentation.designsystem.components.ExManagerSnackBarHost
import com.ravikantsharma.core.presentation.designsystem.components.buttons.ExManagerButton
import com.ravikantsharma.core.presentation.designsystem.components.text_field.ExManagerTextField
import com.ravikantsharma.ui.ObserveAsEvent
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreenRoot(
    modifier: Modifier = Modifier,
    onRegisterClick: () -> Unit,
    onNavigateToDashboard: () -> Unit,
    viewModel: LoginViewModel = koinViewModel()
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }
    val keyboardController = LocalSoftwareKeyboardController.current

    ObserveAsEvent(viewModel.events) { event ->
        when (event) {
            LoginEvent.IncorrectCredentials -> {
                scope.launch {
                    snackBarHostState.currentSnackbarData?.dismiss()
                    snackBarHostState.showSnackbar(
                        message = context.getString(R.string.login_error_username_or_pin_is_incorrect),
                        duration = SnackbarDuration.Short
                    )
                }
            }

            LoginEvent.NavigateToRegisterScreen -> {
                keyboardController?.hide()
                onRegisterClick()
            }

            LoginEvent.NavigateToDashboardScreen -> {
                keyboardController?.hide()
                onNavigateToDashboard()
            }
        }
    }

    LoginScreen(
        modifier = modifier,
        uiState = uiState,
        snackBarHostState = snackBarHostState,
        onAction = viewModel::onAction
    )
}

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    uiState: LoginViewState,
    snackBarHostState: SnackbarHostState,
    onAction: (LoginAction) -> Unit
) {
    ExManagerScaffold(
        snackbarHost = {
            ExManagerSnackBarHost(snackBarHostState)
        }
    ) { contentPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(contentPadding)
                .windowInsetsPadding(WindowInsets.systemBars),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.padding(12.dp))
            Image(
                imageVector = LoginIcon,
                contentDescription = stringResource(R.string.login_button_content_description)
            )
            Text(
                modifier = Modifier.padding(top = 20.dp),
                text = stringResource(R.string.login_welcome_back),
                style = MaterialTheme.typography.headlineMedium,
            )

            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = stringResource(R.string.login_enter_your_details),
                style = MaterialTheme.typography.bodyMedium
            )

            ExManagerTextField(
                value = uiState.username,
                onValueChange = {
                    onAction(LoginAction.OnUsernameUpdate(it))
                },
                hint = stringResource(R.string.login_username),
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 36.dp
                )
            )

            ExManagerTextField(
                value = uiState.pin,
                onValueChange = {
                    onAction(LoginAction.OnPinChange(it))
                },
                hint = stringResource(R.string.login_pin),
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done,
                onDone = {
                    onAction(LoginAction.OnLoginClick)
                },
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp
                )
            )

            ExManagerButton(
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 24.dp
                ),
                buttonText = stringResource(R.string.login_log_in)
            ) {
                onAction(LoginAction.OnLoginClick)
            }

            ExManagerClickableText(
                modifier = Modifier.padding(top = 28.dp),
                text = stringResource(R.string.login_new_to_spend_less)
            ) {
                onAction(LoginAction.OnRegisterClick)
            }
        }
    }
}

@Preview
@Composable
fun PreviewLoginScreen() {
    ExpenseManagerTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            LoginScreen(
                modifier = Modifier,
                uiState = LoginViewState(),
                SnackbarHostState()
            ) {

            }
        }
    }
}