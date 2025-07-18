package com.zenhealth.remindr.listener

import com.zenhealth.remindr.core.Reminder

interface ReminderEvaluationListener {
    fun onEvaluationStarted()
    fun onReminderEvaluated(reminder: Reminder, passed: Boolean)
    fun onReminderSelected(reminder: Reminder)
    fun onReminderDismissed(reminder: Reminder)
    fun onEvaluationCompleted(selectedReminder: Reminder?)
}
