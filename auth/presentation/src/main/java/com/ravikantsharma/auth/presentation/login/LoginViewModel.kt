package com.ravikantsharma.auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoginViewState())
    val uiState = _uiState.asStateFlow()

    private val eventChannel = Channel<LoginEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(loginAction: LoginAction) {
        viewModelScope.launch {
            when (loginAction) {
                LoginAction.OnLoginClick -> {
                    val username = _uiState.value.username.text.trim()

                    if (!isValidUsername(username)) {
                        eventChannel.send(LoginEvent.IncorrectCredentials)
                    } else {
                        // Proceed with login logic
                    }
                }

                LoginAction.OnRegisterClick -> {

                }
            }
        }
    }

    private fun isValidUsername(username: CharSequence): Boolean {
        return username.length in MIN_USERNAME_LENGTH..MAX_USERNAME_LENGTH &&
                username.all { it.isLetterOrDigit() }
    }

    companion object {
        private const val MIN_USERNAME_LENGTH = 3
        private const val MAX_USERNAME_LENGTH = 14
        private const val MIN_PIN_LENGTH = 5
    }
}