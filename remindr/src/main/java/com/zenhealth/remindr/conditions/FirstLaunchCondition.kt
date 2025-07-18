package com.zenhealth.remindr.conditions

import com.zenhealth.remindr.core.ReminderContext
import com.zenhealth.remindr.core.TriggerCondition
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class FirstLaunchCondition : TriggerCondition {

    override suspend fun shouldTrigger(context: ReminderContext): Boolean {
        // Check if this is the first launch for this specific reminder
        return context.storage.getTriggerCount(context.reminderId) == 0
    }

    @OptIn(ExperimentalTime::class)
    override suspend fun onTriggered(context: ReminderContext) {
        // Mark that this reminder has been triggered at least once
        context.storage.incrementTriggerCount(context.reminderId)

        // Optional: Also store first trigger timestamp if needed
        context.storage.setLastTriggered(context.reminderId, Clock.System.now())
    }
}