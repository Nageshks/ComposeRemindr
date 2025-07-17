package com.zenhealth.remindr

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState


@Composable
fun RemindrLayer(
    navController: NavHostController,
    content: RemindrScope.() -> Unit
) {
    val currentRoute = currentBackStackEntryRoute(navController)
    val context = LocalContext.current.applicationContext
    val state = rememberRemindrState()

    Remindr.remindrState = state
    val dismissedTrigger = remember { mutableStateOf(false) }

    // ❗ Always recreate scope so it's fresh
    val scope = RemindrScope(currentRoute, state, context)
    RemindrLogger.d("🛠️ [RemindrLayer] Creating new RemindrScope for route=$currentRoute")

    // 🔁 Re-run on route change or dismiss
    LaunchedEffect(currentRoute, dismissedTrigger.value) {
        RemindrLogger.d("🧱 [RemindrLayer] Rebuilding DSL content")
        scope.content()
        RemindrLogger.d("🚀 [RemindrLayer] Running trigger evaluation...")
        scope.evaluateTriggers()
        dismissedTrigger.value = false
    }

    RemindrUIHost(
        state = state,
        onDismiss = {
            RemindrLogger.d("❌ [RemindrLayer] Reminder dismissed — triggering reevaluation")
            state.clear()
            dismissedTrigger.value = true
        }
    )
}

@Composable
private fun currentBackStackEntryRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}
