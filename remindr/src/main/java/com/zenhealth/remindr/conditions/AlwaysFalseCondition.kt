package com.zenhealth.remindr.conditions

import com.zenhealth.remindr.core.ReminderContext
import com.zenhealth.remindr.core.TriggerCondition
import com.zenhealth.remindr.utils.RemindrLogger

object AlwaysFalseCondition : TriggerCondition {
    override suspend fun shouldTrigger(context: ReminderContext): Boolean {
        RemindrLogger.d("AlwaysFalseCondition.shouldTrigger() called")
        return false
    }

    override suspend fun onTriggered(context: ReminderContext) {
        RemindrLogger.d("AlwaysFalseCondition.onTriggered() called")
    }
}