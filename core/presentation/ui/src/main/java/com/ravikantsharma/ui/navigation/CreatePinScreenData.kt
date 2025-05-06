package com.ravikantsharma.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
data class CreatePinScreenData(
    val username: String,
    val pin: String? = null
)