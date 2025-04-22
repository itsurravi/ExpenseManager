package com.ravikantsharma.auth.presentation.register

data class RegisterViewState(
    val username: String = "",
    val isNextEnabled: Boolean = false
)