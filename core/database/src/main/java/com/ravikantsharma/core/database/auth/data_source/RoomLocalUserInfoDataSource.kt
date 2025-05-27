package com.ravikantsharma.core.database.auth.data_source

import android.database.sqlite.SQLiteConstraintException
import com.ravikantsharma.core.database.auth.dao.UserInfoDao
import com.ravikantsharma.core.database.auth.utils.toUserEntity
import com.ravikantsharma.core.database.auth.utils.toUserInfo
import com.ravikantsharma.core.domain.auth.data_source.LocalUserInfoDataSource
import com.ravikantsharma.core.domain.auth.model.UserInfo
import com.ravikantsharma.core.domain.security.EncryptionService
import com.ravikantsharma.core.domain.utils.DataError
import com.ravikantsharma.core.domain.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

class RoomLocalUserInfoDataSource(
    private val userInfoDao: UserInfoDao
) : LocalUserInfoDataSource {
    override suspend fun upsertUser(userInfo: UserInfo): Result<Long, DataError> =
        withContext(Dispatchers.IO) {
            try {
                val userId = userInfoDao.insertUser(userInfo.toUserEntity())

                when {
                    userId > 0 -> Result.Success(userId)
                    userId == -1L -> Result.Error(DataError.Local.INSERT_USER_ERROR)
                    else -> Result.Error(DataError.Local.UNKNOWN_DATABASE_ERROR)
                }
            } catch (e: SQLiteConstraintException) {
                Result.Error(DataError.Local.DUPLICATE_USER_ERROR)
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                Result.Error(DataError.Local.UNKNOWN_DATABASE_ERROR)
            }
        }

    override suspend fun getUser(userName: String): Result<UserInfo, DataError> =
        withContext(Dispatchers.IO) {
            try {
                val userEntity = userInfoDao.getUser(userName)
                userEntity?.let {
                    Result.Success(it.toUserInfo())
                } ?: Result.Error(DataError.Local.USER_FETCH_ERROR)
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                Result.Error(DataError.Local.UNKNOWN_DATABASE_ERROR)
            }
        }

    override fun getAllUsers(): Flow<Result<List<UserInfo>, DataError>> {
        return userInfoDao.getAllUsers()
            .map { userEntities ->
                if (userEntities.isNotEmpty()) {
                    Result.Success(userEntities.map { it.toUserInfo() })
                } else {
                    Result.Error(DataError.Local.USER_FETCH_ERROR)
                }
            }
            .catch { e ->
                if (e is CancellationException) throw e
                emit(Result.Error(DataError.Local.UNKNOWN_DATABASE_ERROR))
            }
            .flowOn(Dispatchers.IO)
    }
}