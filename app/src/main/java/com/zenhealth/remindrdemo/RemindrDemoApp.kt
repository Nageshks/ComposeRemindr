
package com.zenhealth.remindrdemo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.window.Dialog
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zenhealth.remindr.conditions.AlwaysFalseCondition
import com.zenhealth.remindr.conditions.AlwaysTrueCondition
import com.zenhealth.remindr.conditions.FirstLaunchCondition
import com.zenhealth.remindr.conditions.LaunchCountCondition
import com.zenhealth.remindr.core.ReminderBuilder
import com.zenhealth.remindr.provider.AppStateProvider
import com.zenhealth.remindr.provider.LocalRemindr
import com.zenhealth.remindr.provider.RemindrProvider
import com.zenhealth.remindrdemo.componensts.FeedbackDialog
import com.zenhealth.remindrdemo.componensts.HomeScreen
import com.zenhealth.remindrdemo.componensts.OnboardingScreen


@Composable
fun RemindrDemoApp() {
    val navController = rememberNavController()
    AppStateProvider {
        RemindrProvider {
            val remindr = LocalRemindr.current
            LaunchedEffect(Unit) {
                remindr.registerReminder(
                    ReminderBuilder()
                    .id("onboarding")
                    .addCondition(AlwaysFalseCondition)
                    .priority(1)
                    .content { onDismiss ->
                        OnboardingScreen {
                            onDismiss()
                        }
                    }.build()
                )
                remindr.registerReminder(
                    ReminderBuilder()
                        .id("feedback_reminder")
                        .addCondition(LaunchCountCondition(3))
                        .priority(2)
                        .content { onDismiss ->
                            Dialog(onDismiss) {
                                FeedbackDialog {
                                    onDismiss()
                                }
                            }
                        }.build()
                )
                remindr.evaluateReminders()
            }
            NavHost(
                navController = navController,
                startDestination = "home"
            ) {
                composable("home") {
                    HomeScreen()
                }
            }
        }
    }
}
