package com.ravikantsharma.dashboard.presentation.create_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ravikantsharma.core.domain.model.TransactionCategory
import com.ravikantsharma.core.domain.preference.usecase.SettingsPreferenceUseCase
import com.ravikantsharma.core.domain.time.TimeProvider
import com.ravikantsharma.core.domain.transactions.model.Transaction
import com.ravikantsharma.core.domain.transactions.usecases.TransactionUseCases
import com.ravikantsharma.core.domain.utils.CalendarUtils
import com.ravikantsharma.core.domain.utils.Result
import com.ravikantsharma.core.presentation.designsystem.model.RecurringTypeUI
import com.ravikantsharma.core.presentation.designsystem.model.TransactionCategoryTypeUI
import com.ravikantsharma.core.presentation.designsystem.model.TransactionTypeUI
import com.ravikantsharma.dashboard.presentation.mapper.toDecimalSeparatorUI
import com.ravikantsharma.dashboard.presentation.mapper.toExpenseFormatUI
import com.ravikantsharma.dashboard.presentation.mapper.toRecurringType
import com.ravikantsharma.dashboard.presentation.mapper.toThousandsSeparatorUI
import com.ravikantsharma.dashboard.presentation.mapper.toTransactionCategory
import com.ravikantsharma.dashboard.presentation.mapper.toTransactionType
import com.ravikantsharma.session_management.domain.usecases.SessionUseCases
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal

class CreateTransactionViewModel(
    private val sessionUseCase: SessionUseCases,
    private val settingsPreferenceUseCase: SettingsPreferenceUseCase,
    private val transactionsUseCases: TransactionUseCases,
    private val timeProvider: TimeProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(initialUiState())
    val uiState: StateFlow<CreateTransactionViewState> = _uiState

    private val eventChannel = Channel<CreateTransactionEvent>(Channel.BUFFERED)
    val events = eventChannel.receiveAsFlow()

    init {
        fetchUserPreferences()
    }

    private fun fetchUserPreferences() {
        sessionUseCase.getSessionDataUseCase()
            .flatMapLatest { sessionData ->
                settingsPreferenceUseCase.getPreferencesUseCase(sessionData.userId)
            }
            .onEach { result ->
                if (result is Result.Success) {
                    _uiState.update {
                        it.copy(
                            userId = result.data.userId,
                            currency = result.data.currency,
                            expenseFormat = result.data.expenseFormat.toExpenseFormatUI(),
                            decimalSeparatorUI = result.data.decimalSeparator.toDecimalSeparatorUI(),
                            thousandsSeparatorUI = result.data.thousandsSeparator.toThousandsSeparatorUI()
                        )
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun initialUiState(): CreateTransactionViewState {
        val transactionType = TransactionTypeUI.EXPENSE
        return CreateTransactionViewState(
            transactionType = transactionType,
            transactionName = "",
            transactionNameHint = getTransactionHint(transactionType),
            amount = BigDecimal.ZERO,
            noteHint = "Add Note",
            note = "",
            transactionCategoryType = TransactionCategoryTypeUI.OTHER,
            showExpenseCategoryType = isExpenseCategoryTypeVisible(transactionType),
            recurringType = RecurringTypeUI.ONE_TIME,
            isCreateButtonEnabled = false,
            currentTime = timeProvider.currentLocalDateTime
        )
    }

    private fun getTransactionHint(type: TransactionTypeUI): String =
        if (type == TransactionTypeUI.EXPENSE) "Receiver" else "Sender"

    private fun isExpenseCategoryTypeVisible(type: TransactionTypeUI): Boolean =
        type == TransactionTypeUI.EXPENSE

    fun onAction(action: CreateTransactionAction) {
        when (action) {
            is CreateTransactionAction.OnTransactionCategoryUpdated -> updateState {
                copy(
                    transactionCategoryType = action.category
                )
            }

            is CreateTransactionAction.OnFrequencyUpdated -> updateState { copy(recurringType = action.frequency) }
            is CreateTransactionAction.OnTransactionTypeChanged -> updateState {
                copy(
                    transactionType = action.transactionType,
                    transactionNameHint = getTransactionHint(action.transactionType),
                    showExpenseCategoryType = isExpenseCategoryTypeVisible(action.transactionType)
                )
            }

            is CreateTransactionAction.OnTransactionNameUpdated -> {
                updateState {
                    copy(
                        transactionName = transactionsUseCases.validateTransactionNameUseCase(
                            input = action.transactionName,
                            previousValue = transactionName
                        )
                    )
                }
            }

            is CreateTransactionAction.OnAmountUpdated -> updateState { copy(amount = action.amount) }
            is CreateTransactionAction.OnNoteUpdated -> {
                updateState {
                    copy(
                        note = transactionsUseCases.validateNoteUseCase(
                            input = action.note,
                            previousValue = note
                        )
                    )
                }
            }
            CreateTransactionAction.OnCreateClicked -> handleCreateTransaction()
            CreateTransactionAction.OnBottomSheetCloseClicked -> {
                resetScreen()
                sendEvent(CreateTransactionEvent.CloseBottomSheet)
            }
        }
    }

    private fun updateState(update: CreateTransactionViewState.() -> CreateTransactionViewState) {
        _uiState.update { current ->
            val updated = current.update()
            updated.copy(isCreateButtonEnabled = validateInput(updated))
        }
    }

    private fun validateInput(state: CreateTransactionViewState): Boolean {
        return state.transactionName.isNotBlank() && state.transactionName.length in 3..14 &&
                state.amount > BigDecimal.ZERO
    }

    private fun sendEvent(event: CreateTransactionEvent) {
        viewModelScope.launch {
            eventChannel.send(event)
        }
    }

    private fun handleCreateTransaction() {
        viewModelScope.launch {
            val uiState = _uiState.value
            uiState.userId?.let {
                val recurringType = uiState.recurringType.toRecurringType()
                val nextRecurringDate = transactionsUseCases.getNextRecurringDateUseCase(
                    recurringType = recurringType
                )
                val transaction = Transaction(
                    transactionId = null,
                    userId = uiState.userId,
                    transactionType = uiState.transactionType.toTransactionType(),
                    transactionName = uiState.transactionName.trim(),
                    amount = if (_uiState.value.transactionType == TransactionTypeUI.INCOME) {
                        uiState.amount
                    } else {
                        uiState.amount.negate()
                    },
                    note = uiState.note.trim(),
                    transactionCategory = if (uiState.transactionType == TransactionTypeUI.INCOME) {
                        TransactionCategory.INCOME
                    } else {
                        uiState.transactionCategoryType.toTransactionCategory()
                    },
                    transactionDate = timeProvider.currentLocalDateTime,
                    recurringStartDate = timeProvider.currentLocalDateTime,
                    recurringTransactionId = null,
                    recurringType = recurringType,
                    nextRecurringDate = nextRecurringDate,
                )

                val result = transactionsUseCases.insertTransactionUseCase(transaction)
                if (result is Result.Success) {
                    eventChannel.send(CreateTransactionEvent.CloseBottomSheet)
                    resetScreen()
                }
            }
        }
    }

    private fun resetScreen() {
        val transactionType = TransactionTypeUI.EXPENSE
        _uiState.update {
            it.copy(
                transactionType = transactionType,
                transactionName = "",
                transactionNameHint = getTransactionHint(transactionType),
                amount = BigDecimal.ZERO,
                noteHint = "Add Note",
                note = "",
                transactionCategoryType = TransactionCategoryTypeUI.OTHER,
                showExpenseCategoryType = isExpenseCategoryTypeVisible(transactionType),
                recurringType = RecurringTypeUI.ONE_TIME,
                isCreateButtonEnabled = false
            )
        }
    }
}