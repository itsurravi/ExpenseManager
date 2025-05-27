package com.ravikantsharma.core.database.transactions.utils

import com.ravikantsharma.core.database.transactions.entity.TransactionEntity
import com.ravikantsharma.core.domain.model.RecurringType
import com.ravikantsharma.core.domain.model.TransactionCategory
import com.ravikantsharma.core.domain.security.EncryptionService
import com.ravikantsharma.core.domain.transactions.model.Transaction
import java.math.BigDecimal


fun Transaction.toTransactionEntity(encryptionService: EncryptionService): TransactionEntity {
    return TransactionEntity(
        transactionId = this.transactionId ?: 0L,
        userId = this.userId,
        transactionType = this.transactionType,
        transactionName = this.transactionName,
        amount = this.amount.toPlainString(),
        note = this.note,
        transactionCategory = this.transactionCategory.name,
        transactionDate = this.transactionDate,
        recurringStartDate = this.recurringStartDate,
        recurringTransactionId = this.recurringTransactionId,
        recurringType = this.recurringType.name,
        nextRecurringDate = this.nextRecurringDate,
    )
}

fun TransactionEntity.toTransaction(encryptionService: EncryptionService): Transaction {
    return Transaction(
        transactionId = this.transactionId,
        userId = this.userId,
        transactionType = this.transactionType,
        transactionName = this.transactionName,
        amount = try {
            BigDecimal(this.amount)
        } catch (e: Exception) {
            BigDecimal.ZERO
        },
        note = this.note,
        transactionCategory = try {
            TransactionCategory.valueOf(this.transactionCategory)
        } catch (e: IllegalArgumentException) {
            TransactionCategory.OTHER
        },
        transactionDate = this.transactionDate,
        recurringStartDate = this.recurringStartDate,
        recurringTransactionId = this.recurringTransactionId,
        recurringType = try {
            RecurringType.valueOf(this.recurringType)
        } catch (e: IllegalArgumentException) {
            RecurringType.ONE_TIME
        },
        nextRecurringDate = this.nextRecurringDate,
    )
}
