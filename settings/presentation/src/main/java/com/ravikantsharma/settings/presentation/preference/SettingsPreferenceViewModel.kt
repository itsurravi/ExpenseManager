package com.ravikantsharma.settings.presentation.preference

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ravikantsharma.core.domain.formatting.NumberFormatter
import com.ravikantsharma.core.domain.model.LockoutDuration
import com.ravikantsharma.core.domain.model.PinAttempts
import com.ravikantsharma.core.domain.model.SessionDuration
import com.ravikantsharma.core.domain.preference.model.UserPreferences
import com.ravikantsharma.core.domain.preference.usecase.SettingsPreferenceUseCase
import com.ravikantsharma.core.domain.utils.Result
import com.ravikantsharma.session_management.domain.usecases.SessionUseCases
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsPreferenceViewModel(
    private val sessionUseCase: SessionUseCases,
    private val settingsPreferenceUseCase: SettingsPreferenceUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsPreferencesViewState())
    val uiState: StateFlow<SettingsPreferencesViewState> = _uiState

    private val eventChannel = Channel<SettingsPreferencesEvent>()
    val events = eventChannel.receiveAsFlow()

    private var userPreferences: UserPreferences? = null

    init {
        fetchUserPreferences()
    }

    private fun fetchUserPreferences() {
        sessionUseCase.getSessionDataUseCase()
            .flatMapLatest { sessionData ->
                settingsPreferenceUseCase.getPreferencesUseCase(sessionData.userId)
            }
            .onEach { result ->
                if (result is Result.Success) {
                    userPreferences = result.data
                    updateUiState {
                        it.copy(
                            userId = result.data.userId,
                            expenseFormat = result.data.expenseFormat,
                            currency = result.data.currency,
                            decimalSeparator = result.data.decimalSeparator,
                            thousandsSeparator = result.data.thousandsSeparator,
                        )
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: SettingsPreferencesAction) {
        viewModelScope.launch {
            when (action) {
                SettingsPreferencesAction.OnBackClicked -> {
                    eventChannel.send(SettingsPreferencesEvent.NavigateBack)
                }

                is SettingsPreferencesAction.OnExpenseFormatUpdate -> {
                    updateUiState { it.copy(expenseFormat = action.format) }
                }

                is SettingsPreferencesAction.OnDecimalSeparatorUpdate -> {
                    updateUiState { it.copy(decimalSeparator = action.format) }
                }

                is SettingsPreferencesAction.OnThousandsSeparatorUpdate -> {
                    updateUiState { it.copy(thousandsSeparator = action.format) }
                }

                is SettingsPreferencesAction.OnCurrencyUpdate -> {
                    updateUiState { it.copy(currency = action.currency) }
                }

                SettingsPreferencesAction.OnSaveClicked -> {
                    handleOnSaveClicked()
                }
            }
        }
    }

    private fun handleOnSaveClicked() {
        viewModelScope.launch {
            userPreferences?.let { existingPreference ->

                val userPreferencesUpdated = UserPreferences(
                    userId = _uiState.value.userId,
                    expenseFormat = _uiState.value.expenseFormat,
                    currency = _uiState.value.currency,
                    decimalSeparator = _uiState.value.decimalSeparator,
                    thousandsSeparator = _uiState.value.thousandsSeparator,
                    isBiometricEnabled = existingPreference.isBiometricEnabled,
                    sessionDuration = existingPreference.sessionDuration,
                    lockOutDuration = existingPreference.lockOutDuration,
                    allowedPinAttempts = PinAttempts.THREE
                )

                val preferencesResult = withContext(Dispatchers.IO) {
                    settingsPreferenceUseCase.setPreferencesUseCase(userPreferencesUpdated)
                }
                when (preferencesResult) {
                    is Result.Error -> {
                        return@launch
                    }

                    is Result.Success -> {
                        eventChannel.send(SettingsPreferencesEvent.PreferencesSaved)
                    }
                }
            }
        }
    }


    private fun updateUiState(updateBlock: (SettingsPreferencesViewState) -> SettingsPreferencesViewState) {
        _uiState.update { currentState ->
            val newState = updateBlock(currentState)
            val isValidFormat = settingsPreferenceUseCase.isValidPreference(
                decimalSeparator = newState.decimalSeparator,
                thousandsSeparator = newState.thousandsSeparator
            )

            newState.copy(
                enableSaveButton = isValidFormat,
                exampleFormat = if (isValidFormat) formatExample(newState) else currentState.exampleFormat
            )
        }
    }

    private fun formatExample(state: SettingsPreferencesViewState): String {
        return NumberFormatter.formatAmount(
            amount = state.amount,
            expenseFormat = state.expenseFormat,
            decimalSeparator = state.decimalSeparator,
            thousandsSeparator = state.thousandsSeparator,
            currency = state.currency
        )
    }
}