@file:OptIn(ExperimentalTime::class)

package com.zenhealth.remindr.storage

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

interface ReminderStorage {
    suspend fun getLastTriggered(reminderId: String): Instant?
    suspend fun setLastTriggered(reminderId: String, time: Instant)
    suspend fun getTriggerCount(reminderId: String): Int
    suspend fun incrementTriggerCount(reminderId: String)
}