
package com.zenhealth.remindr.conditions.time

import kotlin.time.Duration.Companion.days

class RepeatEveryXDaysCondition(
    days: Int,
    triggerOnFirstUse: Boolean = true
) : TimeSinceLastTriggerCondition(days.days, triggerOnFirstUse)
