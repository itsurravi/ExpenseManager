package com.ravikantsharma.dashboard.presentation.all_transactions

sealed interface AllTransactionsEvent {
    data object NavigateBack : AllTransactionsEvent
}