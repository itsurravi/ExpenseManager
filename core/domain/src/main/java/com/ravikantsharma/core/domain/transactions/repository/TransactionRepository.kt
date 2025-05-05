package com.ravikantsharma.core.domain.transactions.repository

import com.ravikantsharma.core.domain.transactions.model.Transaction
import com.ravikantsharma.core.domain.utils.DataError
import com.ravikantsharma.core.domain.utils.Result
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

interface TransactionRepository {

    suspend fun insertTransaction(transaction: Transaction): Result<Unit, DataError>

    fun getTransactionsForUser(userId: Long): Flow<Result<List<Transaction>, DataError>>

    fun getRecurringTransactionSeries(recurringId: Long): Flow<Result<List<Transaction>, DataError>>

    suspend fun getDueRecurringTransactions(currentDate: LocalDateTime): Result<List<Transaction>, DataError>
}