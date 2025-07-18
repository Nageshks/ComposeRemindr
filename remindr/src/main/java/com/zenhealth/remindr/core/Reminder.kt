package com.zenhealth.remindr.core

import androidx.compose.runtime.Composable

data class Reminder(
    val id: String,
    val conditions: List<TriggerCondition>,
    val externalConditionKeys: List<String>,
    val content: @Composable (dismiss: () -> Unit) -> Unit,
    val priority: Int
)