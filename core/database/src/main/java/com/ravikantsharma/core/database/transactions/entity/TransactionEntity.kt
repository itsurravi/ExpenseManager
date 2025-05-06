package com.ravikantsharma.core.database.transactions.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.ravikantsharma.core.database.auth.entity.UserInfoEntity
import com.ravikantsharma.core.domain.model.RecurringType
import com.ravikantsharma.core.domain.model.TransactionCategory
import com.ravikantsharma.core.domain.model.TransactionType
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = UserInfoEntity::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["userId"]), Index(value = ["recurringTransactionId"])]
)
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val transactionId: Long,
    val userId: Long,
    val transactionType: TransactionType,
    val transactionName: String,
    val amount: BigDecimal,
    val note: String?,
    val transactionCategory: TransactionCategory,
    val transactionDate: LocalDateTime,
    val recurringTransactionId: Long?,
    val recurringType: RecurringType,
    val nextRecurringDate: LocalDateTime?,
    val endDate: LocalDateTime?
)
