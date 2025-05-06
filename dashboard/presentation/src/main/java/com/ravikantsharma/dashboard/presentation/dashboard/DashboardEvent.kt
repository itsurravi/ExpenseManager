package com.ravikantsharma.dashboard.presentation.dashboard

sealed interface DashboardEvent {
    data object RequestCreateTransaction : DashboardEvent
    data object NavigateToSettings : DashboardEvent
    data object NavigateToAllTransactions : DashboardEvent
}