package com.ravikantsharma.expensemanager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ravikantsharma.session_management.domain.usecases.SessionUseCases
import com.ravikantsharma.ui.AppNavRoute
import com.ravikantsharma.ui.NavigationRequestHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val sessionUseCases: SessionUseCases
) : ViewModel(), NavigationRequestHandler {

    private val _uiState = MutableStateFlow(MainState())
    val uiState: StateFlow<MainState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val userPresent = isUserIdPresent()
            sessionUseCases.isSessionExpiredUseCase().collect { expired ->
                _uiState.update {
                    it.copy(
                        isSessionExpired = expired,
                        isUserLoggedIn = userPresent
                    )
                }
            }
        }
    }

    override fun navigateWithAuthCheck(appNavRoute: AppNavRoute) {
        viewModelScope.launch {
            val expiredNow = sessionUseCases.isSessionExpiredUseCase().first()
            if (expiredNow) {
                _uiState.update {
                    it.copy(
                        pendingRoute = appNavRoute,
                        showPinPrompt = true
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        pendingRoute = appNavRoute,
                        showPinPrompt = false
                    )
                }
            }
        }
    }

    override fun runWithAuthCheck(action: () -> Unit) {
        viewModelScope.launch {
            val expiredNow = sessionUseCases.isSessionExpiredUseCase().first()
            if (expiredNow) {
                _uiState.update {
                    it.copy(
                        pendingActionAfterAuth = action,
                        showPinPrompt = true
                    )
                }
            } else {
                action()
            }
        }
    }

    fun clearPendingActionAfterAuth() {
        _uiState.update { it.copy(pendingActionAfterAuth = null) }
    }

    fun mainViewModelClearPendingRoute() {
        _uiState.update { it.copy(pendingRoute = null) }
    }

    fun startSession() {
        viewModelScope.launch {
            sessionUseCases.resetSessionExpiryUseCase()
            _uiState.update { it.copy(isSessionExpired = false, showPinPrompt = false) }
        }
    }

    fun setSessionToExpired() {
        viewModelScope.launch {
            sessionUseCases.setSessionExpiredUseCase()
        }
    }

    private suspend fun isUserIdPresent(): Boolean {
        return sessionUseCases.getSessionDataUseCase()
            .firstOrNull()?.userId?.let { it > 0L } == true
    }
}