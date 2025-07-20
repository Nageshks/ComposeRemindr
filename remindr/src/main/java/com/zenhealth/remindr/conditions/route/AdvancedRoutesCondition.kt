package com.zenhealth.remindr.conditions.route

import com.zenhealth.remindr.core.ReminderContext
import com.zenhealth.remindr.core.TriggerCondition

/**
 * A flexible condition that triggers when the current route satisfies
 * one of the provided route patterns using the selected [RouteMatchType].
 *
 * Supports:
 * - Exact match (default)
 * - Prefix match (e.g. "settings/")
 * - Regex (e.g. "profile/\\d+")
 * - Optional exclusions (e.g. don’t trigger on "admin")
 *
 * @param patterns Routes to match against (e.g. "home", "profile/\\d+")
 * @param matchType The type of matching to use: exact, prefix, or regex.
 * @param exclude Optional set of routes to explicitly skip, even if matched.
 */

enum class RouteMatchType {
    EXACT,      // Must match the route string exactly
    PREFIX,     // Matches if route starts with given prefix
    REGEX       // Matches if route satisfies the regex pattern
}

class AdvancedRoutesCondition(
    private val patterns: Set<String>,
    private val matchType: RouteMatchType = RouteMatchType.EXACT,
    private val exclude: Set<String> = emptySet()
) : TriggerCondition {

    override suspend fun shouldTrigger(context: ReminderContext): Boolean {
        val route = context.appState.currentRoute ?: return false

        if (route in exclude) return false

        return when (matchType) {
            RouteMatchType.EXACT -> patterns.contains(route)
            RouteMatchType.PREFIX -> patterns.any { route.startsWith(it) }
            RouteMatchType.REGEX -> patterns.any { Regex(it).matches(route) }
        }
    }

    override suspend fun onTriggered(context: ReminderContext) = Unit
}
