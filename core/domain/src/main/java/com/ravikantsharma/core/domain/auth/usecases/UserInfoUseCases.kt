package com.ravikantsharma.core.domain.auth.usecases

import com.ravikantsharma.core.domain.auth.model.UserInfo
import com.ravikantsharma.core.domain.auth.repository.UserInfoRepository
import com.ravikantsharma.core.domain.security.EncryptionService
import com.ravikantsharma.core.domain.utils.DataError
import com.ravikantsharma.core.domain.utils.Result

data class UserInfoUseCases(
    val getUserInfoUseCase: GetUserInfoUseCase
)

class GetUserInfoUseCase(
    private val userInfoRepository: UserInfoRepository
) {
    suspend operator fun invoke(userName: String): Result<UserInfo, DataError> {

        return when (val result = userInfoRepository.getUser(username = userName)) {
            is Result.Error -> Result.Error(result.error)
            is Result.Success -> {
                Result.Success(
                    UserInfo(
                        userId = result.data.userId,
                        username = result.data.username,
                        pin = result.data.pin
                    )
                )
            }
        }
    }
}