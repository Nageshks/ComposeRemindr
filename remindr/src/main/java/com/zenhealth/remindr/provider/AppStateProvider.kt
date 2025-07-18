package com.zenhealth.remindr.provider

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext
import com.zenhealth.remindr.state.AppStateImpl
import com.zenhealth.remindr.state.AppState

val LocalAppState = staticCompositionLocalOf<AppState> {
    error("No AppState provided")
}

@Composable
fun AppStateProvider(
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val appState = remember { AppStateImpl(context).apply { launchCount++ } }

    CompositionLocalProvider(LocalAppState provides appState) {
        content()
    }
}