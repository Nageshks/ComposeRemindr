package com.zenhealth.remindr.core

import com.zenhealth.remindr.listener.ReminderEvaluationListener
import com.zenhealth.remindr.state.AppState
import com.zenhealth.remindr.storage.ReminderStorage
import com.zenhealth.remindr.utils.RemindrLogger
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class ReminderEngine(
    private val storage: ReminderStorage,
    private val appState: AppState,
    private val coroutineScope: CoroutineScope
) {
    private val activeReminders = mutableListOf<Reminder>()

    // Reactive reminder state
    private val _currentReminder = MutableStateFlow<Reminder?>(null)
    val currentReminder: StateFlow<Reminder?> = _currentReminder

    private val externalConditions = mutableMapOf<String, ExternalCondition>()
    private val conditionFlows = mutableListOf<Job>()

    var evaluationListener: ReminderEvaluationListener? = null

    fun registerReminder(reminder: Reminder) {
        activeReminders.add(reminder)
        RemindrLogger.d("📌 Registered reminder: ${reminder.id}")
    }

    fun clearCurrent() {
        _currentReminder.value?.let {
            RemindrLogger.d("🧹 Clearing current reminder: ${it.id}")
        }
        _currentReminder.value = null
        coroutineScope.launch {
            evaluateReminders()
        }
    }

    fun registerExternalCondition(key: String, condition: ExternalCondition) {
        externalConditions[key] = condition
        RemindrLogger.d("🔗 Registered external condition: $key")
        conditionFlows += coroutineScope.launch {
            condition.observeChanges().collect {
                RemindrLogger.d("🔄 External condition changed: $key → Re-evaluating")
                evaluateReminders()
            }
        }
    }

    suspend fun evaluateReminders() {
        evaluationListener?.onEvaluationStarted()

        if (_currentReminder.value != null) {
            RemindrLogger.d("⏸️ Evaluation paused – active reminder: ${_currentReminder.value?.id}")
            evaluationListener?.onEvaluationCompleted(_currentReminder.value)
            return
        }

        val nextReminder = activeReminders
            .sortedBy { it.priority }
            .firstOrNull { reminder ->
                val context = ReminderContext(storage, appState, reminder.id)

                val externalConditionsMet = reminder.externalConditionKeys
                    .all { key -> externalConditions[key]?.isSatisfied() ?: false }

                val conditionsMet = reminder.conditions
                    .all { it.shouldTrigger(context) }

                val allMet = externalConditionsMet && conditionsMet

                evaluationListener?.onReminderEvaluated(reminder, allMet)
                allMet
            }

        nextReminder?.let {
            RemindrLogger.d("✅ Selected next reminder: ${it.id}")
            evaluationListener?.onReminderSelected(it)
        } ?: RemindrLogger.d("🚫 No reminder selected")

        _currentReminder.value = nextReminder
        evaluationListener?.onEvaluationCompleted(nextReminder)
    }

    fun cleanup() {
        conditionFlows.forEach { it.cancel() }
        conditionFlows.clear()
        RemindrLogger.d("🧼 ReminderEngine cleaned up")
    }
}
