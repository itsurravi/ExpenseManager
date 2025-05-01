package com.ravikantsharma.core.presentation.designsystem.components

import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable

@Composable
fun ExManagerSnackBarHost(
    snackBarHostState: SnackbarHostState
) {
    SnackbarHost(hostState = snackBarHostState) { data ->
        ExManagerErrorBanner(
            text = data.visuals.message
        )
    }
}