package com.ravikantsharma.dashboard.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ravikantsharma.session_management.domain.usecases.SessionUseCases
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val sessionUseCases: SessionUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardState(isSessionExpired = false))
    val uiState = _uiState.asStateFlow()

    private val eventChannel = Channel<DashboardEvent>()
    val events = eventChannel.receiveAsFlow()

    fun triggerPin() {
        viewModelScope.launch {
            sessionUseCases.checkSessionExpiryUseCase()
        }
    }
}