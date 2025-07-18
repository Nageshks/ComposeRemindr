package com.zenhealth.remindr.conditions

import com.zenhealth.remindr.core.*
import kotlin.time.Clock
import kotlin.time.Instant
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

class TimeSinceLastTriggerCondition(
    private val duration: Duration
) : TriggerCondition {
    @OptIn(ExperimentalTime::class)
    override suspend fun shouldTrigger(context: ReminderContext): Boolean {
        val lastTriggered = context.storage.getLastTriggered(context.reminderId)
        return lastTriggered == null || Clock.System.now() - lastTriggered >= duration
    }
    
    @OptIn(ExperimentalTime::class)
    override suspend fun onTriggered(context: ReminderContext) {
        context.storage.setLastTriggered(context.reminderId, Clock.System.now())
    }
}