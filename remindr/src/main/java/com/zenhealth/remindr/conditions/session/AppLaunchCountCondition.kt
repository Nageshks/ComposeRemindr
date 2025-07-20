package com.zenhealth.remindr.conditions.session

import com.zenhealth.remindr.core.ReminderContext
import com.zenhealth.remindr.core.TriggerCondition


/**
 * Triggers the reminder based on the number of times the app has been launched.
 *
 * - When [repeat] is false (default), triggers only once when [requiredLaunches] is reached.
 * - When [repeat] is true, triggers every [requiredLaunches] launches (e.g., on 5th, 10th, 15th, etc.),
 *   but only once per interval, tracked by internal trigger count.
 *
 * This condition uses global app launch count from [AppState], not reminder-specific metrics.
 * Useful for showing prompts like rating requests, feedback forms, etc.
 */

class AppLaunchCountCondition(
    private val requiredLaunches: Int,
    private val repeat: Boolean = false
) : TriggerCondition {
    override suspend fun shouldTrigger(context: ReminderContext): Boolean {
        val launchCount = context.appState.launchCount
        val triggerCount = context.storage.getTriggerCount(context.reminderId)
        return if (repeat) {
            // E.g. if required = 5, trigger on 5, 10, 15...
            launchCount / requiredLaunches > triggerCount
        } else {
            launchCount >= requiredLaunches && triggerCount == 0
        }
    }

    override suspend fun onTriggered(context: ReminderContext) {
        context.storage.incrementTriggerCount(context.reminderId)
    }
}