package com.zenhealth.remindrdemo

import android.content.Context
import com.zenhealth.remindr.state.AppState
import com.zenhealth.remindr.state.DefaultAppState

object AppStateProvider {
    private var instance: AppState? = null

    fun get(context: Context): AppState {
        return instance ?: synchronized(this) {
            instance ?: DefaultAppState(context.applicationContext).also {
                instance = it
            }
        }
    }

    fun override(appState: AppState): AppState {
        instance = appState
        return appState
    }

    fun clear() {
        instance = null
    }
}
