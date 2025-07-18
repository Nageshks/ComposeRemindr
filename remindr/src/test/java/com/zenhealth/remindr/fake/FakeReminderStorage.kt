@file:OptIn(ExperimentalTime::class)

package com.zenhealth.remindr.fake

import com.zenhealth.remindr.state.AppState
import com.zenhealth.remindr.storage.ReminderStorage
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

class FakeReminderStorage : ReminderStorage {
    private val triggerTimes = mutableMapOf<String, Instant?>()
    private val triggerCounts = mutableMapOf<String, Int>()
    private val flags = mutableMapOf<String, Boolean>()

    override suspend fun getLastTriggered(reminderId: String) = triggerTimes[reminderId]
    override suspend fun setLastTriggered(reminderId: String, time: Instant) {
        triggerTimes[reminderId] = time
    }

    override suspend fun getTriggerCount(reminderId: String) = triggerCounts[reminderId] ?: 0
    override suspend fun incrementTriggerCount(reminderId: String) {
        triggerCounts[reminderId] = (triggerCounts[reminderId] ?: 0) + 1
    }

    override suspend fun getCustomFlag(key: String) = flags[key] ?: false
    override suspend fun setCustomFlag(key: String, value: Boolean) {
        flags[key] = value
    }
}
