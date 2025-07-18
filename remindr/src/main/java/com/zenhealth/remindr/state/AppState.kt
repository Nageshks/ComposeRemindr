package com.zenhealth.remindr.state

import android.app.Activity
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

interface AppState {
    @OptIn(ExperimentalTime::class)
    val installDate: Instant
    val launchCount: Int
    @OptIn(ExperimentalTime::class)
    val lastLaunchTime: Instant
    var currentRoute: String?
    fun getCurrentActivity(): Activity?
}