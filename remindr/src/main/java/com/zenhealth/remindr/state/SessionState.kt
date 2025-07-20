package com.zenhealth.remindr.state

class SessionState {
    private val triggeredReminders = mutableSetOf<String>()

    fun hasTriggeredInSession(reminderId: String): Boolean {
        return triggeredReminders.contains(reminderId)
    }

    fun markTriggered(reminderId: String) {
        triggeredReminders.add(reminderId)
    }

    fun reset() {
        triggeredReminders.clear()
    }
}
