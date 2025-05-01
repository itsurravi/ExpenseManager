package com.ravikantsharma.session_management.data.utils

import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import com.ravikantsharma.session_management.data.SessionPreferences
import java.io.InputStream
import java.io.OutputStream

object SessionSerializer : Serializer<SessionPreferences> {
    override suspend fun readFrom(input: InputStream): SessionPreferences {
        return try {
            SessionPreferences.parseFrom(input)
        } catch (e: InvalidProtocolBufferException) {
            defaultValue
        }
    }

    override suspend fun writeTo(
        t: SessionPreferences,
        output: OutputStream
    ) {
        t.writeTo(output)
    }

    override val defaultValue: SessionPreferences = SessionPreferences.getDefaultInstance()
}