package com.ravikantsharma.core.database.transactions.data_source

import android.util.Log
import com.ravikantsharma.core.database.transactions.dao.TransactionsDao
import com.ravikantsharma.core.database.transactions.utils.toTransaction
import com.ravikantsharma.core.database.transactions.utils.toTransactionEntity
import com.ravikantsharma.core.domain.model.RecurringType
import com.ravikantsharma.core.domain.model.TransactionCategory
import com.ravikantsharma.core.domain.security.EncryptionService
import com.ravikantsharma.core.domain.transactions.data_source.LocalTransactionDataSource
import com.ravikantsharma.core.domain.transactions.model.Transaction
import com.ravikantsharma.core.domain.utils.CalendarUtils
import com.ravikantsharma.core.domain.utils.DataError
import com.ravikantsharma.core.domain.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.math.BigDecimal
import java.time.LocalDateTime
import kotlin.coroutines.cancellation.CancellationException

class RoomTransactionDataSource(
    private val transactionsDao: TransactionsDao,
    private val encryptionService: EncryptionService
) : LocalTransactionDataSource {

    override suspend fun upsertTransaction(transaction: Transaction): Result<Unit, DataError> {
        return try {
            val transactionEntity = transaction.toTransactionEntity(encryptionService)

            val insertedId = transactionsDao.upsertTransaction(transactionEntity)

            if (transactionEntity.recurringType != RecurringType.ONE_TIME && transactionEntity.recurringTransactionId == null) {
                val updatedEntity = transactionEntity.copy(
                    transactionId = insertedId,
                    recurringTransactionId = insertedId
                )
                transactionsDao.upsertTransaction(updatedEntity)
            }

            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN_DATABASE_ERROR)
        }
    }

    override fun getTransactionsForUser(
        userId: Long,
        limit: Int?
    ): Flow<Result<List<Transaction>, DataError>> {
        return transactionsDao.getTransactionsForUser(userId, limit)
            .map { transactions ->
                Result.Success(transactions.map { it.toTransaction(encryptionService) }) as Result<List<Transaction>, DataError>
            }
            .catch {
                emit(Result.Error(DataError.Local.UNKNOWN_DATABASE_ERROR))
            }
    }

    override suspend fun getDueRecurringTransactions(currentDate: LocalDateTime): Result<List<Transaction>, DataError> {
        return try {
            val transactions = transactionsDao.getDueRecurringTransactions(currentDate)

            if (transactions.isNotEmpty()) {
                Result.Success(transactions.map { it.toTransaction(encryptionService) })
            } else {
                Result.Error(DataError.Local.TRANSACTION_FETCH_ERROR)
            }
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN_DATABASE_ERROR)
        }
    }

    override fun getAccountBalance(userId: Long): Flow<Result<BigDecimal, DataError>> {
        return transactionsDao.getAccountBalance(userId)
            .map { balanceString ->
                try {
                    Result.Success(BigDecimal(balanceString))
                } catch (e: NumberFormatException) {
                    Result.Error(DataError.Local.UNKNOWN_DATABASE_ERROR)
                }
            }
            .catch {
                emit(Result.Error(DataError.Local.UNKNOWN_DATABASE_ERROR))
            }
    }

    override fun getMostPopularExpenseCategory(userId: Long): Flow<Result<TransactionCategory?, DataError>> {
        return transactionsDao.getMostPopularExpenseCategory(userId)
            .map { category ->
                Result.Success(category) as Result<TransactionCategory?, DataError>
            }
            .catch { exception ->
                if (exception is CancellationException) throw exception
                emit(Result.Error(DataError.Local.UNKNOWN_DATABASE_ERROR))
            }
    }

    override fun getLargestTransaction(userId: Long): Flow<Result<Transaction?, DataError>> {
        return transactionsDao.getLargestTransaction(userId)
            .map { transactionEntity ->
                Result.Success(transactionEntity?.toTransaction(encryptionService)) as Result<Transaction?, DataError>
            }
            .catch {
                emit(Result.Error(DataError.Local.UNKNOWN_DATABASE_ERROR))
            }
    }

    override fun getPreviousWeekTotal(userId: Long): Flow<Result<BigDecimal, DataError>> {
        val (startDate, endDate) = CalendarUtils.getPreviousWeekRange()
        Log.d("ExpenseManager", "Start of previous week: $startDate")
        Log.d("ExpenseManager", "End of previous week: $endDate")
        return transactionsDao.getPreviousWeekTotal(userId, startDate, endDate)
            .map { amount ->
                try {
                    Result.Success(amount)
                } catch (e: NumberFormatException) {
                    Result.Error(DataError.Local.UNKNOWN_DATABASE_ERROR)
                }
            }
            .catch {
                emit(Result.Error(DataError.Local.UNKNOWN_DATABASE_ERROR))
            }
    }

    override suspend fun getTransactionsForDateRange(
        userId: Long,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): List<Transaction> {
        return transactionsDao.getTransactionsForDateRange(userId, startDate, endDate)
            .map { it.toTransaction(encryptionService) }
    }
}