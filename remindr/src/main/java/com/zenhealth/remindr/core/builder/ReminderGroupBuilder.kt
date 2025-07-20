package com.zenhealth.remindr.core.builder

import com.zenhealth.remindr.core.Reminder

class ReminderGroupBuilder {
    private val reminders = mutableListOf<Reminder>()

    fun reminder(block: ReminderDslBuilder.() -> Unit) {
        val reminder = ReminderDslBuilder().apply(block).build()
        reminders.add(reminder)
    }

    fun build(): List<Reminder> = reminders
}
