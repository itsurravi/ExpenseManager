package com.ravikantsharma.auth.presentation.create_pin

sealed interface CreatePinAction {
    data class OnPinUpdate(val pin: String) : CreatePinAction
}