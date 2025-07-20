@file:OptIn(ExperimentalTime::class)

package com.zenhealth.remindr.conditions.time

import com.zenhealth.remindr.core.*
import com.zenhealth.remindr.utils.RemindrLogger
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

open class TimeSinceLastTriggerCondition(
    private val duration: Duration,
    private val triggerOnFirstUse: Boolean = true
) : TriggerCondition {

    override suspend fun shouldTrigger(context: ReminderContext): Boolean {
        val lastTriggered = context.storage.getLastTriggered(context.reminderId)
        val now = Clock.System.now()

        return when {
            lastTriggered == null -> {
                if (triggerOnFirstUse) {
                    RemindrLogger.d("✅ Triggering because it's first time and triggerOnFirstUse = true")
                    true
                } else {
                    RemindrLogger.d("❌ Not triggering: first time but triggerOnFirstUse = false")
                    false
                }
            }

            (now - lastTriggered) >= duration -> {
                RemindrLogger.d("✅ Triggering: ${now - lastTriggered} >= $duration")
                true
            }

            else -> {
                val nextEligible = lastTriggered + duration
                RemindrLogger.d("⏳ Not triggering: Only ${now - lastTriggered} elapsed; needs $duration")
                RemindrLogger.d("➡️ Will be eligible again at: $nextEligible")
                false
            }
        }
    }

    override suspend fun onTriggered(context: ReminderContext) {
        context.storage.setLastTriggered(context.reminderId, Clock.System.now())
    }
}
