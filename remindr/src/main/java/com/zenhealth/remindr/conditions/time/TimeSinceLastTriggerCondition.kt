@file:OptIn(ExperimentalTime::class)

package com.zenhealth.remindr.conditions.time

import com.zenhealth.remindr.core.*
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

open class TimeSinceLastTriggerCondition(
    private val duration: Duration,
    private val triggerOnFirstUse: Boolean = true
) : TriggerCondition {

    override suspend fun shouldTrigger(context: ReminderContext): Boolean {
        val lastTriggered = context.storage.getLastTriggered(context.reminderId)
        return when {
            lastTriggered == null -> triggerOnFirstUse
            else -> (Clock.System.now() - lastTriggered) >= duration
        }
    }

    override suspend fun onTriggered(context: ReminderContext) {
        context.storage.setLastTriggered(context.reminderId, Clock.System.now())
    }
}
