package com.ravikantsharma.core.data.repository

import com.ravikantsharma.core.domain.auth.data_source.LocalUserInfoDataSource
import com.ravikantsharma.core.domain.auth.model.UserInfo
import com.ravikantsharma.core.domain.auth.repository.UserInfoRepository
import com.ravikantsharma.core.domain.utils.DataError
import com.ravikantsharma.core.domain.utils.Result
import kotlinx.coroutines.flow.Flow

class UserInfoRepositoryImpl(
    private val localUserInfoDataSource: LocalUserInfoDataSource
) : UserInfoRepository {
    override suspend fun registerUser(userInfo: UserInfo): Result<Long, DataError> {
        return localUserInfoDataSource.upsertUser(userInfo)
    }

    override suspend fun getUser(username: String): Result<UserInfo, DataError> {
        return localUserInfoDataSource.getUser(username)
    }

    override fun getAllUsers(): Flow<Result<List<UserInfo>, DataError>> {
        return localUserInfoDataSource.getAllUsers()
    }
}