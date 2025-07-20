@file:OptIn(ExperimentalTime::class, ExperimentalCoroutinesApi::class)

package com.zenhealth.remindr.core

import com.zenhealth.remindr.conditions.basic.AlwaysFalseCondition
import com.zenhealth.remindr.conditions.basic.AlwaysTrueCondition
import com.zenhealth.remindr.fake.FakeAppState
import com.zenhealth.remindr.fake.FakeExternalCondition
import com.zenhealth.remindr.fake.FakeReminderStorage
import com.zenhealth.remindr.state.AppState
import com.zenhealth.remindr.storage.ReminderStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.time.ExperimentalTime
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull



class ReminderEngineTest {

    private lateinit var storage: ReminderStorage
    private lateinit var appState: AppState
    private lateinit var engine: ReminderEngine
    private val testScope = CoroutineScope(Dispatchers.Unconfined + Job())

    @Before
    fun setup() {
        storage = FakeReminderStorage()
        appState = FakeAppState()
        engine = ReminderEngine(storage, appState, testScope)
    }

    @Test
    fun `T1 - valid reminder gets selected`() = runTest {
        val reminder = ReminderBuilder()
            .id("valid")
            .addCondition(AlwaysTrueCondition)
            .content { {} }
            .build()

        engine.registerReminder(reminder)
        engine.evaluateReminders()

        assertEquals("valid", engine.currentReminder.value?.id)
    }

    @Test
    fun `T2 - reminder skipped if condition false`() = runTest {
        val reminder = ReminderBuilder()
            .id("invalid")
            .addCondition(AlwaysFalseCondition)
            .content { {} }
            .build()

        engine.registerReminder(reminder)
        engine.evaluateReminders()

        assertNull(engine.currentReminder)
    }

    @Test
    fun `T3 - picks reminder with highest priority (lowest number)`() = runTest {
        val lowPriority = ReminderBuilder()
            .id("low")
            .priority(6)
            .addCondition(AlwaysTrueCondition)
            .content { {} }
            .build()

        val highPriority = ReminderBuilder()
            .id("high")
            .priority(1)
            .addCondition(AlwaysTrueCondition)
            .content { {} }
            .build()

        engine.registerReminder(lowPriority)
        engine.registerReminder(highPriority)

        engine.evaluateReminders()

        assertEquals("high", engine.currentReminder.value?.id)
    }

    @Test
    fun `T4 - external condition not satisfied skips reminder`() = runTest {
        val reminder = ReminderBuilder()
            .id("ext")
            .addCondition(AlwaysTrueCondition)
            .requiresExternalCondition("test-condition")
            .content { {} }
            .build()

        engine.registerReminder(reminder)
        // Don't register external condition → should be false
        engine.evaluateReminders()

        assertNull(engine.currentReminder)
    }

    @Test
    fun `T5 - evaluation skipped if currentReminder exists`() = runTest {
        val initial = ReminderBuilder()
            .id("initial")
            .addCondition(AlwaysTrueCondition)
            .content { {} }
            .build()

        engine.registerReminder(initial)
        engine.evaluateReminders()

        val second = ReminderBuilder()
            .id("second")
            .addCondition(AlwaysTrueCondition)
            .content { {} }
            .build()

        engine.registerReminder(second)
        engine.evaluateReminders()

        // Should still be "initial"
        assertEquals("initial", engine.currentReminder.value?.id)
    }

    @Test
    fun `T6 - clearCurrent resets active reminder`() = runTest {
        val reminder = ReminderBuilder()
            .id("clear-me")
            .addCondition(AlwaysTrueCondition)
            .content { {} }
            .build()

        engine.registerReminder(reminder)
        engine.evaluateReminders()

        engine.clearCurrent()
        assertNull(engine.currentReminder)
    }

    @Test
    fun `T7 - reacts to external condition change`() = runTest {
        val reminder = ReminderBuilder()
            .id("reactive")
            .addCondition(AlwaysTrueCondition)
            .requiresExternalCondition("my-key")
            .content { {} }
            .build()

        val fakeExternalCondition = FakeExternalCondition("my-key", satisfied = false)

        engine.registerReminder(reminder)
        engine.registerExternalCondition("my-key", fakeExternalCondition)

        // Initial evaluation → should be skipped
        engine.evaluateReminders()
        assertNull(engine.currentReminder)

        // Emit change → now it should become active
        fakeExternalCondition.emitChange(true)

        // Wait for engine to react to the emitted change
        advanceUntilIdle() // Needed to process the coroutine flow collector

        assertEquals("reactive", engine.currentReminder.value?.id)
    }

    @Test
    fun `T8 - external condition change doesn't override active reminder`() = runTest {
        val first = ReminderBuilder()
            .id("active-reminder")
            .addCondition(AlwaysTrueCondition)
            .content { {} }
            .build()

        val second = ReminderBuilder()
            .id("waiting-reminder")
            .addCondition(AlwaysTrueCondition)
            .requiresExternalCondition("switch")
            .content { {} }
            .build()

        val fakeExternalCondition = FakeExternalCondition("switch", satisfied = false)

        engine.registerReminder(first)
        engine.registerReminder(second)
        engine.registerExternalCondition("switch", fakeExternalCondition)

        engine.evaluateReminders()
        assertEquals("active-reminder", engine.currentReminder.value?.id)

        // Now simulate external condition becoming true
        fakeExternalCondition.emitChange(true)

        // Wait to process
        advanceUntilIdle()

        // Still should be "active-reminder", not overridden
        assertEquals("active-reminder", engine.currentReminder.value?.id)
    }

    @Test
    fun `T9 - reminder triggered after clearing current and external update`() = runTest {
        val first = ReminderBuilder()
            .id("first")
            .addCondition(AlwaysTrueCondition)
            .content { {} }
            .build()

        val second = ReminderBuilder()
            .id("second")
            .addCondition(AlwaysTrueCondition)
            .requiresExternalCondition("condition")
            .content { {} }
            .build()

        val fakeCondition = FakeExternalCondition("condition", satisfied = false)

        engine.registerReminder(first)
        engine.registerReminder(second)
        engine.registerExternalCondition("condition", fakeCondition)

        engine.evaluateReminders()
        assertEquals("first", engine.currentReminder.value?.id)

        // Clear the current reminder manually
        engine.clearCurrent()
        assertNull(engine.currentReminder)

        // Simulate external condition now becoming true
        fakeCondition.emitChange(true)
        advanceUntilIdle()

        // Now, second reminder should be active
        assertEquals("second", engine.currentReminder.value?.id)
    }

}
