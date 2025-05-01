package com.ravikantsharma.expensemanager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ravikantsharma.session_management.domain.usecases.SessionUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val sessionUseCases: SessionUseCases
) : ViewModel() {

    val state = MutableStateFlow(MainState(isSessionExpired = false))
        private set

    init {
        viewModelScope.launch {
            sessionUseCases.resetSessionExpiryUseCase()
        }
        sessionUseCases.isSessionExpiredUseCase().onEach { isExpired ->
            state.update {
                it.copy(
                    isSessionExpired = isExpired
                )
            }
        }.launchIn(viewModelScope)
    }

    fun startSession() {
        viewModelScope.launch {
            sessionUseCases.saveSessionUseCase()
            state.update {
                it.copy(
                    isSessionExpired = false
                )
            }
        }
    }

    fun updateExpiry(isExpired: Boolean) {
        state.update {
            it.copy(
                isSessionExpired = isExpired
            )
        }
    }
}