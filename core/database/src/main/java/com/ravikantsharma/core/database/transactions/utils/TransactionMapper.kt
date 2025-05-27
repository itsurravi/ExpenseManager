package com.ravikantsharma.core.database.transactions.utils

import com.ravikantsharma.core.database.transactions.entity.TransactionEntity
import com.ravikantsharma.core.domain.security.EncryptionService
import com.ravikantsharma.core.domain.transactions.model.Transaction


fun Transaction.toTransactionEntity(encryptionService: EncryptionService): TransactionEntity {
    return TransactionEntity(
        transactionId = this.transactionId ?: 0L,
        userId = this.userId,
        transactionType = this.transactionType,
        transactionNameEncrypted = encryptionService.encrypt(this.transactionName),
        amount = this.amount,
        noteEncrypted = encryptionService.encrypt(this.note ?: ""),
        transactionCategory = this.transactionCategory,
        transactionDate = this.transactionDate,
        recurringStartDate = this.recurringStartDate,
        recurringTransactionId = this.recurringTransactionId,
        recurringType = this.recurringType,
        nextRecurringDate = this.nextRecurringDate,
    )
}

fun TransactionEntity.toTransaction(encryptionService: EncryptionService): Transaction {
    return Transaction(
        transactionId = this.transactionId,
        userId = this.userId,
        transactionType = this.transactionType,
        transactionName = encryptionService.decrypt(this.transactionNameEncrypted),
        amount = this.amount,
        note = this.noteEncrypted?.let {
            encryptionService.decrypt(it)
        } ?: "",
        transactionCategory = this.transactionCategory,
        transactionDate = this.transactionDate,
        recurringTransactionId = this.recurringTransactionId,
        recurringStartDate = this.recurringStartDate,
        recurringType = this.recurringType,
        nextRecurringDate = this.nextRecurringDate,
    )
}
