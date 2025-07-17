package com.zenhealth.remindr

data class ReminderDefinition(
    val id: String,
    val frequencyDays: Long,
    val triggerOnFirstLaunch: Boolean,
    val ui: ReminderUI,
    val priority: Int = 0
)
