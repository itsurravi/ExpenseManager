package com.ravikantsharma.dashboard.presentation.dashboard

data class DashboardState(
    val isSessionExpired: Boolean = false,
    val showCreateTransactionSheet: Boolean = false
)