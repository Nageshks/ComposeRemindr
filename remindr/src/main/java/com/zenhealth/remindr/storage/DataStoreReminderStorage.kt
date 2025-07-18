@file:OptIn(ExperimentalTime::class)

package com.zenhealth.remindr.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

class DataStoreReminderStorage(
    private val context: Context
) : ReminderStorage {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "remindr_storage")
    
    @OptIn(ExperimentalTime::class)
    override suspend fun getLastTriggered(reminderId: String): Instant? {
        return context.dataStore.data
            .map { prefs ->
                prefs[longPreferencesKey("last_triggered_$reminderId")]?.let {
                    Instant.fromEpochMilliseconds(it)
                }
            }
            .first()
    }
    
    override suspend fun setLastTriggered(reminderId: String, time: Instant) {
        context.dataStore.edit { prefs ->
            prefs[longPreferencesKey("last_triggered_$reminderId")] = time.toEpochMilliseconds()
        }
    }
    
    override suspend fun getTriggerCount(reminderId: String): Int {
        return context.dataStore.data
            .map { prefs ->
                prefs[intPreferencesKey("trigger_count_$reminderId")] ?: 0
            }
            .first()
    }
    
    override suspend fun incrementTriggerCount(reminderId: String) {
        context.dataStore.edit { prefs ->
            val current = prefs[intPreferencesKey("trigger_count_$reminderId")] ?: 0
            prefs[intPreferencesKey("trigger_count_$reminderId")] = current + 1
        }
    }
    
    override suspend fun getCustomFlag(key: String): Boolean {
        return context.dataStore.data
            .map { prefs ->
                prefs[booleanPreferencesKey(key)] ?: false
            }
            .first()
    }
    
    override suspend fun setCustomFlag(key: String, value: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[booleanPreferencesKey(key)] = value
        }
    }
}