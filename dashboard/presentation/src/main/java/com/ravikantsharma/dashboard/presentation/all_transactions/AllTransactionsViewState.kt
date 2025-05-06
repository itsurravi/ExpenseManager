package com.ravikantsharma.dashboard.presentation.all_transactions

import com.ravikantsharma.core.domain.preference.model.UserPreferences
import com.ravikantsharma.dashboard.presentation.dashboard.TransactionGroupUIItem

data class AllTransactionsViewState(
    val preference: UserPreferences? = null,
    val transactions: List<TransactionGroupUIItem>? = null,
    val showCreateTransactionsSheet: Boolean = false,
)
