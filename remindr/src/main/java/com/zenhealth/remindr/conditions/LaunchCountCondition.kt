package com.zenhealth.remindr.conditions

import com.zenhealth.remindr.core.ReminderContext
import com.zenhealth.remindr.core.TriggerCondition

class LaunchCountCondition(
    private val requiredLaunches: Int,
    private val repeat: Boolean = false
) : TriggerCondition {
    override suspend fun shouldTrigger(context: ReminderContext): Boolean {
        val launchCount = context.appState.launchCount
        val triggerCount = context.storage.getTriggerCount(context.reminderId)
        return if (repeat) {
            launchCount / requiredLaunches > triggerCount
        } else {
            val shouldTrigger = launchCount >= requiredLaunches && triggerCount == 0
            shouldTrigger
        }
    }

    override suspend fun onTriggered(context: ReminderContext) {
        context.storage.incrementTriggerCount(context.reminderId)
    }
}