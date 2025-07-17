package com.zenhealth.remindr

import androidx.compose.runtime.Composable

sealed class ReminderUI {
    data class Dialog(val content: @Composable (onDismiss: () -> Unit) -> Unit) : ReminderUI()
    data class BottomSheet(val content: @Composable (onDismiss: () -> Unit) -> Unit) : ReminderUI()
    data class FullScreen(val content: @Composable (onDismiss: () -> Unit) -> Unit) : ReminderUI()
    data object None : ReminderUI()
}
