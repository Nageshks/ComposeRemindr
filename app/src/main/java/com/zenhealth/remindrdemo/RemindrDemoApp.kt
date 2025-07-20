package com.zenhealth.remindrdemo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Dialog
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zenhealth.remindr.conditions.session.MinimumAppLaunchCondition
import com.zenhealth.remindr.conditions.time.OneTimeReminderCondition
import com.zenhealth.remindr.conditions.time.RepeatEveryXDaysCondition
import com.zenhealth.remindr.provider.AppStateProvider
import com.zenhealth.remindr.provider.LocalRemindr
import com.zenhealth.remindr.provider.RemindrProvider
import com.zenhealth.remindrdemo.componensts.FeedbackDialog
import com.zenhealth.remindrdemo.componensts.HomeScreen
import com.zenhealth.remindrdemo.componensts.OnboardingScreen
import com.zenhealth.remindrdemo.componensts.ProfileScreen


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
                remindr.buildReminders(this){
                    reminder {
                        id("onboarding")
                        condition {
                            OneTimeReminderCondition()
                        }
                        priority(1)
                        content { onDismiss->
                            OnboardingScreen { onDismiss() }
                        }
                    }
                    reminder {
                        id("feedback_reminder")
                        condition {
                            MinimumAppLaunchCondition(3) and RepeatEveryXDaysCondition(7)
                        }
                        priority(2)
                        content { onDismiss->
                            Dialog(onDismiss) {
                                FeedbackDialog {
                                    onDismiss()
                                }
                            }
                        }
                    }
                }
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
