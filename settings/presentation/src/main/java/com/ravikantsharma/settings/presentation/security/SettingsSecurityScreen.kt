package com.ravikantsharma.settings.presentation.security

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ravikantsharma.core.domain.model.BiometricPromptStatus
import com.ravikantsharma.core.domain.model.LockoutDuration
import com.ravikantsharma.core.domain.model.SessionDuration
import com.ravikantsharma.core.presentation.designsystem.ExpenseManagerTheme
import com.ravikantsharma.core.presentation.designsystem.components.ExManagerScaffold
import com.ravikantsharma.core.presentation.designsystem.components.ExManagerTopBar
import com.ravikantsharma.core.presentation.designsystem.components.SegmentedSelector
import com.ravikantsharma.core.presentation.designsystem.components.buttons.ExManagerButton
import com.ravikantsharma.settings.presentation.R
import com.ravikantsharma.ui.LocalAuthActionHandler
import com.ravikantsharma.ui.ObserveAsEvents
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsSecurityScreenRoot(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
    viewModel: SettingsSecurityViewModel = koinViewModel(),
) {
    val authActionHandler = LocalAuthActionHandler.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    EventHandler(
        events = viewModel.events,
        onNavigateBack = onNavigateBack,
        context = context
    )

    SettingsSecurityScreen(
        modifier = modifier,
        uiState = uiState,
        onAction = { action ->
            when (action) {
                SettingsSecurityAction.OnSaveClicked -> {
                    authActionHandler?.invoke {
                        viewModel.onAction(action)
                    }
                }

                else -> viewModel.onAction(action)
            }
        }
    )
}

@Composable
private fun EventHandler(
    events: Flow<SettingsSecurityEvent>,
    onNavigateBack: () -> Unit,
    context: Context
) {
    ObserveAsEvents(events) { event ->
        when (event) {
            SettingsSecurityEvent.NavigateBack -> onNavigateBack()
            SettingsSecurityEvent.SecuritySettingsSaved -> {
                Toast.makeText(
                    context,
                    context.getString(R.string.security_preferences_saved_successfully),
                    Toast.LENGTH_SHORT
                ).show()
                onNavigateBack()
            }
        }
    }
}

@Composable
fun SettingsSecurityScreen(
    modifier: Modifier = Modifier,
    uiState: SettingsSecurityViewState,
    onAction: (SettingsSecurityAction) -> Unit
) {
    ExManagerScaffold(
        containerColor = Color.Transparent,
        topBar = { SecurityScreenTopBar(onAction) }
    ) { contentPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                SegmentedSelector(
                    title = stringResource(R.string.biometrics_for_pin_prompt),
                    options = BiometricPromptStatus.entries.toTypedArray(),
                    selectedOption = uiState.biometricPromptStatus,
                    onOptionSelected = {
                        onAction(SettingsSecurityAction.OnBiometricSettingUpdated(it))
                    },
                    displayText = {
                        it.getDisplayValue()
                    }
                )

                SegmentedSelector(
                    modifier = Modifier.padding(top = 16.dp),
                    title = stringResource(R.string.session_expiry_duration),
                    options = SessionDuration.entries.toTypedArray(),
                    selectedOption = uiState.sessionExpiryDuration,
                    onOptionSelected = {
                        onAction(SettingsSecurityAction.OnSessionExpiryUpdated(it))
                    },
                    displayText = {
                        it.displayText()
                    }
                )

                SegmentedSelector(
                    modifier = Modifier.padding(top = 16.dp),
                    title = stringResource(R.string.locked_out_duration),
                    options = LockoutDuration.entries.toTypedArray(),
                    selectedOption = uiState.lockedOutDuration,
                    onOptionSelected = {
                        onAction(SettingsSecurityAction.OnLockOutDurationUpdated(it))
                    },
                    displayText = {
                        it.displayText()
                    }
                )

                ExManagerButton(
                    isEnabled = uiState.enableSaveButton,
                    modifier = Modifier.padding(vertical = 34.dp),
                    buttonText = stringResource(R.string.save)
                ) {
                    onAction(SettingsSecurityAction.OnSaveClicked)
                }
            }
        }
    }

}

@Composable
private fun SecurityScreenTopBar(onAction: (SettingsSecurityAction) -> Unit) {
    ExManagerTopBar(
        modifier = Modifier.padding(horizontal = 8.dp),
        title = stringResource(R.string.security),
        titleColor = MaterialTheme.colorScheme.onSurface,
        onStartIconClick = {
            onAction(SettingsSecurityAction.OnBackClicked)
        }
    )
}

@Preview
@Composable
fun PreviewSettingsSecurityScreen() {
    ExpenseManagerTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            SettingsSecurityScreen(
                modifier = Modifier,
                uiState = SettingsSecurityViewState(
                    biometricPromptStatus = BiometricPromptStatus.ENABLE,
                    sessionExpiryDuration = SessionDuration.FIVE_MIN,
                    lockedOutDuration = LockoutDuration.FIFTEEN_SECONDS
                ),
                onAction = {

                }
            )
        }
    }
}