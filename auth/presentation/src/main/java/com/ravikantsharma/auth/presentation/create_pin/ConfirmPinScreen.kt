package com.ravikantsharma.auth.presentation.create_pin

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ravikantsharma.auth.presentation.R
import com.ravikantsharma.auth.presentation.create_pin.component.CreatePinScreenComponent
import com.ravikantsharma.ui.ObserveAsEvent
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun ConfirmPinScreenRoot(
    modifier: Modifier = Modifier,
    onNavigateToRegisterScreen: () -> Unit,
    onNavigateToPreferencesScreen: () -> Unit,
    viewModel: CreatePinViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    ObserveAsEvent(viewModel.events) { event ->
        when (event) {
            CreatePinEvent.NavigateToRegisterScreen -> onNavigateToRegisterScreen()
            CreatePinEvent.NavigateToPreferencesScreen -> onNavigateToPreferencesScreen()
            CreatePinEvent.PinsDoNotMatch -> {
                scope.launch {
                    snackBarHostState.currentSnackbarData?.dismiss()
                    snackBarHostState.showSnackbar(context.getString(R.string.confirm_pin_error_pins_do_not_match))
                }
            }

            is CreatePinEvent.NavigateToConfirmPinScreen -> Unit
        }
    }

    CreatePinScreenComponent(
        modifier = modifier,
        headlineResId = R.string.confirm_pin_headline,
        subHeadlineResId = R.string.confirm_pin_sub_headline,
        snackbarHostState = snackBarHostState,
        uiState = uiState,
        onAction = viewModel::onAction
    )
}