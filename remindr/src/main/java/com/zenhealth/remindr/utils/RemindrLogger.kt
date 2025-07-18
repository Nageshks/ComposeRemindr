package com.zenhealth.remindr.utils
import android.util.Log

object RemindrLogger {
    private const val TAG = "RemindrLogger"

    var isLoggingEnabled: Boolean = false

    fun e(message: String, throwable: Throwable? = null) {
        if (isLoggingEnabled) {
            if (throwable != null) {
                Log.e(TAG, message, throwable)
            } else {
                Log.e(TAG, message)
            }
        }
    }

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