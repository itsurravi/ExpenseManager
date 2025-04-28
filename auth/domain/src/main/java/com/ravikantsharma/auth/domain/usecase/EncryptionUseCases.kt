package com.ravikantsharma.auth.domain.usecase

import com.ravikantsharma.core.domain.security.EncryptionService

data class EncryptionUseCases(
    val encryptPinUseCase: EncryptPinUseCase,
    val decryptPinUseCase: DecryptPinUseCase
)

class EncryptPinUseCase(private val encryptionService: EncryptionService) {
    operator fun invoke(pin: String): Pair<String, String> {
        return encryptionService.encrypt(pin)
    }
}

class DecryptPinUseCase(private val encryptionService: EncryptionService) {
    operator fun invoke(encryptedPin: String, iv: String): String {
        return encryptionService.decrypt(encryptedPin, iv)
    }
}