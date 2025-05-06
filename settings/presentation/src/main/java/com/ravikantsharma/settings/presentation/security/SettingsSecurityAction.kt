package com.ravikantsharma.settings.presentation.security

import com.ravikantsharma.core.domain.model.BiometricPromptStatus
import com.ravikantsharma.core.domain.model.LockoutDuration
import com.ravikantsharma.core.domain.model.SessionDuration

sealed interface SettingsSecurityAction {
    data class OnBiometricSettingUpdated(val setting: BiometricPromptStatus) :
        SettingsSecurityAction

    data class OnSessionExpiryUpdated(val setting: SessionDuration) : SettingsSecurityAction
    data class OnLockOutDurationUpdated(val setting: LockoutDuration) : SettingsSecurityAction
    data object OnSaveClicked : SettingsSecurityAction
    data object OnBackClicked : SettingsSecurityAction
}