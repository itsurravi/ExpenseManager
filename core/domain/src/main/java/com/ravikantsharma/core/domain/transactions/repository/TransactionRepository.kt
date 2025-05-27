package com.ravikantsharma.core.domain.transactions.repository

import com.ravikantsharma.core.domain.model.TransactionCategory
import com.ravikantsharma.core.domain.transactions.model.Transaction
import com.ravikantsharma.core.domain.utils.DataError
import com.ravikantsharma.core.domain.utils.Result
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal
import java.time.LocalDateTime

interface TransactionRepository {

    suspend fun insertTransaction(transaction: Transaction): Result<Unit, DataError>

    fun getTransactionsForUser(
        userId: Long,
        limit: Int? = null
    ): Flow<Result<List<Transaction>, DataError>>

    suspend fun getDueRecurringTransactions(currentDate: LocalDateTime): Result<List<Transaction>, DataError>

    fun getAccountBalance(userId: Long): Flow<Result<BigDecimal, DataError>>

    fun getMostPopularExpenseCategory(userId: Long): Flow<Result<TransactionCategory?, DataError>>

    fun getLargestTransaction(userId: Long): Flow<Result<Transaction?, DataError>>

    fun getPreviousWeekTotal(userId: Long): Flow<Result<BigDecimal, DataError>>

    suspend fun getTransactionsForDateRange(
        userId: Long,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Result<List<Transaction>, DataError>
}