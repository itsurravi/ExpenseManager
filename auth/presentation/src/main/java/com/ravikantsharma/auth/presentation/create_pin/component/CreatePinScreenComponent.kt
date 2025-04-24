package com.ravikantsharma.auth.presentation.create_pin.component

import androidx.annotation.StringRes
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ravikantsharma.auth.presentation.R
import com.ravikantsharma.auth.presentation.create_pin.CreatePinAction
import com.ravikantsharma.auth.presentation.create_pin.CreatePinState
import com.ravikantsharma.designsystem.LoginIcon
import com.ravikantsharma.designsystem.components.ExManagerEnterPin
import com.ravikantsharma.designsystem.components.ExManagerPinPad
import com.ravikantsharma.designsystem.components.ExManagerSnackBarHost
import com.ravikantsharma.designsystem.components.ExManagerTopBar

@Composable
fun CreatePinScreenComponent(
    modifier: Modifier = Modifier,
    @StringRes headlineResId: Int,
    @StringRes subHeadlineResId: Int,
    snackbarHostState: SnackbarHostState,
    uiState: CreatePinState,
    onAction: (CreatePinAction) -> Unit,
) {
    Scaffold(
        containerColor = Color.Transparent,
        snackbarHost = {
            ExManagerSnackBarHost(snackbarHostState)
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
                    text = stringResource(headlineResId),
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(top = 20.dp)
                )
                Text(
                    text = stringResource(subHeadlineResId),
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