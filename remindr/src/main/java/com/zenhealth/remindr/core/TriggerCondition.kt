package com.zenhealth.remindr.core

interface TriggerCondition {
    suspend fun shouldTrigger(context: ReminderContext): Boolean
    suspend fun onTriggered(context: ReminderContext)
}