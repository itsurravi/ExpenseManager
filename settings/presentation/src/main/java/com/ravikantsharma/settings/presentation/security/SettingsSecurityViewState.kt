package com.ravikantsharma.settings.presentation.security

import com.ravikantsharma.core.domain.model.BiometricPromptStatus
import com.ravikantsharma.core.domain.model.LockoutDuration
import com.ravikantsharma.core.domain.model.SessionDuration

data class SettingsSecurityViewState(
    val biometricPromptStatus: BiometricPromptStatus,
    val sessionExpiryDuration: SessionDuration,
    val lockedOutDuration: LockoutDuration,
    val enableSaveButton: Boolean = true
)