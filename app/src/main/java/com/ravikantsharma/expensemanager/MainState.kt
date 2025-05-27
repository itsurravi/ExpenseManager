package com.ravikantsharma.expensemanager

import com.ravikantsharma.ui.AppNavRoute

data class MainState(
    val isCheckingAuth: Boolean = true,
    val authNavigationDestination: AuthNavigationDestination = AuthNavigationDestination.None,
    val isSessionExpired: Boolean = false,
    val isUserLoggedIn: Boolean = false,
    val showPinPrompt: Boolean = false,
    val pendingRoute: AppNavRoute? = null,
    val pendingActionAfterAuth: (() -> Unit)? = null
)

sealed class AuthNavigationDestination {
    data object None : AuthNavigationDestination()
    data object PinScreen : AuthNavigationDestination()
    data class AuthScreen(val shouldNavigateToLogin: Boolean = false): AuthNavigationDestination()
    data class DashboardScreen(val isLaunchedFromWidget: Boolean) : AuthNavigationDestination()
}