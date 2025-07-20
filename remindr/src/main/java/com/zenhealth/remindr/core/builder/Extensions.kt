package com.zenhealth.remindr.core.builder

import com.zenhealth.remindr.core.Reminder

fun reminder(block: ReminderDslBuilder.() -> Unit): Reminder {
    return ReminderDslBuilder().apply(block).build()
}

fun buildReminders(block: ReminderGroupBuilder.() -> Unit): List<Reminder> {
    return ReminderGroupBuilder().apply(block).build()
}
