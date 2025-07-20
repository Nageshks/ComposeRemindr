package com.zenhealth.remindr.conditions.basic

import com.zenhealth.remindr.core.ReminderContext
import com.zenhealth.remindr.core.TriggerCondition
import com.zenhealth.remindr.utils.RemindrLogger


/**
 * A trigger condition that always returns `true`.
 *
 * This condition will always allow the associated reminder to be triggered,
 * regardless of app state, route, or any external factors.
 *
 * Useful for testing, default reminders, or unconditional UI display.
 *
 * Example use cases:
 * - Verifying reminder rendering and dismissal flow.
 * - Development-only reminders.
 * - Static onboarding tips or help overlays shown immediately.
 */
object AlwaysTrueCondition : TriggerCondition {
    override suspend fun shouldTrigger(context: ReminderContext): Boolean {
        RemindrLogger.d("AlwaysTrueCondition.shouldTrigger() called")
        return true
    }

    override suspend fun onTriggered(context: ReminderContext) {
        RemindrLogger.d("AlwaysTrueCondition.onTriggered() called")
    }
}