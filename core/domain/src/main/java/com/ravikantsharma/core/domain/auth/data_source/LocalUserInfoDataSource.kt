package com.ravikantsharma.core.domain.auth.data_source

import com.ravikantsharma.core.domain.auth.model.UserInfo
import com.ravikantsharma.core.domain.utils.DataError
import com.ravikantsharma.core.domain.utils.Result
import kotlinx.coroutines.flow.Flow

interface LocalUserInfoDataSource {
    suspend fun upsertUser(userInfo: UserInfo): Result<Long, DataError>

    suspend fun getUser(userId: Long): Result<UserInfo, DataError>

    fun getAllUsers(): Flow<Result<List<UserInfo>, DataError>>
}