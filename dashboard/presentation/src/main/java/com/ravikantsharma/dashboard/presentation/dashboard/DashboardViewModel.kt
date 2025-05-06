package com.ravikantsharma.dashboard.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ravikantsharma.core.domain.formatting.NumberFormatter
import com.ravikantsharma.core.domain.model.TransactionCategory
import com.ravikantsharma.core.domain.preference.model.UserPreferences
import com.ravikantsharma.core.domain.preference.usecase.SettingsPreferenceUseCase
import com.ravikantsharma.core.domain.transactions.model.Transaction
import com.ravikantsharma.core.domain.transactions.usecases.TransactionUseCases
import com.ravikantsharma.core.domain.utils.CombinedResult
import com.ravikantsharma.core.domain.utils.DataError
import com.ravikantsharma.core.domain.utils.Result
import com.ravikantsharma.core.domain.utils.toShortDateString
import com.ravikantsharma.dashboard.presentation.mapper.toTransactionCategoryUI
import com.ravikantsharma.dashboard.presentation.mapper.toUIItem
import com.ravikantsharma.session_management.domain.model.SessionData
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
import java.math.BigDecimal

class DashboardViewModel(
    private val sessionUseCases: SessionUseCases,
    private val sessionPreferenceUseCase: SettingsPreferenceUseCase,
    private val transactionUseCases: TransactionUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardViewState())
    val uiState = _uiState.asStateFlow()

    private val eventChannel = Channel<DashboardEvent>()
    val events = eventChannel.receiveAsFlow()

    private var preference: UserPreferences? = null

    init {
        fetchTransactions()
    }

    private fun fetchTransactions() {
        combine(
            sessionUseCases.getSessionDataUseCase(),
            sessionUseCases.getSessionDataUseCase().flatMapLatest { sessionData ->
                sessionPreferenceUseCase.getPreferencesUseCase(sessionData.userId)
            },
            sessionUseCases.getSessionDataUseCase().flatMapLatest { sessionData ->
                transactionUseCases.getTransactionsForUserUseCase(sessionData.userId, limit = FETCH_TRANSACTIONS_LIMIT)
            },
            sessionUseCases.getSessionDataUseCase().flatMapLatest { sessionData ->
                transactionUseCases.getAccountBalanceUseCase(sessionData.userId)
            },
            sessionUseCases.getSessionDataUseCase().flatMapLatest { sessionData ->
                transactionUseCases.getMostPopularExpenseCategoryUseCase(sessionData.userId)
            },
            sessionUseCases.getSessionDataUseCase().flatMapLatest { sessionData ->
                transactionUseCases.getLargestTransactionUseCase(sessionData.userId)
            },
            sessionUseCases.getSessionDataUseCase().flatMapLatest { sessionData ->
                transactionUseCases.getPreviousWeekTotalUseCase(sessionData.userId)
            }
        ) { array: Array<Any?> ->
            CombinedResult(
                array[0] as SessionData,
                array[1] as Result<UserPreferences, DataError>,
                array[2] as Result<List<Transaction>, DataError>,
                array[3] as Result<BigDecimal, DataError>,
                array[4] as Result<TransactionCategory?, DataError>,
                array[5] as Result<Transaction?, DataError>,
                array[6] as Result<BigDecimal, DataError>,
            )
        }.onEach { (
                       sessionData: SessionData,
                       preferenceResult: Result<UserPreferences, DataError>,
                       transactionResult: Result<List<Transaction>, DataError>,
                       balanceResult: Result<BigDecimal, DataError>,
                       popularCategoryResult: Result<TransactionCategory?, DataError>,
                       largestTransactionResult: Result<Transaction?, DataError>,
                       previousWeekTotalResult: Result<BigDecimal, DataError>
                   ) ->
            if (preferenceResult is Result.Success &&
                transactionResult is Result.Success &&
                balanceResult is Result.Success &&
                popularCategoryResult is Result.Success &&
                largestTransactionResult is Result.Success &&
                previousWeekTotalResult is Result.Success
            ) {
                preference = preferenceResult.data
                _uiState.update { currentState ->
                    currentState.copy(
                        preference = preferenceResult.data,
                        username = sessionData.userName,
                        accountBalance = NumberFormatter.formatAmount(
                            balanceResult.data,
                            preference
                        ),
                        mostPopularCategory = popularCategoryResult.data?.toTransactionCategoryUI(),
                        largestTransaction = largestTransactionResult.data.toLargestTransactionItem(),
                        previousWeekTotal = NumberFormatter.formatAmount(
                            previousWeekTotalResult.data,
                            preference
                        ),
                        transactions = groupTransactionsByDate(transactionResult.data)
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    fun onAction(action: DashboardAction) {
        when (action) {
            DashboardAction.NavigationClick -> Unit
            DashboardAction.OnShowAllTransactionsClicked -> {
                viewModelScope.launch {
                    eventChannel.send(DashboardEvent.NavigateToAllTransactions)
                }
            }

            DashboardAction.OnSettingsClicked -> {
                viewModelScope.launch {
                    eventChannel.send(DashboardEvent.NavigateToSettings)
                }
            }

            is DashboardAction.UpdatedBottomSheet -> {
                _uiState.update {
                    it.copy(
                        showCreateTransactionSheet = action.showSheet
                    )
                }
            }
            is DashboardAction.UpdateExportBottomSheet -> {
                _uiState.update {
                    it.copy(
                        showExportTransactionSheet = action.showSheet
                    )
                }
            }

            is DashboardAction.OnCardClicked -> {
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
        }
    }

    private fun groupTransactionsByDate(transactions: List<Transaction>): List<TransactionGroupUIItem> {
        return transactionUseCases.getTransactionsGroupedByDateUseCase(transactions).map {
            it.toUIItem()
        }
    }

    private fun Transaction?.toLargestTransactionItem(): LargestTransaction? {
        return this?.let {
            LargestTransaction(
                name = this.transactionName,
                amount = NumberFormatter.formatAmount(this.amount, preference),
                date = this.transactionDate.toShortDateString()
            )
        }
    }

    companion object {
        private const val FETCH_TRANSACTIONS_LIMIT = 20
    }
}