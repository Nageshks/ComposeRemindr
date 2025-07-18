@file:OptIn(ExperimentalTime::class)

package com.zenhealth.remindr.conditions

import com.zenhealth.remindr.core.ReminderContext
import com.zenhealth.remindr.core.TriggerCondition
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Duration.Companion.days

class TimeCondition(
    private val days: Int,
    private val requireAllConditions: Boolean = true
) : TriggerCondition {

    override suspend fun shouldTrigger(context: ReminderContext): Boolean {
        val lastShown = context.storage.getLastTriggered(context.reminderId)
        val now = Clock.System.now()

        return when {
            lastShown == null -> true
            else -> {
                val timePassed = now - lastShown
                val requiredDuration = days.days
                timePassed >= requiredDuration
            }
        }
    }

    override suspend fun onTriggered(context: ReminderContext) {
        context.storage.setLastTriggered(
            context.reminderId,
            Clock.System.now()
        )
    }
}