package com.ravikantsharma.dashboard.presentation.dashboard

import com.ravikantsharma.core.domain.preference.model.UserPreferences
import com.ravikantsharma.core.presentation.designsystem.model.TransactionCategoryTypeUI
import java.math.BigDecimal
import java.time.LocalDateTime

data class DashboardViewState(
    val username: String = "",
    val preference: UserPreferences? = null,
    val accountBalance: String = "",
    val mostPopularCategory: TransactionCategoryTypeUI? = null,
    val largestTransaction: LargestTransaction? = null,
    val previousWeekTotal: String = "",
    val transactions: List<TransactionGroupUIItem>? = null,
    val isSessionExpired: Boolean = false,
    val showCreateTransactionSheet: Boolean = false,
    val showExportTransactionSheet: Boolean = false
)

data class LargestTransaction(
    val name: String,
    val amount: String,
    val date: String
)

data class TransactionGroupUIItem(
    val dateLabel: String,
    val transactions: List<TransactionUIItem>
)

data class TransactionUIItem(
    val transactionId: Long = 0L,
    val transactionCategory: TransactionCategoryTypeUI,
    val title: String,
    val note: String? = null,
    val amount: BigDecimal,
    val date: LocalDateTime,
    val isCollapsed: Boolean = true
)