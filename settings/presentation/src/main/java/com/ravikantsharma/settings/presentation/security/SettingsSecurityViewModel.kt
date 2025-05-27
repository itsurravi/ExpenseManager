package com.ravikantsharma.settings.presentation.security

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ravikantsharma.core.domain.model.BiometricPromptStatus
import com.ravikantsharma.core.domain.model.LockoutDuration
import com.ravikantsharma.core.domain.model.SessionDuration
import com.ravikantsharma.core.domain.preference.model.UserPreferences
import com.ravikantsharma.core.domain.preference.usecase.SettingsPreferenceUseCase
import com.ravikantsharma.core.domain.utils.Result
import com.ravikantsharma.session_management.domain.usecases.SessionUseCases
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsSecurityViewModel(
    private val sessionUseCases: SessionUseCases,
    private val settingsPreferenceUseCase: SettingsPreferenceUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        SettingsSecurityViewState(
            biometricPromptStatus = BiometricPromptStatus.DISABLE,
            sessionExpiryDuration = SessionDuration.ONE_MIN,
            lockedOutDuration = LockoutDuration.FIFTEEN_SECONDS
        )
    )
    val uiState = _uiState.asStateFlow()

    private val eventChannel = Channel<SettingsSecurityEvent>()
    val events = eventChannel.receiveAsFlow()

    private var userPreferences: UserPreferences? = null

    init {
        fetchUserPreferences()
    }

    private fun fetchUserPreferences() {
        sessionUseCases.getSessionDataUseCase()
            .onEach { sessionData ->
                settingsPreferenceUseCase.getPreferencesUseCase(sessionData.userId)
                    .collect { result ->
                        if (result is Result.Success) {
                            userPreferences = result.data

                            _uiState.update {
                                it.copy(
                                    biometricPromptStatus = BiometricPromptStatus.convertToBiometricPromptStatus(
                                        result.data.isBiometricEnabled
                                    ),
                                    sessionExpiryDuration = result.data.sessionDuration,
                                    lockedOutDuration = result.data.lockOutDuration
                                )
                            }
                        }
                    }
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: SettingsSecurityAction) {
        when (action) {
            SettingsSecurityAction.OnBackClicked -> sendEvent(SettingsSecurityEvent.NavigateBack)
            is SettingsSecurityAction.OnBiometricSettingUpdated -> updateUiState {
                it.copy(biometricPromptStatus = action.setting)
            }

            is SettingsSecurityAction.OnLockOutDurationUpdated -> updateUiState {
                it.copy(lockedOutDuration = action.setting)
            }

            is SettingsSecurityAction.OnSessionExpiryUpdated -> updateUiState {
                it.copy(sessionExpiryDuration = action.setting)
            }

            SettingsSecurityAction.OnSaveClicked -> handleOnSaveClicked()
        }
    }

    private fun handleOnSaveClicked() {
        userPreferences?.let { existingPreference ->
            val userPreferencesUpdated = existingPreference.copy(
                isBiometricEnabled = _uiState.value.biometricPromptStatus.getBooleanValue(),
                sessionDuration = _uiState.value.sessionExpiryDuration,
                lockOutDuration = _uiState.value.lockedOutDuration
            )

            viewModelScope.launch {
                when (settingsPreferenceUseCase.setPreferencesUseCase(userPreferencesUpdated)) {
                    is Result.Success -> {
                        sessionUseCases.resetSessionExpiryUseCase()
                        sendEvent(SettingsSecurityEvent.SecuritySettingsSaved)
                    }
                    is Result.Error -> Unit
                }
            }
        }
    }

    private fun updateUiState(update: (SettingsSecurityViewState) -> SettingsSecurityViewState) {
        _uiState.update(update)
    }

    private fun sendEvent(event: SettingsSecurityEvent) {
        viewModelScope.launch {
            eventChannel.send(event)
        }
    }
}