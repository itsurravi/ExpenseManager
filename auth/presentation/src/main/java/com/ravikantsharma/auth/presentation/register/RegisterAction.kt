package com.ravikantsharma.auth.presentation.register

sealed interface RegisterAction {
    data object OnAlreadyHaveAnAccountClicked : RegisterAction
    data class OnUserNameChanged(val username: String) : RegisterAction
    data object OnNextClicked : RegisterAction
}