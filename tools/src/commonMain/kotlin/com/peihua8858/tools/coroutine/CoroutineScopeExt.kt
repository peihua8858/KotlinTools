package com.peihua8858.tools.coroutine

import com.peihua8858.tools.utils.eLog
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

public fun WorkScope(): CoroutineScope = ContextScope(SupervisorJob() + Dispatchers.IO)

internal class ContextScope(context: CoroutineContext) : CoroutineScope {
    override val coroutineContext: CoroutineContext = context

    override fun toString(): String = "CoroutineScope(coroutineContext=$coroutineContext)"
}

suspend fun <T> runCatching(
    request: suspend CoroutineScope.() -> T,
    callback: (T) -> Unit,
    onError: (Throwable) -> Unit,
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
    complete: () -> Unit = {},
) {
    coroutineScope {
        try {
            val result = withContext(dispatcher) {
                request()
            }
            callback(result)
        } catch (e: Throwable) {
            eLog { e.stackTraceToString() }
            onError(e)
        } finally {
            try {
                cancel()
            } catch (e: Throwable) {
                eLog { e.stackTraceToString() }
            }
            complete()
        }
    }
}