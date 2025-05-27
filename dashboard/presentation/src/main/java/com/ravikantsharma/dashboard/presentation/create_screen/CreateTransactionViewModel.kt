package com.ravikantsharma.dashboard.presentation.create_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ravikantsharma.core.domain.model.TransactionCategory
import com.ravikantsharma.core.domain.preference.usecase.PreferenceUseCase
import com.ravikantsharma.core.domain.time.TimeProvider
import com.ravikantsharma.core.domain.transactions.model.Transaction
import com.ravikantsharma.core.domain.transactions.usecases.TransactionUseCases
import com.ravikantsharma.core.domain.utils.Result
import com.ravikantsharma.core.presentation.designsystem.model.RecurringTypeUI
import com.ravikantsharma.core.presentation.designsystem.model.TransactionCategoryTypeUI
import com.ravikantsharma.core.presentation.designsystem.model.TransactionTypeUI
import com.ravikantsharma.dashboard.domain.usecases.create_transactions.CreateTransactionsUseCases
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
    private val sessionUseCases: SessionUseCases,
    private val preferenceUseCase: PreferenceUseCase,
    private val transactionUseCases: TransactionUseCases,
    private val createTransactionsUseCases: CreateTransactionsUseCases,
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
        sessionUseCases.getSessionDataUseCase()
            .flatMapLatest { sessionData ->
                preferenceUseCase.getPreferencesUseCase(sessionData.userId)
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

    fun onAction(action: CreateTransactionAction) {
        when (action) {
            is CreateTransactionAction.OnTransactionCategoryUpdated -> updateState {
                copy(transactionCategoryType = action.category)
            }
            is CreateTransactionAction.OnFrequencyUpdated -> updateState { copy(recurringType = action.frequency) }
            is CreateTransactionAction.OnTransactionTypeChanged -> updateOnTransactionTypeChange(action)
            is CreateTransactionAction.OnTransactionNameUpdated -> updateOnTransactionNameChange(action)
            is CreateTransactionAction.OnAmountUpdated -> updateState { copy(amount = action.amount) }
            is CreateTransactionAction.OnNoteUpdated -> updateOnNoteChange(action)
            CreateTransactionAction.OnCreateClicked -> handleCreateTransaction()
            CreateTransactionAction.OnBottomSheetCloseClicked -> {
                resetScreen()
                sendEvent(CreateTransactionEvent.CloseBottomSheet)
            }
        }
    }

    private fun updateOnNoteChange(action: CreateTransactionAction.OnNoteUpdated) {
        updateState {
            copy(
                note = transactionUseCases.validateNoteUseCase(
                    input = action.note,
                    previousValue = note
                )
            )
        }
    }

    private fun updateOnTransactionNameChange(action: CreateTransactionAction.OnTransactionNameUpdated) {
        updateState {
            copy(
                transactionName = transactionUseCases.validateTransactionNameUseCase(
                    input = action.transactionName,
                    previousValue = transactionName
                )
            )
        }
    }

    private fun updateOnTransactionTypeChange(action: CreateTransactionAction.OnTransactionTypeChanged) {
        updateState {
            copy(
                transactionType = action.transactionType,
                transactionNameHint = createTransactionsUseCases.getTransactionHintUseCase(
                    action.transactionType.toTransactionType()
                ),
                showExpenseCategoryType = createTransactionsUseCases.isExpenseCategoryVisibleUseCase(
                    action.transactionType.toTransactionType()
                )
            )
        }
    }

    private fun updateState(update: CreateTransactionViewState.() -> CreateTransactionViewState) {
        _uiState.update { current ->
            val updated = current.update()
            updated.copy(
                isCreateButtonEnabled = createTransactionsUseCases.isValidInputUseCase(
                    transactionName = updated.transactionName,
                    amount = updated.amount
                )
            )
        }
    }

    private fun sendEvent(event: CreateTransactionEvent) {
        viewModelScope.launch {
            eventChannel.send(event)
        }
    }

    private fun handleCreateTransaction() {
        viewModelScope.launch {
            val uiState = _uiState.value
            uiState.userId?.let { userId ->
                val transaction = createTransactionsUseCases.buildTransactionUseCase(
                    userId = userId,
                    transactionType = uiState.transactionType.toTransactionType(),
                    transactionName = uiState.transactionName,
                    amount = uiState.amount,
                    note = uiState.note,
                    transactionCategoryType = uiState.transactionCategoryType.toTransactionCategory(),
                    recurringType = uiState.recurringType.toRecurringType(),
                    currentTime = timeProvider.currentLocalDateTime
                )

                val result = transactionUseCases.insertTransactionUseCase(transaction)
                if (result is Result.Success) {
                    eventChannel.send(CreateTransactionEvent.CloseBottomSheet)
                    resetScreen()
                }
            }
        }
    }

    private fun resetScreen() {
        _uiState.update {
            initialUiState()
        }
    }

    private fun initialUiState(): CreateTransactionViewState {
        val transactionTypeUI = TransactionTypeUI.EXPENSE
        return CreateTransactionViewState(
            transactionType = transactionTypeUI,
            transactionName = "",
            transactionNameHint = createTransactionsUseCases.getTransactionHintUseCase(
                transactionTypeUI.toTransactionType()
            ),
            amount = BigDecimal.ZERO,
            noteHint = "Add Note",
            note = "",
            transactionCategoryType = TransactionCategoryTypeUI.OTHER,
            showExpenseCategoryType = createTransactionsUseCases.isExpenseCategoryVisibleUseCase(
                transactionTypeUI.toTransactionType()
            ),
            recurringType = RecurringTypeUI.ONE_TIME,
            isCreateButtonEnabled = false,
            currentTime = timeProvider.currentLocalDateTime
        )
    }
}