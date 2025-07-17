package com.zenhealth.remindrdemo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.zenhealth.remindr.RemindrLayer



@Composable
fun RemindrApp() {
    val navController = rememberNavController()
    Box(Modifier.fillMaxSize()) {
        AppNavigation(navController)
        RemindrLayer(navController = navController) {
            remindOnRoute("home") {
                id("onboarding_fullscreen")
                triggerOnFirstLaunch()
                everyDays(999) // just a safeguard
                priority(1)
                showFullScreen { onDismiss ->
                    OnboardingScreen(onDismiss = onDismiss)
                }
            }

            remindOnRoute("home") {
                id("feedback_popup")
                everyDays(7)
                triggerOnFirstLaunch()
                priority(2)
                showDialog { onDismiss ->
                    FeedbackDialog(onDismiss = onDismiss)
                }
            }

            remindOnRoute("home") {
                id("premium_reminder")
                everyDays(7)
                triggerOnFirstLaunch()
                priority(3)
                showBottomSheet { onDismiss ->
                    UpgradeReminderBottomSheet(
                        onDismiss = onDismiss,
                        onUpgradeClick = {
                            onDismiss()
                        }
                    )
                }
            }
        }
    }
}
