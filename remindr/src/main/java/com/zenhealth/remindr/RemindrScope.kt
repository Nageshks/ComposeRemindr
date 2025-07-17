package com.zenhealth.remindr

import android.content.Context
import kotlinx.coroutines.delay
import java.util.Date

class RemindrScope(
    private val currentRoute: String?,
    private val state: RemindrState,
    private val context: Context
) {
    private val reminders = mutableListOf<ReminderDefinition>()

    fun remindOnRoute(
        route: String,
        block: ReminderBuilder.() -> Unit
    ) {
        if (route != currentRoute) return
        val builder = ReminderBuilder().apply(block)
        val reminder = builder.build()
        RemindrLogger.d("🧠 Built reminder: id=${reminder.id}, frequency=${reminder.frequencyDays} days")
        reminders += reminder
    }

    suspend fun evaluateTriggers() {
        RemindrLogger.d("🧮 [RemindrScope] Starting evaluation of ${reminders.size} reminders")
        val sortedReminders = reminders.sortedBy { it.priority }

        for (reminder in sortedReminders) {
            val lastShown = RemindrStorage.getLastShown(context, reminder.id)
            val now = System.currentTimeMillis()
            val due = lastShown == null || now - lastShown >= reminder.frequencyDays * 24 * 60 * 60 * 1000L

            RemindrLogger.d("Evaluating '${reminder.id}': lastShown=${lastShown?.let { Date(it) }}, now=${Date(now)}, due=$due")

            if (due) {
                RemindrLogger.i("✅ Triggering reminder '${reminder.id}'")
                state.show(reminder.ui)
                RemindrLogger.d("⏳ Waiting for reminder '${reminder.id}' to be dismissed...")
                while (!state.isIdle()) {
                    delay(200)
                }
                // 👉 Only mark as shown after dismissal
                RemindrStorage.setLastShown(context, reminder.id, System.currentTimeMillis())
                RemindrLogger.d("✅ Reminder '${reminder.id}' dismissed. Continuing evaluation...")
            } else {
                val millisRemaining = reminder.frequencyDays * 24 * 60 * 60 * 1000L - (now - (lastShown ?: 0))
                val daysRemaining = millisRemaining / (1000 * 60 * 60 * 24)
                RemindrLogger.i("⏳ Skipping '${reminder.id}' – not due yet ($daysRemaining days remaining)")
            }
        }
    }
    fun getRegisteredReminderIds(): List<String> = reminders.map { it.id }
}
