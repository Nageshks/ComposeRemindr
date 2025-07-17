package com.zenhealth.remindr

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

class RemindrState {
    private val _uiState = mutableStateOf<ReminderUI>(ReminderUI.None)
    val uiState: State<ReminderUI> get() = _uiState

    fun show(ui: ReminderUI) {
        _uiState.value = ui
    }

    fun clear() {
        _uiState.value = ReminderUI.None
    }

    fun isIdle(): Boolean {
        return _uiState.value == ReminderUI.None
    }
}

@Composable
fun rememberRemindrState(): RemindrState = remember { RemindrState() }
