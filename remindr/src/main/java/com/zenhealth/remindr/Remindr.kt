package com.zenhealth.remindr

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.zenhealth.remindr.core.ReminderEngine
import com.zenhealth.remindr.provider.LocalAppState
import com.zenhealth.remindr.provider.ReminderStorageProvider
import com.zenhealth.remindr.state.SessionState

object Remindr {
    private var remindrInstance: ReminderEngine? = null

    @Composable
    fun rememberRemindrInstance(): ReminderEngine {
        val context = LocalContext.current
        val coroutineScope = rememberCoroutineScope()
        val appState = LocalAppState.current
        val session = remember { SessionState() }
        return remember(context, coroutineScope, appState) {
            ReminderEngine(
                storage = ReminderStorageProvider.get(context),
                appState = appState,
                sessionState = session,
                coroutineScope = coroutineScope
            ).also {
                remindrInstance = it
            }
        }
    }
}