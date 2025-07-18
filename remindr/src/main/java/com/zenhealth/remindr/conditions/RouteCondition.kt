package com.zenhealth.remindr.conditions

import com.zenhealth.remindr.core.ReminderContext
import com.zenhealth.remindr.core.TriggerCondition

class RouteCondition(
    private val route: String
) : TriggerCondition {
    override suspend fun shouldTrigger(context: ReminderContext): Boolean {
        return context.appState.currentRoute == route
    }
    
    override suspend fun onTriggered(context: ReminderContext) = Unit
}