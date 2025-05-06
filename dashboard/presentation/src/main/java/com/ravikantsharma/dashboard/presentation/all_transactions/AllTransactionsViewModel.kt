package com.ravikantsharma.dashboard.presentation.all_transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ravikantsharma.core.domain.preference.usecase.SettingsPreferenceUseCase
import com.ravikantsharma.core.domain.transactions.model.Transaction
import com.ravikantsharma.core.domain.transactions.usecases.TransactionUseCases
import com.ravikantsharma.core.domain.utils.Result
import com.ravikantsharma.dashboard.presentation.dashboard.TransactionGroupUIItem
import com.ravikantsharma.dashboard.presentation.mapper.toUIItem
import com.ravikantsharma.session_management.domain.usecases.SessionUseCases
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AllTransactionsViewModel(
    sessionUseCases: SessionUseCases,
    private val sessionPreferenceUseCase: SettingsPreferenceUseCase,
    private val transactionUseCases: TransactionUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(AllTransactionsViewState())
    val uiState = _uiState.asStateFlow()

    private val eventChannel = Channel<AllTransactionsEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: AllTransactionsAction) {
        when (action) {
            is AllTransactionsAction.OnCardClicked -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        transactions = currentState.transactions?.map { group ->
                            group.copy(
                                transactions = group.transactions.map { transaction ->
                                    if (transaction.transactionId == action.transactionId) {
                                        transaction.copy(isCollapsed = !transaction.isCollapsed)
                                    } else transaction
                                }
                            )
                        }
                    )
                }
            }

            AllTransactionsAction.OnClickBackButton -> {
                viewModelScope.launch {
                    eventChannel.send(AllTransactionsEvent.NavigateBack)
                }
            }

            is AllTransactionsAction.UpdateCreateBottomSheet -> {
                _uiState.update {
                    it.copy(
                        showCreateTransactionsSheet = action.showSheet
                    )
                }
            }

            is AllTransactionsAction.UpdateExportBottomSheet -> {
                _uiState.update {
                    it.copy(
                        showExportTransactionsSheet = action.showSheet
                    )
                }
            }
        }
    }

    init {
        combine(
            sessionUseCases.getSessionDataUseCase().flatMapLatest { sessionData ->
                sessionPreferenceUseCase.getPreferencesUseCase(sessionData.userId)
            },
            sessionUseCases.getSessionDataUseCase().flatMapLatest { sessionData ->
                transactionUseCases.getTransactionsForUserUseCase(sessionData.userId)
            }
        ) { preferences, transactions ->
            Pair(preferences, transactions)
        }.onEach { (preferencesResult, transactionsResult) ->
            if (
                preferencesResult is Result.Success &&
                transactionsResult is Result.Success
            ) {
                _uiState.update { currentState ->
                    currentState.copy(
                        preference = preferencesResult.data,
                        transactions = groupTransactionsByDate(transactionsResult.data)
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun groupTransactionsByDate(transactions: List<Transaction>): List<TransactionGroupUIItem> {
        return transactionUseCases.getTransactionsGroupedByDateUseCase(transactions).map {
            it.toUIItem()
        }
    }
}