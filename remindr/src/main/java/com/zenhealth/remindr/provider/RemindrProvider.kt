
package com.zenhealth.remindr.provider

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavController
import com.zenhealth.remindr.Remindr.rememberRemindrInstance
import com.zenhealth.remindr.core.ReminderEngine
import com.zenhealth.remindr.ui.composables.ReminderHost
import com.zenhealth.remindr.ui.composables.TrackRoutes


val LocalRemindr = staticCompositionLocalOf<ReminderEngine> {
    error("No ReminderEngine provided. Wrap your app with Remindr.Provider")
}

@Composable
fun RemindrProvider(
    navController: NavController? = null,
    content: @Composable () -> Unit
) {
    val remindr = rememberRemindrInstance()
    CompositionLocalProvider(LocalRemindr provides remindr) {
        navController?.let { TrackRoutes(it) }
        content()
        ReminderHost()
    }
}