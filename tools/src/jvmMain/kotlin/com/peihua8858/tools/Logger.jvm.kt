package com.peihua8858.tools


class AndroidLogger : Logger() {
    override fun printLog(level: Int, stackTraceIndex: Int, tag: String, message: String) {
        when (level) {
            VERBOSE -> println(tag + message)
            else -> println(tag + message)
        }
    }

    override fun writeLog(tag: String, stackTraceIndex: Int, message: String) {
        println(tag + message)
    }
}

private val logger: Logger = AndroidLogger()
actual fun logcat(): Logger = logger