@file:OptIn(ExperimentalTime::class)

package com.zenhealth.remindrdemo

import android.app.Application
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import androidx.core.content.edit

class MyApplication: Application(){
    override fun onCreate() {
        super.onCreate()
        val prefs = getSharedPreferences("remindr_app_state", MODE_PRIVATE)
        if (!prefs.contains("install_date")) {
            prefs.edit { putLong("install_date", Clock.System.now().toEpochMilliseconds()) }
        }
    }
}