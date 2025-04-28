package com.ravikantsharma.auth.presentation.user_preference

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ravikantsharma.auth.domain.usecase.OnboardingPreferenceUseCases
import com.ravikantsharma.auth.presentation.navigation.model.PreferencesScreenData
import com.ravikantsharma.ui.getRouteData
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OnboardingPreferencesViewModel(
    savedStateHandle: SavedStateHandle,
    private val onboardingPreferenceUseCases: OnboardingPreferenceUseCases
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
                    eventChannel.send(OnboardingPreferencesEvent.NavigateToRegisterScreen)
                }

                OnboardingPreferencesAction.OnStartClicked -> {
                    eventChannel.send(OnboardingPreferencesEvent.NavigateToDashboardScreen)
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
        return onboardingPreferenceUseCases.formatExampleUseCase(
            expenseFormat = state.expenseFormat,
            decimalSeparator = state.decimalSeparator,
            thousandsSeparator = state.thousandsSeparator,
            currency = state.currency
        )
    }
}