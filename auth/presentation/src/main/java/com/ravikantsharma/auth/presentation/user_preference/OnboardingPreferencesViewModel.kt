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
import com.ravikantsharma.core.domain.preference.usecase.PreferenceUseCase
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
    private val preferenceUseCase: PreferenceUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingPreferencesViewState())
    val uiState = _uiState.asStateFlow()

    private val eventChannel = Channel<OnboardingPreferencesEvent>()
    val events = eventChannel.receiveAsFlow()

    private val screenData = savedStateHandle.getRouteData<PreferencesScreenData>("screenData")

    fun onAction(action: OnboardingPreferencesAction) {
        when (action) {
            OnboardingPreferencesAction.OnBackClicked -> emitEvent(OnboardingPreferencesEvent.OnBackClicked)

            is OnboardingPreferencesAction.OnExpenseFormatUpdate -> updateUiState {
                it.copy(expenseFormat = action.format)
            }

            is OnboardingPreferencesAction.OnDecimalSeparatorUpdate -> updateUiState {
                it.copy(decimalSeparator = action.format)
            }

            is OnboardingPreferencesAction.OnThousandsSeparatorUpdate -> updateUiState {
                it.copy(thousandsSeparator = action.format)
            }

            is OnboardingPreferencesAction.OnCurrencyUpdate -> updateUiState {
                it.copy(currency = action.currency)
            }

            OnboardingPreferencesAction.OnStartClicked -> handleStartClicked()
        }
    }

    private fun handleStartClicked() {
        viewModelScope.launch {
            val userInfo = UserInfo(
                username = screenData?.username.orEmpty(),
                pin = screenData?.pin.orEmpty()
            )

            val userPreferences = UserPreferences(
                userId = -1L,
                expenseFormat = _uiState.value.expenseFormat,
                currency = _uiState.value.currency,
                decimalSeparator = _uiState.value.decimalSeparator,
                thousandsSeparator = _uiState.value.thousandsSeparator,
                isBiometricEnabled = false,
                sessionDuration = SessionDuration.ONE_MIN,
                lockOutDuration = LockoutDuration.THIRTY_SECONDS,
                allowedPinAttempts = PinAttempts.THREE
            )

            val result = onboardingPreferenceUseCases.registerUserAndSavePreferencesUseCase(
                userInfo,
                userPreferences
            )

            when (result) {
                is Result.Success -> emitEvent(OnboardingPreferencesEvent.NavigateToDashboardScreen)
                is Result.Error -> handleError(result.error)
            }
        }
    }

    private fun handleError(error: DataError) {
        val event = when (error) {
            DataError.Local.DUPLICATE_USER_ERROR -> OnboardingPreferencesEvent.Error.DuplicateEntry
            DataError.Local.PREFERENCE_FETCH_ERROR -> OnboardingPreferencesEvent.Error.Generic
            else -> OnboardingPreferencesEvent.Error.Generic
        }
        emitEvent(event)
    }

    private fun updateUiState(updateBlock: (OnboardingPreferencesViewState) -> OnboardingPreferencesViewState) {
        _uiState.update { currentState ->
            val newState = updateBlock(currentState)
            val isValidFormat = preferenceUseCase.isValidPreferenceUseCase(
                decimalSeparator = newState.decimalSeparator,
                thousandsSeparator = newState.thousandsSeparator
            )

            newState.copy(
                enableStartTracking = isValidFormat,
                exampleFormat = if (isValidFormat) formatExample(newState) else currentState.exampleFormat
            )
        }
    }

    private fun formatExample(state: OnboardingPreferencesViewState): String {
        return NumberFormatter.formatAmount(
            amount = state.amount,
            expenseFormat = state.expenseFormat,
            decimalSeparator = state.decimalSeparator,
            thousandsSeparator = state.thousandsSeparator,
            currency = state.currency
        )
    }

    private fun emitEvent(event: OnboardingPreferencesEvent) {
        viewModelScope.launch { eventChannel.send(event) }
    }
}