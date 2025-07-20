@file:OptIn(ExperimentalTime::class)

package com.zenhealth.remindr.fake

import com.zenhealth.remindr.state.AppState
import com.zenhealth.remindr.storage.ReminderStorage
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

class FakeReminderStorage : ReminderStorage {
    private val triggerTimes = mutableMapOf<String, Instant?>()
    private val triggerCounts = mutableMapOf<String, Int>()

    override suspend fun getLastTriggered(reminderId: String) = triggerTimes[reminderId]
    override suspend fun setLastTriggered(reminderId: String, time: Instant) {
        triggerTimes[reminderId] = time
    }

    override suspend fun getTriggerCount(reminderId: String) = triggerCounts[reminderId] ?: 0
    override suspend fun incrementTriggerCount(reminderId: String) {
        triggerCounts[reminderId] = (triggerCounts[reminderId] ?: 0) + 1
    }

}
