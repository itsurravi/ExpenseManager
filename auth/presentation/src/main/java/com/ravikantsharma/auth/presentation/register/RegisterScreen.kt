package com.ravikantsharma.auth.presentation.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ravikantsharma.auth.presentation.R
import com.ravikantsharma.auth.presentation.login.component.ExManagerClickableText
import com.ravikantsharma.ui.navigation.CreatePinScreenData
import com.ravikantsharma.core.presentation.designsystem.ArrowForward
import com.ravikantsharma.core.presentation.designsystem.ExpenseManagerTheme
import com.ravikantsharma.core.presentation.designsystem.LoginIcon
import com.ravikantsharma.core.presentation.designsystem.components.buttons.ExManagerButton
import com.ravikantsharma.core.presentation.designsystem.components.ExManagerHeadlineTextField
import com.ravikantsharma.core.presentation.designsystem.components.ExManagerSnackBarHost
import com.ravikantsharma.ui.ObserveAsEvent
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegisterScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel = koinViewModel(),
    onAlreadyHaveAnAccountClick: () -> Unit,
    onNavigateToPinScreen: (CreatePinScreenData) -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope()

    ObserveAsEvent(viewModel.events) { event ->
        when (event) {
            RegisterEvent.SuccessfulRegistration -> {
                keyboardController?.hide()
            }

            RegisterEvent.UsernameTaken -> Unit
            RegisterEvent.NavigateToRegisterScreen -> {
                keyboardController?.hide()
                onAlreadyHaveAnAccountClick()
            }

            RegisterEvent.IncorrectUsername -> {
                scope.launch {
                    snackBarHostState.currentSnackbarData?.dismiss()
                    snackBarHostState.showSnackbar(
                        message = context.getString(R.string.common_incorrect_username_format),
                        duration = SnackbarDuration.Short
                    )
                }
            }

            is RegisterEvent.NavigateToPinScreen -> {
                keyboardController?.hide()
                onNavigateToPinScreen(event.screenData)
            }

            RegisterEvent.DuplicateUsername -> {
                scope.launch {
                    snackBarHostState.currentSnackbarData?.dismiss()
                    snackBarHostState.showSnackbar(
                        message = context.getString(R.string.common_error_username_taken),
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }

    RegisterScreen(
        modifier = modifier,
        uiState = uiState,
        snackBarHostState = snackBarHostState,
        onAction = viewModel::onAction
    )
}

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    uiState: RegisterViewState,
    snackBarHostState: SnackbarHostState,
    onAction: (RegisterAction) -> Unit
) {
    Scaffold(containerColor = Color.Transparent, snackbarHost = {
        ExManagerSnackBarHost(snackBarHostState)
    }) { contentPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(contentPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.padding(12.dp))
            Image(
                imageVector = LoginIcon,
                contentDescription = stringResource(R.string.login_button_content_description)
            )

            Text(
                modifier = Modifier.padding(
                    top = 20.dp, start = 26.dp, end = 26.dp
                ),
                text = stringResource(R.string.register_headline),
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )

            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = stringResource(R.string.register_create_unique_username),
                style = MaterialTheme.typography.bodyMedium
            )

            ExManagerHeadlineTextField(
                value = uiState.username,
                modifier = Modifier.padding(top = 36.dp, start = 26.dp, end = 26.dp),
                onValueChange = {
                    onAction(RegisterAction.OnUserNameChanged(it))
                },
                hint = stringResource(R.string.login_username)
            )

            ExManagerButton(
                modifier = Modifier.padding(
                    top = 16.dp,
                    start = 26.dp,
                    end = 26.dp
                ),
                buttonText = stringResource(R.string.common_next),
                onClick = {
                    onAction(RegisterAction.OnNextClicked)
                },
                isEnabled = uiState.isNextEnabled,
                icon = ArrowForward
            )

            ExManagerClickableText(
                modifier = Modifier.padding(
                    top = 16.dp,
                    start = 26.dp,
                    end = 26.dp
                ),
                text = stringResource(R.string.register_already_have_an_account)
            ) {
                onAction(RegisterAction.OnAlreadyHaveAnAccountClicked)
            }
        }
    }
}

@Preview
@Composable
fun PreviewRegisterScreen() {
    ExpenseManagerTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            RegisterScreen(
                uiState = RegisterViewState(), snackBarHostState = SnackbarHostState()
            ) {

            }
        }
    }
}