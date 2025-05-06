package com.ravikantsharma.expensemanager

import com.ravikantsharma.ui.AppNavRoute

data class MainState(
    val isSessionExpired: Boolean = false,
    val isUserLoggedIn: Boolean = false,
    val showPinPrompt: Boolean = false,
    val pendingRoute: AppNavRoute? = null
)