package com.zenhealth.remindr.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.zenhealth.remindr.provider.LocalRemindr

@Composable
fun ReminderHost() {
    val remindr = LocalRemindr.current
    val currentReminder by remindr.currentReminder.collectAsState()

    currentReminder?.let { reminder ->
        var isActive by remember(reminder) { mutableStateOf(true) }

        DisposableEffect(reminder) {
            onDispose {
                if (isActive) {
                    remindr.evaluationListener?.onReminderDismissed(reminder)
                    remindr.clearCurrent()
                }
            }
        }

        if (isActive) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(100f)
                    .pointerInput(Unit) { detectTapGestures {} } // Blocks background touches
            ) {
                if (LocalInspectionMode.current) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .background(Color.Red.copy(alpha = 0.3f))
                            .padding(4.dp)
                    ) {
                        Text("REMINDER: ${reminder.id}", color = Color.White)
                    }
                }

                reminder.content {
                    isActive = false
                    remindr.evaluationListener?.onReminderDismissed(reminder)
                    remindr.clearCurrent()
                }
            }
        }
    }
}
