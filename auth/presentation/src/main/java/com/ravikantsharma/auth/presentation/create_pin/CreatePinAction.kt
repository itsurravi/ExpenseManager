package com.ravikantsharma.auth.presentation.create_pin

sealed interface CreatePinAction {
    data class OnNumberPressed(val number: Int) : CreatePinAction
    data object OnDeletePressed : CreatePinAction
    data object OnBackPressed : CreatePinAction
}