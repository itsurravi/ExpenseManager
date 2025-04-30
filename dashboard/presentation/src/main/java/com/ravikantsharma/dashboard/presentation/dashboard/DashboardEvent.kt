package com.ravikantsharma.dashboard.presentation.dashboard

sealed interface DashboardEvent {
    data object NavigateTest : DashboardEvent
}