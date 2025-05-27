package com.ravikantsharma.expensemanager

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ravikantsharma.core.domain.auth.usecases.UserInfoUseCases
import com.ravikantsharma.core.domain.transactions.usecases.TransactionUseCases
import com.ravikantsharma.session_management.domain.usecases.SessionUseCases
import com.ravikantsharma.ui.AppNavRoute
import com.ravikantsharma.ui.NavigationRequestHandler
import com.ravikantsharma.ui.navigation.DashboardScreenRoute
import com.ravikantsharma.widget.presentation.create_transaction.CreateTransactionWidget
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    savedStateHandle: SavedStateHandle,
    private val sessionUseCases: SessionUseCases,
    private val transactionUseCases: TransactionUseCases,
    private val userInfoUseCases: UserInfoUseCases
) : ViewModel(), NavigationRequestHandler {

    private val _uiState = MutableStateFlow(MainState())
    val uiState: StateFlow<MainState> = _uiState.asStateFlow()

    private val isLaunchedFromWidget: Boolean =
        savedStateHandle.get<String>(CreateTransactionWidget.INTENT_SOURCE_KEY) == CreateTransactionWidget.SOURCE

    init {
        initializeSession()
    }

    private fun initializeSession() {
        viewModelScope.launch {
            transactionUseCases.processRecurringTransactionsUseCase()
            val isUserPresent = isUserIdPresent()
            val isSessionExpired = sessionUseCases.isSessionExpiredUseCase().first()
            val authNavigationDestination = getAuthNavigationDestination(
                isUserPresent = isUserPresent,
                isSessionExpired = isSessionExpired,
                isLaunchedFromWidget = isLaunchedFromWidget
            )

            _uiState.update {
                it.copy(
                    isSessionExpired = isSessionExpired,
                    isUserLoggedIn = isUserPresent,
                    isCheckingAuth = false,
                    authNavigationDestination = authNavigationDestination,
                    pendingRoute = if (authNavigationDestination == AuthNavigationDestination.PinScreen) {
                        AppNavRoute(
                            pendingRoute = DashboardScreenRoute(
                                isLaunchedFromWidget = isLaunchedFromWidget
                            )
                        )
                    } else null
                )
            }
        }
    }

    fun onAppResumed() {
        viewModelScope.launch {
            if (!isUserIdPresent()) {
                return@launch
            }
            val isSessionExpired = sessionUseCases.isSessionExpiredUseCase().first()
            if (isSessionExpired) {
                _uiState.update {
                    it.copy(
                        showPinPrompt = true,
                        pendingRoute = null
                    )
                }
            }
        }
    }

    fun onPinVerified() {
        _uiState.update {
            it.copy(
                showPinPrompt = false,
                isSessionExpired = false,
                isUserLoggedIn = true,
                pendingRoute = it.pendingRoute
                    ?: AppNavRoute(
                        pendingRoute = DashboardScreenRoute(
                            isLaunchedFromWidget = isLaunchedFromWidget
                        )
                    )
            )
        }
    }

    private suspend fun getAuthNavigationDestination(
        isUserPresent: Boolean,
        isSessionExpired: Boolean,
        isLaunchedFromWidget: Boolean
    ): AuthNavigationDestination {
        return when {
            isUserPresent && !isSessionExpired -> {
                AuthNavigationDestination.DashboardScreen(isLaunchedFromWidget)
            }

            isUserPresent && isSessionExpired -> {
                AuthNavigationDestination.PinScreen
            }

            userInfoUseCases.areUsersPresentUseCase() -> {
                AuthNavigationDestination.AuthScreen(shouldNavigateToLogin = true)
            }

            else -> AuthNavigationDestination.AuthScreen()
        }
    }

    override fun navigateWithAuthCheck(appNavRoute: AppNavRoute) {
        viewModelScope.launch {
            val expiredNow = sessionUseCases.isSessionExpiredUseCase().first()
            _uiState.update {
                it.copy(
                    pendingRoute = appNavRoute,
                    showPinPrompt = expiredNow,
                    isUserLoggedIn = true
                )
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