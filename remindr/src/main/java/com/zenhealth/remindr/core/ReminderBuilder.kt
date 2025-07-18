package com.zenhealth.remindr.core

import androidx.compose.runtime.Composable
import java.util.*

class ReminderBuilder {
    private val conditions = mutableListOf<TriggerCondition>()
    private val externalConditionKeys = mutableListOf<String>()
    private var content: @Composable (dismiss: () -> Unit) -> Unit = {}
    private var priority: Int = 0
    private var id: String = UUID.randomUUID().toString()

    fun requiresExternalCondition(key: String) = apply {
        externalConditionKeys.add(key)
    }

    fun id(id: String) = apply { this.id = id }
    fun addCondition(condition: TriggerCondition) = apply { conditions.add(condition) }
    fun content(block: @Composable (dismiss: () -> Unit) -> Unit) = apply { content = block }
    fun priority(level: Int) = apply { priority = level }
    
    fun build(): Reminder {
        require(conditions.isNotEmpty()) { "At least one condition must be specified" }
        return Reminder(
            id = id,
            conditions = conditions,
            content = content,
            externalConditionKeys = externalConditionKeys,
            priority = priority
        )
    }
}