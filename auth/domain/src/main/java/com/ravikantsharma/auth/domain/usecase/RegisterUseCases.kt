package com.ravikantsharma.auth.domain.usecase

import com.ravikantsharma.auth.domain.repository.UserInfoRepository
import com.ravikantsharma.core.domain.auth.model.UserInfo
import com.ravikantsharma.core.domain.utils.DataError
import com.ravikantsharma.core.domain.utils.Result

data class RegisterUseCases(
    val registerUserUseCase: RegisterUserUseCase
)

class RegisterUserUseCase(
    private val userInfoRepository: UserInfoRepository
) {
    suspend operator fun invoke(userInfo: UserInfo): Result<Long, DataError> {
        return userInfoRepository.registerUser(userInfo)
    }
}