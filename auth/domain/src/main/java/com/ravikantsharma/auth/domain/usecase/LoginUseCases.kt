package com.ravikantsharma.auth.domain.usecase

import com.ravikantsharma.core.domain.auth.repository.UserInfoRepository
import com.ravikantsharma.core.domain.security.EncryptionService
import com.ravikantsharma.core.domain.utils.DataError
import com.ravikantsharma.core.domain.utils.Result

private const val MIN_USERNAME_LENGTH = 3
private const val MAX_USERNAME_LENGTH = 14

data class LoginUseCases(
    val isUsernameValidUseCase: IsUsernameValidUseCase,
    val initiateLoginUseCase: InitiateLoginUseCase
)

class IsUsernameValidUseCase {
    operator fun invoke(username: CharSequence): Boolean {
        return username.length in MIN_USERNAME_LENGTH..MAX_USERNAME_LENGTH &&
                username.all { it.isLetterOrDigit() }
    }
}

class InitiateLoginUseCase(
    private val userInfoRepository: UserInfoRepository,
    private val encryptionService: EncryptionService
) {
    suspend operator fun invoke(username: String, enteredPin: String): Result<Long, DataError> {
        return when(val userResult = userInfoRepository.getUser(username)) {
            is Result.Success -> {
                val storedPin = encryptionService.decrypt(
                    encryptedData = userResult.data.encryptedPin,
                    iv = userResult.data.iv
                )
                if (enteredPin == storedPin) {
                    Result.Success(userResult.data.userId ?: 0L)
                } else {
                    Result.Error(DataError.Local.INVALID_CREDENTIALS)
                }
            }
            is Result.Error -> Result.Error(DataError.Local.INVALID_CREDENTIALS)
        }
    }
}