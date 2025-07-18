package com.zenhealth.remindr.fake

import android.app.Activity
import com.zenhealth.remindr.state.AppState
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@ExperimentalTime
class FakeAppState : AppState {
    override val installDate: Instant = Instant.DISTANT_PAST
    override val launchCount: Int = 5
    override val lastLaunchTime: Instant = Instant.DISTANT_PAST
    override var currentRoute: String? = "home"
    override fun getCurrentActivity(): Activity? = null
}
