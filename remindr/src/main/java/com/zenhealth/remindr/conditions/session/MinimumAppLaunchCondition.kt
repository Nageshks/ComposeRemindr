@file:OptIn(ExperimentalTime::class)

package com.zenhealth.remindr.conditions.session

import com.zenhealth.remindr.core.ReminderContext
import com.zenhealth.remindr.core.TriggerCondition
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 * Triggers once the app has been launched at least [minLaunches] times,
 * and continues triggering on every launch after that.
 *
 * - Ensures only one trigger per launch count (no duplicate triggers per session).
 * - Tracks last launch count used to prevent double firing.
 * - Useful for showing reminders after user shows minimum engagement.
 */
class MinimumAppLaunchCondition(
    private val minLaunches: Int
) : TriggerCondition {

    override suspend fun shouldTrigger(context: ReminderContext): Boolean {
        val launchCount = context.appState.launchCount
        if (launchCount < minLaunches) return false

        val lastTriggered = context.storage.getLastTriggered(context.reminderId)
        val currentLaunch = launchCount

        // Prevent re-triggering on the same launch
        // Store trigger count instead of launch, so we compare launch count against previous trigger
        val triggerCount = context.storage.getTriggerCount(context.reminderId)

        return when {
            lastTriggered == null -> true // never triggered before
            triggerCount < launchCount -> true // not yet triggered for this launch
            else -> false
        }
    }

    override suspend fun onTriggered(context: ReminderContext) {
        context.storage.setLastTriggered(context.reminderId, Clock.System.now())
        context.storage.incrementTriggerCount(context.reminderId)
    }
}