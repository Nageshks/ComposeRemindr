package com.zenhealth.remindr.conditions.route

import com.zenhealth.remindr.core.ReminderContext
import com.zenhealth.remindr.core.TriggerCondition

/**
 * A condition that triggers only when the user is on a specific route (screen).
 *
 * This is useful for showing reminders contextually, such as:
 * - Onboarding steps for a particular screen
 * - Tooltips relevant to a feature's location
 * - Prompts tied to specific navigation flows
 *
 * The condition compares the current route from [AppState] with the expected route.
 *
 * @param route The route string that must match [AppState.currentRoute] for the reminder to trigger.
 */
class RouteCondition(
    private val route: String
) : TriggerCondition {
    override suspend fun shouldTrigger(context: ReminderContext): Boolean {
        return context.appState.currentRoute == route
    }
    
    override suspend fun onTriggered(context: ReminderContext) = Unit
}