package com.peihua8858.tools

abstract class Logger {
    companion object {
        /**
         * Priority constant for the println method; use Log.v.
         */
        val VERBOSE = 2;

        /**
         * Priority constant for the println method; use Log.d.
         */
        val DEBUG = 3;

        /**
         * Priority constant for the println method; use Log.i.
         */
        val INFO = 4;

        /**
         * Priority constant for the println method; use Log.w.
         */
        val WARN = 5;

        /**
         * Priority constant for the println method; use Log.e.
         */
        val ERROR = 6;

        /**
         * Priority constant for the println method.
         */
        val ASSERT = 7;
    }

    abstract fun printLog(level: Int, stackTraceIndex: Int, tag: String, message: String)
    abstract fun writeLog(tag: String, stackTraceIndex: Int, message: String)
}

expect fun logcat(): Logger
internal fun printLog(level: Int, stackTraceIndex: Int, tag: String, message: String) =
    logcat().printLog(level, stackTraceIndex, tag, message)
internal fun printLog(level: Int, tag: String, message: String) =
    printLog(level, 3, tag, message)

internal fun writeLogToFile(tag: String, stackTraceIndex: Int, message: String) =
    logcat().writeLog(tag, stackTraceIndex, message)

