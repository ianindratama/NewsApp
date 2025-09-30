package com.ianindratama.newsapp.core.data.settings.source.local.datastore

import androidx.datastore.core.Serializer
import com.ianindratama.newsapp.core.data.settings.model.UserSettingsDto
import com.ianindratama.newsapp.core.ui.utils.Cryptography
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream
import java.util.Base64

object UserPreferencesSerializer: Serializer<UserSettingsDto> {
    override val defaultValue: UserSettingsDto
        get() = UserSettingsDto()

    override suspend fun readFrom(input: InputStream): UserSettingsDto {
        val encryptedBytes = withContext(Dispatchers.IO) {
            input.use { it.readBytes() }
        }
        val encryptedBytesDecoded = Base64.getDecoder().decode(encryptedBytes)
        val decryptedBytes = Cryptography.decrypt(encryptedBytesDecoded)
        val decodedJsonString = decryptedBytes.decodeToString()
        return Json.decodeFromString(decodedJsonString)
    }

    override suspend fun writeTo(t: UserSettingsDto, output: OutputStream) {
        val json = Json.encodeToString(t)
        val bytes = json.toByteArray()
        val encryptedBytes = Cryptography.encrypt(bytes)
        val encryptedBytesBase64 = Base64.getEncoder().encode(encryptedBytes)
        withContext(Dispatchers.IO) {
            output.use {
                it.write(encryptedBytesBase64)
            }
        }
    }
}