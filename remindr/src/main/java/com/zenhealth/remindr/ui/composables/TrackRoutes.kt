package com.zenhealth.remindr.ui.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.navigation.NavController
import com.zenhealth.remindr.provider.LocalAppState
import com.zenhealth.remindr.utils.RemindrLogger

@Composable
fun TrackRoutes(navController: NavController) {
    val appState = LocalAppState.current

    DisposableEffect(navController) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            destination.route?.let { route ->
                appState.currentRoute = route
                RemindrLogger.d("📍 Route changed to: $route")
            }
        }

        navController.addOnDestinationChangedListener(listener)

        onDispose {
            navController.removeOnDestinationChangedListener(listener)
        }
    }
}