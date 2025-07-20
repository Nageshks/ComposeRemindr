@file:OptIn(ExperimentalTime::class)

package com.zenhealth.remindr.conditions.time

import com.zenhealth.remindr.core.ReminderContext
import com.zenhealth.remindr.core.TriggerCondition
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

class DelayAfterInstallCondition(private val delay: Duration) : TriggerCondition {
    override suspend fun shouldTrigger(context: ReminderContext): Boolean {
        return Clock.System.now() - context.appState.installDate >= delay
    }

    override suspend fun onTriggered(context: ReminderContext) = Unit
}
