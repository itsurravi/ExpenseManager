package com.ravikantsharma.auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ravikantsharma.auth.domain.usecase.AuthUseCases
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(private val authUseCases: AuthUseCases) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginViewState())
    val uiState = _uiState.asStateFlow()

    private val eventChannel = Channel<LoginEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: LoginAction) {
        viewModelScope.launch {
            when (action) {
                LoginAction.OnLoginClick -> {
                    val username = _uiState.value.username.trim()
                    val pin = _uiState.value.pin.trim()

                    if (!authUseCases.isUsernameValidUseCase(username) || pin.length < MIN_PIN_LENGTH) {
                        eventChannel.send(LoginEvent.IncorrectCredentials)
                    } else {
                        // Proceed with login logic
                    }
                }

                LoginAction.OnRegisterClick -> {
                    eventChannel.send(LoginEvent.NavigateToRegisterScreen)
                }

                is LoginAction.OnPinChange -> {
                    _uiState.update {
                        it.copy(pin = action.pin)
                    }
                }

                is LoginAction.OnUsernameUpdate -> {
                    _uiState.update {
                        it.copy(username = action.username)
                    }
                }
            }
        }
    }

    companion object {
        private const val MIN_PIN_LENGTH = 5
    }
}