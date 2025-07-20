@file:OptIn(ExperimentalTime::class)

package com.zenhealth.remindr.state

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.core.content.edit
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

class DefaultAppState(
    private val context: Context
) : AppState {
    private val prefs = context.getSharedPreferences("remindr_app_state", Context.MODE_PRIVATE)

    override val installDate: Instant
        get() {
            val key = "install_date"
            val now = Clock.System.now().toEpochMilliseconds()
            val stored = prefs.getLong(key, -1)
            if (stored == -1L) {
                prefs.edit { putLong(key, now) }
                return Instant.fromEpochMilliseconds(now)
            }
            return Instant.fromEpochMilliseconds(stored)
        }

    override var launchCount: Int
        get() = prefs.getInt("launch_count", 0)
        set(value) = prefs.edit { putInt("launch_count", value) }

    override var lastLaunchTime: Instant
        get() = Instant.fromEpochMilliseconds(
            prefs.getLong("last_launch_time", Clock.System.now().toEpochMilliseconds())
        )
        set(value) = prefs.edit { putLong("last_launch_time", value.toEpochMilliseconds()) }

    override var currentRoute: String? = null

    override fun getCurrentActivity(): Activity? {
        return (context as? Activity) ?: context.findActivity()
    }

    override suspend fun incrementLaunchCount() {
        prefs.edit { putInt("launch_count", launchCount + 1) }
    }

    private fun Context.findActivity(): Activity? = when (this) {
        is Activity -> this
        is ContextWrapper -> baseContext.findActivity()
        else -> null
    }
}
