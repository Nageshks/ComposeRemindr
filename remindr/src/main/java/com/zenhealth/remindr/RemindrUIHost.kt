package com.zenhealth.remindr

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemindrUIHost(
    state: RemindrState,
    onDismiss: () -> Unit
) {
    when (val ui = state.uiState.value) {
        is ReminderUI.Dialog -> {
            Dialog(onDismissRequest = {
                RemindrLogger.d("❌ [RemindrUIHost] Dialog dismissed via system")
                onDismiss()
            }) {
                ui.content(onDismiss)
            }
        }

        is ReminderUI.BottomSheet -> {
            ModalBottomSheet(onDismissRequest = {
                RemindrLogger.d("❌ [RemindrUIHost] BottomSheet dismissed")
                onDismiss()
            }) {
                ui.content(onDismiss)
            }
        }

        is ReminderUI.FullScreen -> {
            Box(modifier = Modifier.fillMaxSize()) {
                ui.content {
                    RemindrLogger.d("❌ [RemindrUIHost] FullScreen dismissed via button")
                    onDismiss()
                }
            }
        }

        ReminderUI.None -> {
            RemindrLogger.d("📭 [RemindrUIHost] No UI to show")
        }
    }
}

