package com.peihua8858.tools
import platform.Foundation.NSLog
 class IosLogger : Logger() {
     override fun printLog(level: Int, stackTraceIndex: Int, tag: String, message: String) =
        NSLog(tag, message)

     override fun writeLog(tag: String, stackTraceIndex: Int, message: String) {
         NSLog(tag, message)
    }
}

private val logger: Logger = IosLogger()
actual fun logcat(): Logger = logger
