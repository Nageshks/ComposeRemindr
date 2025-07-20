package com.zenhealth.remindr.provider

import android.content.Context
import com.zenhealth.remindr.storage.DataStoreReminderStorage
import com.zenhealth.remindr.storage.ReminderStorage

// ReminderStorageProvider.kt
object ReminderStorageProvider {
    private var instance: ReminderStorage? = null

    fun get(context: Context): ReminderStorage {
        return instance ?: synchronized(this) {
            instance ?: DataStoreReminderStorage(context.applicationContext).also {
                instance = it
            }
        }
    }
}
