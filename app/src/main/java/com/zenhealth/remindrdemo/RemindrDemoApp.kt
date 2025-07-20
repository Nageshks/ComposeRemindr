package com.zenhealth.remindrdemo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Dialog
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zenhealth.remindr.conditions.basic.AlwaysFalseCondition
import com.zenhealth.remindr.conditions.time.OneTimeReminderCondition
import com.zenhealth.remindr.conditions.session.AppLaunchCountCondition
import com.zenhealth.remindr.conditions.route.RouteCondition
import com.zenhealth.remindr.conditions.session.MinimumAppLaunchCondition
import com.zenhealth.remindr.conditions.time.RepeatEveryXDaysCondition
import com.zenhealth.remindr.core.Reminder
import com.zenhealth.remindr.core.ReminderBuilder
import com.zenhealth.remindr.provider.AppStateProvider
import com.zenhealth.remindr.provider.LocalRemindr
import com.zenhealth.remindr.provider.RemindrProvider
import com.zenhealth.remindrdemo.componensts.FeedbackDialog
import com.zenhealth.remindrdemo.componensts.HomeScreen
import com.zenhealth.remindrdemo.componensts.OnboardingScreen
import com.zenhealth.remindrdemo.componensts.ProfileScreen


fun buildFeedbackReminder(): Reminder {
    return ReminderBuilder()
        .id("feedback_reminder")
        .addCondition(MinimumAppLaunchCondition(3))
        .addCondition(RepeatEveryXDaysCondition(7))
        .priority(2)
        .content { onDismiss ->
            Dialog(onDismiss) {
                FeedbackDialog {
                    onDismiss()
                }
            }
        }.build()
}

fun buildOnboardingReminder(): Reminder {
    return ReminderBuilder()
        .id("onboarding")
        .addCondition(OneTimeReminderCondition())
        .priority(1)
        .content { onDismiss ->
            OnboardingScreen {
                onDismiss()
            }
        }.build()
}

@Composable
fun ProvideAppState(content: @Composable () -> Unit) {
    val context = LocalContext.current
    val appState = remember {
        AppStateProvider.get(context)
    }

    AppStateProvider(appStateOverride = appState) {
        content()
    }
}


@Composable
fun RemindrDemoApp() {
    val navController = rememberNavController()
    ProvideAppState {
        RemindrProvider(navController) {
            val remindr = LocalRemindr.current
            LaunchedEffect(Unit) {
                remindr.registerReminder(buildOnboardingReminder())
                remindr.registerReminder(buildFeedbackReminder())
                remindr.evaluateReminders()
            }
            NavHost(
                navController = navController,
                startDestination = "home"
            ) {
                composable("home") {
                    HomeScreen(onPressProfile = {
                        navController.navigate("profile")
                    })
                }
                composable("profile") {
                    ProfileScreen(onPressHome = {
                        navController.navigate("home")
                    })
                }
            }
        }
    }
}
