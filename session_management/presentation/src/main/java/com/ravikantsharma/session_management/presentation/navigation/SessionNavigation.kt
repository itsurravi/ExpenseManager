package com.ravikantsharma.session_management.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.ravikantsharma.session_management.presentation.pin_prompt.PinPromptScreenRoot
import com.ravikantsharma.ui.navigation.PinPromptScreenRoute
import com.ravikantsharma.ui.navigation.SessionBaseRoute

fun NavGraphBuilder.sessionNavGraph(
    navController: NavController,
    onVerificationSuccess: () -> Unit,
    onLogout: () -> Unit,
) {
    navigation<SessionBaseRoute>(
        startDestination = PinPromptScreenRoute
    ) {
        composable<PinPromptScreenRoute> {
            PinPromptScreenRoot(
                onSuccessClick = onVerificationSuccess,
                onLogout = onLogout
            )
        }
    }
}