package com.zenhealth.remindr

import androidx.compose.runtime.Composable
import java.util.UUID

class ReminderBuilder {
    private var id: String = UUID.randomUUID().toString()
    private var frequencyDays: Long = 7
    private var triggerOnFirstLaunch: Boolean = false
    private var ui: ReminderUI = ReminderUI.None
    private var priority: Int = 0

    fun priority(level: Int) {
        this.priority = level
    }

    fun everyDays(days: Long) {
        frequencyDays = days
    }

    fun id(value: String) {
        id = value
    }

    fun triggerOnFirstLaunch(enabled: Boolean = true) {
        triggerOnFirstLaunch = enabled
    }

    fun showDialog(content: @Composable (onDismiss: () -> Unit) -> Unit) {
        ui = ReminderUI.Dialog {
            content {
                Remindr.remindrState?.clear()
            }
        }
    }

    fun showBottomSheet(content: @Composable (onDismiss: () -> Unit) -> Unit) {
        ui = ReminderUI.BottomSheet{
            content{
                Remindr.remindrState?.clear()
            }
        }
    }

    fun showFullScreen(content: @Composable (onDismiss: () -> Unit) -> Unit) {
        ui = ReminderUI.FullScreen {
            content {
                Remindr.remindrState?.clear()
            }
        }
    }

    fun build(): ReminderDefinition {
        return ReminderDefinition(
            id = id,
            frequencyDays = frequencyDays,
            triggerOnFirstLaunch = triggerOnFirstLaunch,
            ui = ui
        )
    }
}
