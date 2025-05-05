package com.ravikantsharma.core.domain.transactions.model

import com.ravikantsharma.core.domain.model.ExpenseCategory
import com.ravikantsharma.core.domain.model.RecurringType
import com.ravikantsharma.core.domain.model.TransactionType
import java.math.BigDecimal
import java.time.LocalDateTime

data class Transaction(
    val transactionId: Long?,
    val userId: Long,
    val transactionType: TransactionType,
    val transactionName: String,
    val amount: BigDecimal,
    val note: String?,
    val expenseCategory: ExpenseCategory,
    val transactionDate: LocalDateTime,
    val recurringTransactionId: Long?,
    val recurringType: RecurringType,
    val nextRecurringDate: LocalDateTime?,
    val endDate: LocalDateTime?
)
