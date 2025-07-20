package com.zenhealth.remindr.conditions.route

import com.zenhealth.remindr.core.ReminderContext
import com.zenhealth.remindr.core.TriggerCondition

/**
 * A trigger condition that activates when the current route matches
 * **any** of the provided routes.
 *
 * Useful for showing a reminder across multiple screens, for example:
 * - Tooltips relevant on both "home" and "dashboard"
 * - Feedback prompts shown on multiple eligible routes
 *
 * ### Example usage:
 * ```
 * RoutesCondition(setOf("home", "dashboard", "feed"))
 * ```
 *
 * @param routes A set of valid route strings. The condition passes if
 * [AppState.currentRoute] is contained in this set.
 */
class RoutesCondition(
    private val routes: Set<String>
) : TriggerCondition {
    override suspend fun shouldTrigger(context: ReminderContext): Boolean {
        return routes.contains(context.appState.currentRoute)
    }
    
    override suspend fun onTriggered(context: ReminderContext) = Unit
}