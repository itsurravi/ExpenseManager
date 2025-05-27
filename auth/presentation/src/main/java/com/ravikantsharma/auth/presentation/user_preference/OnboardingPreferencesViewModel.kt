package com.ravikantsharma.auth.presentation.user_preference

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ravikantsharma.auth.domain.usecase.OnboardingPreferenceUseCases
import com.ravikantsharma.auth.domain.usecase.RegisterUseCases
import com.ravikantsharma.core.domain.auth.model.UserInfo
import com.ravikantsharma.core.domain.formatting.NumberFormatter
import com.ravikantsharma.core.domain.model.LockoutDuration
import com.ravikantsharma.core.domain.model.PinAttempts
import com.ravikantsharma.core.domain.model.SessionDuration
import com.ravikantsharma.core.domain.preference.model.UserPreferences
import com.ravikantsharma.core.domain.preference.usecase.SettingsPreferenceUseCase
import com.ravikantsharma.core.domain.utils.DataError
import com.ravikantsharma.core.domain.utils.Result
import com.ravikantsharma.session_management.domain.model.SessionData
import com.ravikantsharma.session_management.domain.usecases.SessionUseCases
import com.ravikantsharma.ui.getRouteData
import com.ravikantsharma.ui.navigation.PreferencesScreenData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OnboardingPreferencesViewModel(
    savedStateHandle: SavedStateHandle,
    private val onboardingPreferenceUseCases: OnboardingPreferenceUseCases,
    private val registerUseCases: RegisterUseCases,
    private val sessionUseCase: SessionUseCases,
    private val settingsPreferenceUseCase: SettingsPreferenceUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingPreferencesViewState())
    val uiState = _uiState.asStateFlow()

    private val eventChannel = Channel<OnboardingPreferencesEvent>()
    val events = eventChannel.receiveAsFlow()

    private val screenData = savedStateHandle.getRouteData<PreferencesScreenData>("screenData")

    fun onAction(action: OnboardingPreferencesAction) {
        viewModelScope.launch {
            when (action) {
                OnboardingPreferencesAction.OnBackClicked -> {
                    eventChannel.send(OnboardingPreferencesEvent.OnBackClick)
                }

                OnboardingPreferencesAction.OnStartClicked -> {
                    handleOnStartClicked()

                }

                is OnboardingPreferencesAction.OnCurrencyUpdate -> {
                    updateUiState {
                        it.copy(currency = action.currency)
                    }
                }

                is OnboardingPreferencesAction.OnDecimalSeparatorUpdate -> {
                    updateUiState {
                        it.copy(decimalSeparator = action.format)
                    }
                }

                is OnboardingPreferencesAction.OnExpenseFormatUpdate -> {
                    updateUiState {
                        it.copy(expenseFormat = action.format)
                    }
                }

                is OnboardingPreferencesAction.OnThousandsSeparatorUpdate -> {
                    updateUiState {
                        it.copy(thousandsSeparator = action.format)
                    }
                }
            }
        }
    }

    private suspend fun handleOnStartClicked() {
        val userInfo = UserInfo(
            username = screenData?.username.orEmpty(),
            pin = screenData?.pin.orEmpty()
        )

        val userIdResult = withContext(Dispatchers.IO) {
            registerUseCases.registerUserUseCase(userInfo)
        }

        var userId = -1L

        when (userIdResult) {
            is Result.Error -> {
                handleRegistrationError(userIdResult.error)
                return
            }

            is Result.Success -> {
                userId = userIdResult.data
            }
        }

        val userPreferences = UserPreferences(
            userId = userIdResult.data,
            expenseFormat = uiState.value.expenseFormat,
            currency = uiState.value.currency,
            decimalSeparator = uiState.value.decimalSeparator,
            thousandsSeparator = uiState.value.thousandsSeparator,
            isBiometricEnabled = false,
            sessionDuration = SessionDuration.ONE_MIN,
            lockOutDuration = LockoutDuration.THIRTY_SECONDS,
            allowedPinAttempts = PinAttempts.THREE
        )

        val preferencesResult = withContext(Dispatchers.IO) {
            settingsPreferenceUseCase.setPreferencesUseCase(userPreferences)
        }

        when (preferencesResult) {
            is Result.Error -> {
                handlePreferenceError(preferencesResult.error)
                return
            }

            is Result.Success -> Unit
        }

        val sessionData = SessionData(
            userId = userId,
            userName = screenData?.username.orEmpty(),
            sessionExpiryTime = 0
        )
        sessionUseCase.saveSessionUseCase(sessionData)
        eventChannel.send(OnboardingPreferencesEvent.NavigateToDashboardScreen)
    }

    private suspend fun handleRegistrationError(error: DataError) {
        val event = when (error) {
            DataError.Local.DUPLICATE_USER_ERROR -> OnboardingPreferencesEvent.Error.DuplicateEntry
            else -> OnboardingPreferencesEvent.Error.Generic
        }
        eventChannel.send(event)
    }

    private suspend fun handlePreferenceError(error: DataError) {
        val event = when (error) {
            DataError.Local.PREFERENCE_FETCH_ERROR -> OnboardingPreferencesEvent.Error.Generic
            else -> OnboardingPreferencesEvent.Error.Generic
        }
        eventChannel.send(event)
    }

    /**
     * Helper function to update UI state while ensuring that:
     * - `exampleFormat` is updated when needed.
     * - `enableStartTracking` is updated when needed.
     */
    private fun updateUiState(updateBlock: (OnboardingPreferencesViewState) -> OnboardingPreferencesViewState) {
        _uiState.update { currentState ->
            val newState = updateBlock(currentState)
            val isValidFormat = onboardingPreferenceUseCases.validateSelectedPreferences(
                decimalSeparator = newState.decimalSeparator,
                thousandsSeparator = newState.thousandsSeparator
            )

            newState.copy(
                enableStartTracking = isValidFormat,
                exampleFormat = if (isValidFormat) formatExample(newState) else currentState.exampleFormat
            )
        }
    }

    /**
     * Formats the example number based on current preferences.
     */
    private fun formatExample(state: OnboardingPreferencesViewState): String {
        return NumberFormatter.formatAmount(
            amount = state.amount,
            expenseFormat = state.expenseFormat,
            decimalSeparator = state.decimalSeparator,
            thousandsSeparator = state.thousandsSeparator,
            currency = state.currency
        )
    }
}