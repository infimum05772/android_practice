package com.itis.android_tasks.utils

import java.nio.charset.StandardCharsets
import java.security.MessageDigest


object PasswordUtil {
    private const val ENCRYPTING_ALGORITHM = "MD5"
    private const val BYTE_FORMATTING = "%02x"

    fun encrypt(password: String): String {
        val md = MessageDigest.getInstance(ENCRYPTING_ALGORITHM)
        md.update(password.toByteArray(StandardCharsets.UTF_8))
        val hashBytes = md.digest()
        return with(StringBuilder()) {
            hashBytes.forEach { byte ->
                append(String.format(BYTE_FORMATTING, byte))
            }
            toString().lowercase()
        }
    }
}
