package com.ravikantsharma.settings.presentation.security

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ravikantsharma.core.domain.model.BiometricPromptStatus
import com.ravikantsharma.core.domain.model.LockoutDuration
import com.ravikantsharma.core.domain.model.SessionDuration
import com.ravikantsharma.core.presentation.designsystem.ExpenseManagerTheme
import com.ravikantsharma.core.presentation.designsystem.components.ExManagerTopBar
import com.ravikantsharma.core.presentation.designsystem.components.SegmentedSelector
import com.ravikantsharma.core.presentation.designsystem.components.buttons.ExManagerButton
import com.ravikantsharma.ui.LocalAuthActionHandler
import com.ravikantsharma.ui.ObserveAsEvent
import org.koin.androidx.compose.koinViewModel
import kotlin.invoke

@Composable
fun SettingsSecurityScreenRoot(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
    viewModel: SettingsSecurityViewModel = koinViewModel(),
) {
    val authActionHandler = LocalAuthActionHandler.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    ObserveAsEvent(viewModel.events) { event ->
        when (event) {
            SettingsSecurityEvent.NavigateBack -> onNavigateBack()
            SettingsSecurityEvent.SecuritySettingsSaved -> {
                Toast.makeText(
                    context,
                    "Security Preferences saved successfully!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

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
fun SettingsSecurityScreen(
    modifier: Modifier = Modifier,
    uiState: SettingsSecurityViewState,
    onAction: (SettingsSecurityAction) -> Unit
) {
    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            ExManagerTopBar(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .padding(horizontal = 8.dp),
                title = "Security",
                titleColor = MaterialTheme.colorScheme.onSurface,
                onStartIconClick = {
                    onAction(SettingsSecurityAction.OnBackClicked)
                }
            )
        }) { contentPadding ->
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
                    title = "Biometrics for PIN prompt",
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
                    title = "Session expiry duration",
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
                    title = "Locked out duration",
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
                    buttonText = "Save"
                ) {
                    onAction(SettingsSecurityAction.OnSaveClicked)
                }
            }
        }
    }

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
                    sessionExpiryDuration = SessionDuration.ONE_MIN,
                    lockedOutDuration = LockoutDuration.FIFTEEN_SECONDS
                ),
                onAction = {

                }
            )
        }
    }
}