package com.ravikantsharma.auth.presentation.register

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ravikantsharma.auth.presentation.R
import com.ravikantsharma.auth.presentation.login.component.ExManagerClickableText
import com.ravikantsharma.core.presentation.designsystem.ArrowForward
import com.ravikantsharma.core.presentation.designsystem.ExpenseManagerTheme
import com.ravikantsharma.core.presentation.designsystem.LoginIcon
import com.ravikantsharma.core.presentation.designsystem.components.ExManagerHeadlineTextField
import com.ravikantsharma.core.presentation.designsystem.components.ExManagerScaffold
import com.ravikantsharma.core.presentation.designsystem.components.ExManagerSnackBarHost
import com.ravikantsharma.core.presentation.designsystem.components.buttons.ExManagerButton
import com.ravikantsharma.ui.ObserveAsEvents
import com.ravikantsharma.ui.navigation.CreatePinScreenData
import com.ravikantsharma.ui.showTimedSnackBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
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

    EventHandler(
        events = viewModel.events,
        snackBarHostState = snackBarHostState,
        scope = scope,
        context = context,
        keyboardController = keyboardController,
        onAlreadyHaveAnAccountClick = onAlreadyHaveAnAccountClick,
        onNavigateToPinScreen = onNavigateToPinScreen
    )

    RegisterScreen(
        modifier = modifier,
        uiState = uiState,
        snackBarHostState = snackBarHostState,
        onAction = viewModel::onAction
    )
}

@Composable
private fun EventHandler(
    events: Flow<RegisterEvent>,
    snackBarHostState: SnackbarHostState,
    scope: CoroutineScope,
    context: Context,
    keyboardController: SoftwareKeyboardController?,
    onAlreadyHaveAnAccountClick: () -> Unit,
    onNavigateToPinScreen: (CreatePinScreenData) -> Unit
) {
    ObserveAsEvents(events) { event ->
        when (event) {
            RegisterEvent.SuccessfulRegistration -> {
                keyboardController?.hide()
            }
            RegisterEvent.UsernameTaken -> Unit
            RegisterEvent.NavigateToLoginScreen -> {
                keyboardController?.hide()
                onAlreadyHaveAnAccountClick()
            }

            RegisterEvent.IncorrectUsername -> {
                scope.showTimedSnackBar(
                    snackBarHostState = snackBarHostState,
                    message = context.getString(R.string.common_incorrect_username_format)
                )
            }

            is RegisterEvent.NavigateToPinScreen -> {
                keyboardController?.hide()
                onNavigateToPinScreen(event.screenData)
            }

            RegisterEvent.DuplicateUsername -> {
                scope.showTimedSnackBar(
                    snackBarHostState = snackBarHostState,
                    message = context.getString(R.string.common_error_username_taken)
                )
            }
        }
    }
}

@Composable
private fun RegisterScreen(
    modifier: Modifier = Modifier,
    uiState: RegisterViewState,
    snackBarHostState: SnackbarHostState,
    onAction: (RegisterAction) -> Unit
) {
    ExManagerScaffold(
        containerColor = Color.Transparent,
        snackbarHost = { ExManagerSnackBarHost(snackBarHostState) }
    ) { contentPadding ->
        RegisterContent(
            modifier = modifier.padding(contentPadding),
            uiState = uiState,
            onAction = onAction
        )
    }
}

@Composable
private fun RegisterContent(
    modifier: Modifier = Modifier,
    uiState: RegisterViewState,
    onAction: (RegisterAction) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 26.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(36.dp))

        IntroductionContent()

        Spacer(modifier = Modifier.height(36.dp))

        ExManagerHeadlineTextField(
            value = uiState.username,
            onValueChange = { onAction(RegisterAction.OnUserNameChanged(it)) },
            hint = stringResource(R.string.login_username)
        )

        Spacer(modifier = Modifier.height(16.dp))

        FooterContent(onAction, uiState)
    }
}

@Composable
private fun IntroductionContent() {
    Image(
        imageVector = LoginIcon,
        contentDescription = stringResource(R.string.login_button_content_description)
    )

    Spacer(modifier = Modifier.height(20.dp))

    Text(
        text = stringResource(R.string.register_headline),
        style = MaterialTheme.typography.headlineMedium,
        textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.height(8.dp))

    Text(
        text = stringResource(R.string.register_create_unique_username),
        style = MaterialTheme.typography.bodyMedium
    )
}

@Composable
private fun FooterContent(
    onAction: (RegisterAction) -> Unit,
    uiState: RegisterViewState
) {
    ExManagerButton(
        buttonText = stringResource(R.string.common_next),
        onClick = { onAction(RegisterAction.OnNextClicked) },
        isEnabled = uiState.isNextEnabled,
        icon = ArrowForward
    )

    Spacer(modifier = Modifier.height(28.dp))

    ExManagerClickableText(
        text = stringResource(R.string.register_already_have_an_account)
    ) {
        onAction(RegisterAction.OnAlreadyHaveAnAccountClicked)
    }
}

@Preview
@Composable
private fun PreviewRegisterScreen() {
    ExpenseManagerTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            RegisterScreen(
                uiState = RegisterViewState(), snackBarHostState = SnackbarHostState()
            ) {

            }
        }
    }
}