package com.peihua8858.tools
import com.peihua8858.tools.Logger
import platform.Foundation.NSLog
 class IosLogger : Logger() {
     override fun printLog(level: Int, stackTraceIndex: Int, tag: String, message: String) =
        NSLog("ChatBox", message)

     override fun writeLog(tag: String, stackTraceIndex: Int, message: String) {
         NSLog("ChatBox", message)
    }
}

private val logger: Logger = IosLogger()
actual fun logcat(): Logger = logger
