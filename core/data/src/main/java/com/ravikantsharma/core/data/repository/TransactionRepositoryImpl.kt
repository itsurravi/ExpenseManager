package com.ravikantsharma.core.data.repository

import com.ravikantsharma.core.domain.model.TransactionCategory
import com.ravikantsharma.core.domain.transactions.data_source.LocalTransactionDataSource
import com.ravikantsharma.core.domain.transactions.model.Transaction
import com.ravikantsharma.core.domain.transactions.repository.TransactionRepository
import com.ravikantsharma.core.domain.utils.DataError
import com.ravikantsharma.core.domain.utils.Result
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal
import java.time.LocalDateTime

class TransactionRepositoryImpl(
    private val localTransactionDataSource: LocalTransactionDataSource
) : TransactionRepository {

    override suspend fun insertTransaction(transaction: Transaction): Result<Unit, DataError> {
        return localTransactionDataSource.upsertTransaction(transaction)
    }

    override fun getTransactionsForUser(
        userId: Long,
        limit: Int?
    ): Flow<Result<List<Transaction>, DataError>> {
        return localTransactionDataSource.getTransactionsForUser(userId, limit)
    }

    override suspend fun getDueRecurringTransactions(currentDate: LocalDateTime): Result<List<Transaction>, DataError> {
        return localTransactionDataSource.getDueRecurringTransactions(currentDate)
    }

    override fun getAccountBalance(userId: Long): Flow<Result<BigDecimal, DataError>> {
        return localTransactionDataSource.getAccountBalance(userId)
    }

    override fun getMostPopularExpenseCategory(userId: Long): Flow<Result<TransactionCategory?, DataError>> {
        return localTransactionDataSource.getMostPopularExpenseCategory(userId)
    }

    override fun getLargestTransaction(userId: Long): Flow<Result<Transaction?, DataError>> {
        return localTransactionDataSource.getLargestTransaction(userId)
    }

    override fun getPreviousWeekTotal(userId: Long): Flow<Result<BigDecimal, DataError>> {
        return localTransactionDataSource.getPreviousWeekTotal(userId)
    }

    override suspend fun getTransactionsForDateRange(
        userId: Long,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Result<List<Transaction>, DataError> {
        return try {
            val transactions =
                localTransactionDataSource.getTransactionsForDateRange(userId, startDate, endDate)
            Result.Success(transactions)
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN_DATABASE_ERROR)
        }
    }
}