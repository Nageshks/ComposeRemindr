package com.zenhealth.remindrdemo.componensts

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun FeedbackDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("We'd love your feedback!") },
        text = { Text("How are you enjoying the app so far?") },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Sure")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Maybe later")
            }
        }
    )
}
