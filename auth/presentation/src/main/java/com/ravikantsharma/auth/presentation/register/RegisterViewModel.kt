package com.ravikantsharma.auth.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterViewState())
    val uiState = _uiState.asStateFlow()

    private val eventChannel = Channel<RegisterEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: RegisterAction) {
        viewModelScope.launch {
            when (action) {
                is RegisterAction.OnUserNameChanged -> {
                    _uiState.update {
                        it.copy(
                            username = action.username,
                            isNextEnabled = action.username.isNotEmpty()
                        )
                    }
                }

                RegisterAction.OnAlreadyHaveAnAccountClicked -> {
                    eventChannel.send(RegisterEvent.NavigateToRegisterScreen)
                }
            }
        }
    }
}