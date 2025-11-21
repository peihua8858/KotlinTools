package com.peihua8858.tools

import com.peihua8858.tools.log.Logcat

class AndroidLogger : com.peihua8858.tools.Logger() {
    override fun printLog(level: Int, stackTraceIndex: Int, tag: String, message: String) {
        when (level) {
            VERBOSE -> Logcat.printLog(stackTraceIndex, Logcat.V, tag, message)
            DEBUG -> Logcat.printLog(stackTraceIndex, Logcat.D, tag, message)
            INFO -> Logcat.printLog(stackTraceIndex, Logcat.I, tag, message)
            WARN -> Logcat.printLog(stackTraceIndex, Logcat.W, tag, message)
            ERROR -> Logcat.printLog(stackTraceIndex, Logcat.E, tag, message)
        }
    }

    override fun writeLog(tag: String, stackTraceIndex: Int, message: String) {
        val context = ContextInitializer.context
        Logcat.writeLog(context, stackTraceIndex, tag, message)
    }
}

private val logger: com.peihua8858.tools.Logger = AndroidLogger()
actual fun logcat(): com.peihua8858.tools.Logger = logger