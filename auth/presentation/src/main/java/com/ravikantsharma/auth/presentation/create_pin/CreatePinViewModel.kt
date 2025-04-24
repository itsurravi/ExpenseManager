package com.ravikantsharma.auth.presentation.create_pin

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreatePinViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreatePinState())
    val uiState = _uiState.asStateFlow()

    private val _eventChannel = Channel<CreatePinEvent>()
    val events = _eventChannel.receiveAsFlow()

    private val enteredPin = savedStateHandle.get<String>("createdPin")

    fun onAction(action: CreatePinAction) {
        viewModelScope.launch {
            when (action) {
                is CreatePinAction.OnPinUpdate -> Unit
                CreatePinAction.OnBackPressed -> {
                    _eventChannel.send(CreatePinEvent.NavigateToRegisterScreen)
                }

                CreatePinAction.OnDeletePressed -> {
                    val pin = _uiState.value.pin
                    _uiState.update {
                        it.copy(
                            pin = pin.dropLast(1)
                        )
                    }
                }

                is CreatePinAction.OnNumberPressed -> {
                    val pin = _uiState.value.pin
                    if (pin.length < 5) {
                        _uiState.update {
                            it.copy(
                                pin = pin + action.number
                            )
                        }
                    }
                    val updatedPin = _uiState.value.pin
                    if (updatedPin.length == 5) {
                        if (enteredPin == null) {
                            _eventChannel.send(CreatePinEvent.NavigateToConfirmPinScreen(updatedPin))
                        } else if (updatedPin.equals(enteredPin, true)) {
                            _eventChannel.send(CreatePinEvent.NavigateToPreferencesScreen)
                        } else {
                            _eventChannel.send(CreatePinEvent.PinsDoNotMatch)
                        }
                    }
                }
            }
        }

    }
}