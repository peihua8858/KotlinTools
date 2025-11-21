package com.peihua8858.tools.utils

import kotlinx.coroutines.delay
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
fun Any?.isNotNull(): Boolean {
    contract {
        returns(true) implies (this@isNotNull != null)
    }
    return this != null
}

@OptIn(ExperimentalContracts::class)
fun Any?.isNull(): Boolean {
    contract {
        returns(false) implies (this@isNull != null)
    }
    return this == null
}

@OptIn(ExperimentalContracts::class)
fun Any?.checkNotNull(value: Any?, msg: String): Boolean {
    contract {
        returns() implies (value != null)
    }
    return checkNotNull(value) { msg }
}

@OptIn(ExperimentalContracts::class)
fun <T : Any> checkNotNull(value: T?, lazyMessage: () -> Any): Boolean {
    contract {
        returns() implies (value != null)
    }
    if (value == null) {
        val message = lazyMessage()
        throw IllegalStateException(message.toString())
    } else {
        return false
    }
}

fun Any?.checkNotNull(msg: String?): Boolean {
    if (isNotNull()) {
        return true
    }
    throw NullPointerException(msg)
}

fun Any.rangeArray(min: Int, length: Int): Array<String?> {
    val data: Array<String?> = arrayOfNulls(length)
    for (i in 0 until length) {
        data[i] = ((min + i).toString())
    }
    return data
}

suspend fun <T> Any?.retryIO(
    times: Int = Int.MAX_VALUE,
    initialDelay: Long = 100, // 0.1 second
    maxDelay: Long = 1000,    // 1 second
    factor: Double = 2.0,
    block: suspend () -> T
): T {
    var currentDelay = initialDelay
    repeat(times - 1) {
        try {
            return block()
        } catch (e: Exception) {
            eLog { e.stackTraceToString() }
            // you can log an error here and/or make a more finer-grained
            // analysis of the cause to see if retry is needed
        }
        delay(currentDelay)
        currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelay)
    }
    return block() // last attempt
}