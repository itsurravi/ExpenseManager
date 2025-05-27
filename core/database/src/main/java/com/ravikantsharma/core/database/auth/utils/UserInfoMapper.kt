package com.ravikantsharma.core.database.auth.utils

import com.ravikantsharma.core.database.auth.entity.UserInfoEntity
import com.ravikantsharma.core.domain.auth.model.UserInfo

fun UserInfoEntity.toUserInfo(): UserInfo {
    return UserInfo(
        userId = this.userId,
        username = this.username,
        pin = this.pin
    )
}

fun UserInfo.toUserEntity(): UserInfoEntity {
    return UserInfoEntity(
        userId = this.userId ?: 0L,
        username = this.username,
        pin = this.pin
    )
}