package com.zenhealth.remindr.conditions.session

import com.zenhealth.remindr.core.ReminderContext
import com.zenhealth.remindr.core.TriggerCondition

class OncePerSessionCondition : TriggerCondition {

    override suspend fun shouldTrigger(context: ReminderContext): Boolean {
        return !context.sessionState.hasTriggeredInSession(context.reminderId)
    }

    override suspend fun onTriggered(context: ReminderContext) {
        context.sessionState.markTriggered(context.reminderId)
    }
}
