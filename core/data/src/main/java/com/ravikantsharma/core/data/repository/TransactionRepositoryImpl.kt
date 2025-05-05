package com.ravikantsharma.core.data.repository

import com.ravikantsharma.core.domain.transactions.data_source.LocalTransactionDataSource
import com.ravikantsharma.core.domain.transactions.model.Transaction
import com.ravikantsharma.core.domain.transactions.repository.TransactionRepository
import com.ravikantsharma.core.domain.utils.DataError
import com.ravikantsharma.core.domain.utils.Result
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

class TransactionRepositoryImpl(
    private val localTransactionDataSource: LocalTransactionDataSource
) : TransactionRepository {

    override suspend fun insertTransaction(transaction: Transaction): Result<Unit, DataError> {
        return localTransactionDataSource.upsertTransaction(transaction)
    }

    override fun getTransactionsForUser(userId: Long): Flow<Result<List<Transaction>, DataError>> {
        return localTransactionDataSource.getTransactionsForUser(userId)
    }

    override fun getRecurringTransactionSeries(recurringId: Long): Flow<Result<List<Transaction>, DataError>> {
        return localTransactionDataSource.getRecurringTransactionSeries(recurringId)
    }

    override suspend fun getDueRecurringTransactions(currentDate: LocalDateTime): Result<List<Transaction>, DataError> {
        return localTransactionDataSource.getDueRecurringTransactions(currentDate)
    }
}