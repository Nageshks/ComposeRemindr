package com.zenhealth.remindr.core.builder

import androidx.compose.runtime.Composable
import com.zenhealth.remindr.conditions.basic.AlwaysFalseCondition
import com.zenhealth.remindr.core.Reminder
import com.zenhealth.remindr.core.TriggerCondition

class ReminderDslBuilder {
    private var reminderId: String = ""
    private var priority: Int = 0
    private val conditions = mutableListOf<TriggerCondition>()
    private val externalConditionKeys = mutableListOf<String>()
    private var content: (@Composable (onDismiss: () -> Unit) -> Unit)? = null

    fun id(id: String) {
        reminderId = id
    }

    fun priority(value: Int) {
        priority = value
    }

    fun condition(block: ConditionBuilder.() -> TriggerCondition) {
        val built = ConditionBuilder().block()
        conditions.add(built)
    }

    fun externalConditionKey(key: String) {
        externalConditionKeys.add(key)
    }

    fun externalConditionKeys(vararg keys: String) {
        externalConditionKeys.addAll(keys)
    }

    fun content(block: @Composable (onDismiss: () -> Unit) -> Unit) {
        content = block
    }

    fun build(): Reminder {
        return Reminder(
            id = reminderId,
            conditions = conditions.toList(),
            externalConditionKeys = externalConditionKeys.toList(),
            content = content ?: error("Reminder content not provided"),
            priority = priority
        )
    }
}
