package com.ravikantsharma.auth.presentation.create_pin

sealed interface CreatePinEvent {
    data object NavigateToConfirmPinScreen : CreatePinEvent
}