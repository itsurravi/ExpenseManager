package com.ravikantsharma.auth.presentation.create_pin

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ravikantsharma.auth.presentation.navigation.model.CreatePinScreenData
import com.ravikantsharma.auth.presentation.navigation.model.PreferencesScreenData
import com.ravikantsharma.ui.getRouteData
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

    private var createPinScreenData = savedStateHandle.getRouteData<CreatePinScreenData>("screenData")

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
                    if (pin.length < MAX_PIN_LENGTH) {
                        _uiState.update {
                            it.copy(
                                pin = pin + action.number
                            )
                        }
                    }
                    val updatedPin = _uiState.value.pin
                    if (updatedPin.length == MAX_PIN_LENGTH) {
                        if (createPinScreenData?.pin == null) {
                            _eventChannel.send(
                                CreatePinEvent.NavigateToConfirmPinScreen(
                                    CreatePinScreenData(
                                        username = createPinScreenData?.username ?: "",
                                        pin = updatedPin
                                    )
                                )
                            )
                        } else if (updatedPin.equals(createPinScreenData?.pin, true)) {
                            createPinScreenData?.let {
                                _eventChannel.send(
                                    CreatePinEvent.NavigateToPreferencesScreen(
                                        screenData = PreferencesScreenData(
                                            username = it.username,
                                            pin = it.pin
                                        )
                                    )
                                )
                            }
                            resetState()
                        } else {
                            _eventChannel.send(CreatePinEvent.PinsDoNotMatch)
                        }
                    }
                }
            }
        }
    }

    private fun resetState() {
        _uiState.update {
            it.copy(
                pin = ""
            )
        }
        createPinScreenData = createPinScreenData?.copy(
            pin = ""
        )
    }

    companion object {
        private const val MAX_PIN_LENGTH = 5
    }
}