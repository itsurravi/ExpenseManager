package com.ravikantsharma.dashboard.domain.usecases.create_transactions

import com.ravikantsharma.core.domain.model.RecurringType
import com.ravikantsharma.core.domain.model.TransactionCategory
import com.ravikantsharma.core.domain.model.TransactionType
import com.ravikantsharma.core.domain.time.TimeProvider
import com.ravikantsharma.core.domain.transactions.model.Transaction
import com.ravikantsharma.core.domain.transactions.usecases.TransactionUseCases
import java.math.BigDecimal
import java.time.LocalDateTime

data class CreateTransactionsUseCases(
    val getTransactionHintUseCase: GetTransactionHintUseCase,
    val isExpenseCategoryVisibleUseCase: IsExpenseCategoryVisibleUseCase,
    val isValidInputUseCase: IsValidInputUseCase,
    val buildTransactionUseCase: BuildTransactionUseCase
)

class GetTransactionHintUseCase {
    operator fun invoke(type: TransactionType): String {
        return if (type == TransactionType.EXPENSE) "Receiver" else "Sender"
    }
}

class IsExpenseCategoryVisibleUseCase {
    operator fun invoke(type: TransactionType): Boolean {
        return type == TransactionType.EXPENSE
    }
}

class IsValidInputUseCase {
    operator fun invoke(transactionName: String, amount: BigDecimal): Boolean {
        return transactionName.isNotBlank() && transactionName.length in 3..14 &&
                amount > BigDecimal.ZERO
    }
}

class BuildTransactionUseCase(
    private val transactionUseCases: TransactionUseCases,
    private val currentTimeProvider: TimeProvider

) {
    operator fun invoke(
        userId: Long,
        transactionType: TransactionType,
        transactionName: String,
        amount: BigDecimal,
        note: String,
        transactionCategoryType: TransactionCategory,
        recurringType: RecurringType
    ): Transaction {
        val nextRecurringDate =
            transactionUseCases.getNextRecurringDateUseCase(recurringType = recurringType)

        val transactionCategory = if (transactionType == TransactionType.INCOME) {
            TransactionCategory.INCOME
        } else {
            transactionCategoryType
        }

        val finalAmount = if (transactionType == TransactionType.INCOME) {
            amount
        } else {
            amount.negate()
        }

        return Transaction(
            transactionId = null,
            userId = userId,
            transactionType = transactionType,
            transactionName = transactionName.trim(),
            amount = finalAmount,
            note = note.trim(),
            transactionCategory = transactionCategory,
            transactionDate = currentTimeProvider.currentLocalDateTime,
            recurringStartDate = currentTimeProvider.currentLocalDateTime,
            recurringTransactionId = null,
            recurringType = recurringType,
            nextRecurringDate = nextRecurringDate
        )
    }
}