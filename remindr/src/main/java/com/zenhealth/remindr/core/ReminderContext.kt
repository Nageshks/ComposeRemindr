package com.zenhealth.remindr.core

import com.zenhealth.remindr.state.AppState
import com.zenhealth.remindr.storage.ReminderStorage

data class ReminderContext(
    val storage: ReminderStorage,
    val appState: AppState,
    val reminderId: String
)