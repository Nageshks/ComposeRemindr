@file:OptIn(ExperimentalTime::class, ExperimentalCoroutinesApi::class)

package com.zenhealth.remindr.core

import com.zenhealth.remindr.conditions.basic.AlwaysFalseCondition
import com.zenhealth.remindr.conditions.basic.AlwaysTrueCondition
import com.zenhealth.remindr.core.builder.reminder
import com.zenhealth.remindr.fake.FakeAppState
import com.zenhealth.remindr.fake.FakeExternalCondition
import com.zenhealth.remindr.fake.FakeReminderStorage
import com.zenhealth.remindr.state.AppState
import com.zenhealth.remindr.state.SessionState
import com.zenhealth.remindr.storage.ReminderStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import kotlin.time.ExperimentalTime


class ReminderEngineTest {

    private lateinit var storage: ReminderStorage
    private lateinit var appState: AppState
    private lateinit var engine: ReminderEngine
    private val testScope = CoroutineScope(Dispatchers.Unconfined + Job())

    @Before
    fun setup() {
        storage = FakeReminderStorage()
        appState = FakeAppState()
        engine = ReminderEngine(storage, appState, SessionState(), testScope)
    }

    @Test
    fun `T1 - valid reminder gets selected`() = runTest {
        val reminder = reminder {
            id("valid")
            condition { and(AlwaysTrueCondition) }
            content { {} }
        }

        engine.registerReminder(reminder)
        engine.evaluateReminders()

        assertEquals("valid", engine.currentReminder.value?.id)
    }

    @Test
    fun `T2 - reminder skipped if condition false`() = runTest {
        val reminder = reminder {
            id("invalid")
            condition { and(AlwaysFalseCondition) }
            content { {} }
        }

        engine.registerReminder(reminder)
        engine.evaluateReminders()

        assertNull(engine.currentReminder)
    }

    @Test
    fun `T3 - picks reminder with highest priority (lowest number)`() = runTest {
        val lowPriority = reminder {
            id("low")
            priority(6)
            condition { and(AlwaysTrueCondition) }
            content { {} }
        }

        val highPriority = reminder {
            id("high")
            priority(1)
            condition { and(AlwaysTrueCondition) }
            content { {} }
        }

        engine.registerReminder(lowPriority)
        engine.registerReminder(highPriority)
        engine.evaluateReminders()

        assertEquals("high", engine.currentReminder.value?.id)
    }

    @Test
    fun `T4 - external condition not satisfied skips reminder`() = runTest {
        val reminder = reminder {
            id("ext")
            condition { and(AlwaysTrueCondition) }
//            external("test-condition")
            content { {} }
        }

        engine.registerReminder(reminder)
        engine.evaluateReminders()

        assertNull(engine.currentReminder)
    }

    @Test
    fun `T5 - evaluation skipped if currentReminder exists`() = runTest {
        val initial = reminder {
            id("initial")
            condition { and(AlwaysTrueCondition) }
            content { {} }
        }

        engine.registerReminder(initial)
        engine.evaluateReminders()

        val second = reminder {
            id("second")
            condition { and(AlwaysTrueCondition) }
            content { {} }
        }

        engine.registerReminder(second)
        engine.evaluateReminders()

        assertEquals("initial", engine.currentReminder.value?.id)
    }

    @Test
    fun `T6 - clearCurrent resets active reminder`() = runTest {
        val reminder = reminder {
            id("clear-me")
            condition { and(AlwaysTrueCondition) }
            content { {} }
        }

        engine.registerReminder(reminder)
        engine.evaluateReminders()

        engine.clearCurrent()
        assertNull(engine.currentReminder)
    }

    @Test
    fun `T7 - reacts to external condition change`() = runTest {
        val reminder = reminder {
            id("reactive")
            condition { and(AlwaysTrueCondition) }
//            external("my-key")
            content { {} }
        }

        val fakeExternalCondition = FakeExternalCondition("my-key", satisfied = false)

        engine.registerReminder(reminder)
        engine.registerExternalCondition("my-key", fakeExternalCondition)

        engine.evaluateReminders()
        assertNull(engine.currentReminder)

        fakeExternalCondition.emitChange(true)
        advanceUntilIdle()

        assertEquals("reactive", engine.currentReminder.value?.id)
    }

    @Test
    fun `T8 - external condition change doesn't override active reminder`() = runTest {
        val first = reminder {
            id("active-reminder")
            condition { and(AlwaysTrueCondition) }
            content { {} }
        }

        val second = reminder {
            id("waiting-reminder")
            condition { and(AlwaysTrueCondition) }
//            external("switch")
            content { {} }
        }

        val fakeExternalCondition = FakeExternalCondition("switch", satisfied = false)

        engine.registerReminder(first)
        engine.registerReminder(second)
        engine.registerExternalCondition("switch", fakeExternalCondition)

        engine.evaluateReminders()
        assertEquals("active-reminder", engine.currentReminder.value?.id)

        fakeExternalCondition.emitChange(true)
        advanceUntilIdle()

        assertEquals("active-reminder", engine.currentReminder.value?.id)
    }

    @Test
    fun `T9 - reminder triggered after clearing current and external update`() = runTest {
        val first = reminder {
            id("first")
            condition { and(AlwaysTrueCondition) }
            content { {} }
        }

        val second = reminder {
            id("second")
            condition { and(AlwaysTrueCondition) }
//            external("condition")
            content { {} }
        }

        val fakeCondition = FakeExternalCondition("condition", satisfied = false)

        engine.registerReminder(first)
        engine.registerReminder(second)
        engine.registerExternalCondition("condition", fakeCondition)

        engine.evaluateReminders()
        assertEquals("first", engine.currentReminder.value?.id)

        engine.clearCurrent()
        assertNull(engine.currentReminder)

        fakeCondition.emitChange(true)
        advanceUntilIdle()

        assertEquals("second", engine.currentReminder.value?.id)
    }
}
