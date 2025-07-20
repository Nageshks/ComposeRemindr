
package com.zenhealth.remindrdemo

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyApplication: Application(){
    override fun onCreate() {
        super.onCreate()
        CoroutineScope(Dispatchers.Default).launch {
            AppStateProvider.override(DemoAppStateImpl(this@MyApplication)).incrementLaunchCount()
        }
    }
}