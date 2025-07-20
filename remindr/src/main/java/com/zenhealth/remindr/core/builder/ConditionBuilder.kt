package com.zenhealth.remindr.core.builder

import com.zenhealth.remindr.conditions.logic.CompositeCondition
import com.zenhealth.remindr.core.TriggerCondition

class ConditionBuilder {
    infix fun TriggerCondition.and(other: TriggerCondition): TriggerCondition =
        CompositeCondition.And(listOf(this, other))

    infix fun TriggerCondition.or(other: TriggerCondition): TriggerCondition =
        CompositeCondition.Or(listOf(this, other))

    fun and(vararg conditions: TriggerCondition): TriggerCondition =
        CompositeCondition.And(conditions.toList())

    fun or(vararg conditions: TriggerCondition): TriggerCondition =
        CompositeCondition.Or(conditions.toList())
}
