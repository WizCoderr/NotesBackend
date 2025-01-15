package me.wizcoderr.auth

import io.ktor.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

private val hashKey= System.getenv("HASH_KEY").toByteArray()
private val hmscKey = SecretKeySpec(hashKey, "HmacSHA1")

fun hashPassword(password: String): String {
    val algo = Mac.getInstance("HMACSHA1")
    algo.init(hmscKey)
    return  hex(algo.doFinal(password.toByteArray(Charsets.UTF_8)))
}
