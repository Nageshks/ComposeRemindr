package com.zenhealth.remindr.conditions.logic

import com.zenhealth.remindr.core.ReminderContext
import com.zenhealth.remindr.core.TriggerCondition

sealed class CompositeCondition : TriggerCondition {

    class And(private val children: List<TriggerCondition>) : CompositeCondition() {
        override suspend fun shouldTrigger(context: ReminderContext): Boolean =
            children.all { it.shouldTrigger(context) }

        override suspend fun onTriggered(context: ReminderContext) {
            children.forEach { it.onTriggered(context) }
        }
    }

    class Or(private val children: List<TriggerCondition>) : CompositeCondition() {
        override suspend fun shouldTrigger(context: ReminderContext): Boolean =
            children.any { it.shouldTrigger(context) }

        override suspend fun onTriggered(context: ReminderContext) {
            // ✅ Only trigger the ones that returned true
            children.forEach {
                if (it.shouldTrigger(context)) {
                    it.onTriggered(context)
                }
            }
        }
    }
}
