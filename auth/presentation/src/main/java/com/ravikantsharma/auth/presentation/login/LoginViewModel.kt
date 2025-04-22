package com.ravikantsharma.auth.presentation.login

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel: ViewModel() {

    private val _uiState = MutableStateFlow(LoginViewState())
    val uiState = _uiState.asStateFlow()

    fun onAction(loginAction: LoginAction) {
        when(loginAction) {
            LoginAction.OnLoginClick -> {

            }
            LoginAction.OnRegisterClick -> {

            }
        }
    }
}