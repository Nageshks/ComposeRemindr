package com.zenhealth.remindr.ui.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import com.zenhealth.remindr.provider.LocalAppState
import com.zenhealth.remindr.provider.LocalRemindr
import com.zenhealth.remindr.utils.RemindrLogger
import kotlinx.coroutines.launch

@Composable
fun TrackRoutes(navController: NavController) {
    val appState = LocalAppState.current
    val remindr = LocalRemindr.current
    val scope = rememberCoroutineScope()
    DisposableEffect(navController) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            destination.route?.let { route ->
                appState.currentRoute = route
                scope.launch {
                    remindr.evaluateReminders()
                }
                RemindrLogger.d("📍 Route changed to: $route")
            }
        }

        navController.addOnDestinationChangedListener(listener)

        onDispose {
            navController.removeOnDestinationChangedListener(listener)
        }
    }
}