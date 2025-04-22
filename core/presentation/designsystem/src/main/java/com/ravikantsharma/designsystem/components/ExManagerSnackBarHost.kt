package com.ravikantsharma.designsystem.components

import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

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