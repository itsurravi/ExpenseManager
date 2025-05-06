package com.ravikantsharma.settings.presentation.home

sealed interface SettingsHomeEvent {
    data object NavigateToPreferencesScreen : SettingsHomeEvent
    data object NavigateToSecurityScreen : SettingsHomeEvent
    data object Logout : SettingsHomeEvent
    data object NavigateBack : SettingsHomeEvent
}