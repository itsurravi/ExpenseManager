package com.ravikantsharma.auth.domain.repository

import com.ravikantsharma.core.domain.auth.model.UserInfo
import com.ravikantsharma.core.domain.utils.DataError
import com.ravikantsharma.core.domain.utils.Result
import kotlinx.coroutines.flow.Flow

interface UserInfoRepository {
    suspend fun registerUser(userInfo: UserInfo): Result<Long, DataError>

    suspend fun getUser(username: String): Result<UserInfo, DataError>

    fun getAllUsers(): Flow<Result<List<UserInfo>, DataError>>
}