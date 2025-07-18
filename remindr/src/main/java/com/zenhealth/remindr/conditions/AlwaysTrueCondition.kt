package com.zenhealth.remindr.conditions

import com.zenhealth.remindr.core.ReminderContext
import com.zenhealth.remindr.core.TriggerCondition
import com.zenhealth.remindr.utils.RemindrLogger

object AlwaysTrueCondition : TriggerCondition {
    override suspend fun shouldTrigger(context: ReminderContext): Boolean {
        RemindrLogger.d("AlwaysTrueCondition.shouldTrigger() called")
        return true
    }

    override suspend fun onTriggered(context: ReminderContext) {
        RemindrLogger.d("AlwaysTrueCondition.onTriggered() called")
    }
}