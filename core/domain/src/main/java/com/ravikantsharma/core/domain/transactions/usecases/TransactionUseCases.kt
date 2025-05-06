package com.ravikantsharma.core.domain.transactions.usecases

import com.ravikantsharma.core.domain.model.TransactionCategory
import com.ravikantsharma.core.domain.transactions.model.Transaction
import com.ravikantsharma.core.domain.transactions.model.TransactionGroupItem
import com.ravikantsharma.core.domain.transactions.repository.TransactionRepository
import com.ravikantsharma.core.domain.utils.CalendarUtils
import com.ravikantsharma.core.domain.utils.DataError
import com.ravikantsharma.core.domain.utils.Result
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

data class TransactionUseCases(
    val insertTransactionUseCase: InsertTransactionUseCase,
    val getTransactionsForUserUseCase: GetTransactionsForUserUseCase,
    val getRecurringTransactionSeriesUseCase: GetRecurringTransactionSeriesUseCase,
    val getDueRecurringTransactionsUseCase: GetDueRecurringTransactionsUseCase,
    val getAccountBalanceUseCase: GetAccountBalanceUseCase,
    val getMostPopularExpenseCategoryUseCase: GetMostPopularExpenseCategoryUseCase,
    val getLargestTransactionUseCase: GetLargestTransactionUseCase,
    val getPreviousWeekTotalUseCase: GetPreviousWeekTotalUseCase,
    val getTransactionsGroupedByDateUseCase: GetTransactionsGroupedByDateUseCase,
)

class InsertTransactionUseCase(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(transaction: Transaction): Result<Unit, DataError> {
        return transactionRepository.insertTransaction(transaction)
    }
}

class GetTransactionsForUserUseCase(
    private val transactionRepository: TransactionRepository
) {
    operator fun invoke(
        userId: Long,
        limit: Int? = null
    ): Flow<Result<List<Transaction>, DataError>> {
        return transactionRepository.getTransactionsForUser(userId, limit)
    }
}

class GetRecurringTransactionSeriesUseCase(
    private val transactionRepository: TransactionRepository
) {
    operator fun invoke(recurringId: Long): Flow<Result<List<Transaction>, DataError>> {
        return transactionRepository.getRecurringTransactionSeries(recurringId)
    }
}

class GetDueRecurringTransactionsUseCase(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(currentDate: LocalDateTime): Result<List<Transaction>, DataError> {
        return transactionRepository.getDueRecurringTransactions(currentDate)
    }
}

class GetAccountBalanceUseCase(
    private val transactionRepository: TransactionRepository
) {
    operator fun invoke(userId: Long): Flow<Result<BigDecimal, DataError>> {
        return transactionRepository.getAccountBalance(userId)
    }
}

class GetMostPopularExpenseCategoryUseCase(
    private val transactionRepository: TransactionRepository
) {
    operator fun invoke(userId: Long): Flow<Result<TransactionCategory?, DataError>> {
        return transactionRepository.getMostPopularExpenseCategory(userId)
    }
}

class GetLargestTransactionUseCase(
    private val transactionRepository: TransactionRepository
) {
    operator fun invoke(userId: Long): Flow<Result<Transaction?, DataError>> {
        return transactionRepository.getLargestTransaction(userId)
    }
}

class GetPreviousWeekTotalUseCase(
    private val transactionRepository: TransactionRepository
) {
    operator fun invoke(userId: Long): Flow<Result<BigDecimal, DataError>> {
        return transactionRepository.getPreviousWeekTotal(userId)
    }
}

class GetTransactionsGroupedByDateUseCase {
    operator fun invoke(transactions: List<Transaction>): List<TransactionGroupItem> {
        val today = CalendarUtils.currentEstDate
        val yesterday = today.minusDays(1)
        val dateFormatter = DateTimeFormatter.ofPattern("MMM d")

        return transactions
            .sortedByDescending { it.transactionDate }
            .groupBy { transaction ->
                when (transaction.transactionDate.toLocalDate()) {
                    today -> "TODAY"
                    yesterday -> "YESTERDAY"
                    else -> transaction.transactionDate.format(dateFormatter).uppercase(Locale.US)
                }
            }
            .map { (dateLabel, transactions) ->
                TransactionGroupItem(
                    dateLabel = dateLabel,
                    transactions = transactions
                )
            }
    }
}