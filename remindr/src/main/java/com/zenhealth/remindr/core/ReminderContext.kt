package com.zenhealth.remindr.core

import com.zenhealth.remindr.state.AppState
import com.zenhealth.remindr.state.SessionState
import com.zenhealth.remindr.storage.ReminderStorage

data class ReminderContext(
    val storage: ReminderStorage,
    val appState: AppState,
    val sessionState: SessionState,
    val reminderId: String,
)