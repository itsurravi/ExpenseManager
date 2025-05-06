package com.ravikantsharma.settings.presentation.home

sealed interface SettingsHomeAction {
    data object OnPreferencesClick : SettingsHomeAction
    data object OnSecurityClick : SettingsHomeAction
    data object OnLogoutClick : SettingsHomeAction
    data object OnBackClick: SettingsHomeAction
}