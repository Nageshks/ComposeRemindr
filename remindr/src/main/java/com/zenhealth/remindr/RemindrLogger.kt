package com.zenhealth.remindr


import android.util.Log

object RemindrLogger {
    private const val TAG = "RemindrLogger"

    var isLoggingEnabled: Boolean = false

    fun d(message: String) {
        if (isLoggingEnabled) Log.d(TAG, message)
    }

    fun i(message: String) {
        if (isLoggingEnabled) Log.i(TAG, message)
    }

    fun w(message: String) {
        if (isLoggingEnabled) Log.w(TAG, message)
    }
}