package com.peihua8858.tools.model

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * dsl 回调监听
 * @author dingpeihua
 * @date 2023/10/11 11:39
 * @version 1.0
 */
class DslApiModel<Response> {
    private var coroutineScope: CoroutineScope? = null
    internal lateinit var request: suspend CoroutineScope.() -> Response

    private var onStart: (() -> Unit?)? = null

    private var onResponse: ((Response) -> Unit?)? = null

    private var onError: ((Throwable) -> Unit?)? = null

    private var onComplete: (() -> Unit?)? = null
    private var onCancel: (() -> Unit?)? = null
    private var isCanceledJob = false

    internal fun isOnStart(): Boolean {
        return onStart != null
    }

    internal fun isOnResponse(): Boolean {
        return onResponse != null
    }

    internal fun isOnCancel(): Boolean {
        return onCancel != null
    }

    internal fun isOnError(): Boolean {
        return onError != null
    }

    internal fun isOnComplete(): Boolean {
        return onComplete != null
    }

    infix fun onStart(onStart: (() -> Unit?)?): DslApiModel<Response> {
        this.onStart = onStart
        return this
    }
    infix fun onRequest(request: suspend CoroutineScope.() -> Response): DslApiModel<Response> {
        this.request = request
        return this
    }

    infix fun onResponse(onResponse: ((Response) -> Unit)?): DslApiModel<Response> {
        this.onResponse = onResponse
        return this
    }

    infix fun onError(onError: ((Throwable) -> Unit)?): DslApiModel<Response> {
        this.onError = onError
        return this
    }

    infix fun onComplete(onComplete: (() -> Unit)?): DslApiModel<Response> {
        this.onComplete = onComplete
        return this
    }

    infix fun onCancel(onCancel: (() -> Unit)?): DslApiModel<Response> {
        this.onCancel = onCancel
        return this
    }

    val isCanceled: Boolean
        get() = isCanceledJob

    fun cancelJob() {
        isCanceledJob = true
        coroutineScope?.cancel(CancellationException("Close dialog"))
        invokeOnCancel()
    }

    internal suspend fun syncLaunch() {
        coroutineScope {
            invokeOnStart()
            try {
                val response = request()
                invokeOnResponse(response)
            } catch (e: Throwable) {
                print(e.stackTraceToString())
                invokeOnError(e)
            } finally {
                invokeOnComplete()
            }
        }
    }

    internal suspend fun launch() {
        invokeOnStart()
        try {
            val response = withContext(Dispatchers.IO) {
                coroutineScope = this
                request()
            }
            invokeOnResponse(response)
        } catch (e: Throwable) {
            print(e.stackTraceToString())
            invokeOnError(e)
        } finally {
            invokeOnComplete()
        }
    }

    internal fun syncLaunch(viewModelScope: CoroutineScope) {
        viewModelScope.launch(Dispatchers.Main) {
            coroutineScope = this
            invokeOnStart()
            try {
                val response = request()
                invokeOnResponse(response)
            } catch (e: Throwable) {
                print(e.stackTraceToString() )
                invokeOnError(e)
            } finally {
                invokeOnComplete()
            }
        }
    }

    internal fun launch(viewModelScope: CoroutineScope) {
        viewModelScope.launch(Dispatchers.Main) {
            coroutineScope = this
            invokeOnStart()
            try {
                val response = withContext(Dispatchers.IO) {
                    request()
                }
                invokeOnResponse(response)
            } catch (e: Throwable) {
                print( e.stackTraceToString() )
                invokeOnError(e)
            } finally {
                invokeOnComplete()
            }
        }
    }

    fun invokeOnError(e: Throwable) {
        if (isCanceled) return
        this.onError?.invoke(e)
    }

    fun invokeOnResponse(response: Response) {
        if (isCanceled) return
        this.onResponse?.invoke(response)
    }

    fun invokeOnStart() {
        if (isCanceled) return
        this.onStart?.invoke()
    }

    fun invokeOnComplete() {
        if (isCanceled) return
        this.onComplete?.invoke()
    }

    fun invokeOnCancel() {
        this.onCancel?.invoke()
    }
}