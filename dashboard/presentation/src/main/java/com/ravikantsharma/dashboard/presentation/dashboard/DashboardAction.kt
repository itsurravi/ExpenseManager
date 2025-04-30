package com.ravikantsharma.dashboard.presentation.dashboard

sealed interface DashboardAction {
    data object NavigationClick : DashboardAction
}