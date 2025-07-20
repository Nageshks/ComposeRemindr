package com.zenhealth.remindr.provider

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext
import com.zenhealth.remindr.state.AppState
import com.zenhealth.remindr.state.DefaultAppState

val LocalAppState = staticCompositionLocalOf<AppState> {
    error("No AppState provided")
}

@Composable
fun AppStateProvider(
    appStateOverride: AppState? = null,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val appState = remember {
        appStateOverride ?: DefaultAppState(context).apply { launchCount++ }
    }

    CompositionLocalProvider(LocalAppState provides appState) {
        content()
    }
}
