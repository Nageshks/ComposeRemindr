@file:OptIn(ExperimentalTime::class)

package com.zenhealth.remindr.conditions.time

import com.zenhealth.remindr.core.ReminderContext
import com.zenhealth.remindr.core.TriggerCondition
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 * A trigger condition that activates only the first time this specific reminder is evaluated.
 *
 * This is scoped per `reminderId`, not per app launch. It ensures the reminder is shown only once,
 * typically used for onboarding flows or one-time prompts.
 *
 * Not to be confused with global app launch count — this condition tracks trigger count
 * for the individual reminder via persistent storage.
 */
class OneTimeReminderCondition : TriggerCondition {
    override suspend fun shouldTrigger(context: ReminderContext): Boolean {
        return context.storage.getTriggerCount(context.reminderId) == 0
    }

    @OptIn(ExperimentalTime::class)
    override suspend fun onTriggered(context: ReminderContext) {
        if (context.storage.getTriggerCount(context.reminderId) == 0) {
            context.storage.incrementTriggerCount(context.reminderId)
            context.storage.setLastTriggered(context.reminderId, Clock.System.now())
        }
    }
}
