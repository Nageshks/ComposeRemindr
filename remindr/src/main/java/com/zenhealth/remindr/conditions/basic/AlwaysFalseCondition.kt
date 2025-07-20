package com.zenhealth.remindr.conditions.basic

import com.zenhealth.remindr.core.ReminderContext
import com.zenhealth.remindr.core.TriggerCondition
import com.zenhealth.remindr.utils.RemindrLogger


/**
 * A trigger condition that always returns `false`.
 *
 * This condition will always prevent the associated reminder from being triggered.
 * It is primarily useful for testing scenarios where a reminder should be ignored or filtered out.
 *
 * Example use cases:
 * - Validating reminder evaluation logic (e.g., short-circuiting).
 * - Disabling reminders temporarily without removing them.
 * - Placeholder condition during development or staging.
 */
object AlwaysFalseCondition : TriggerCondition {
    override suspend fun shouldTrigger(context: ReminderContext): Boolean {
        RemindrLogger.d("AlwaysFalseCondition.shouldTrigger() called")
        return false
    }

    override suspend fun onTriggered(context: ReminderContext) {
        RemindrLogger.d("AlwaysFalseCondition.onTriggered() called")
    }
}