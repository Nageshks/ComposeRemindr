package com.zenhealth.remindr.core

import androidx.compose.runtime.Composable
import com.zenhealth.remindr.core.builder.reminder
import kotlinx.coroutines.delay
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ReminderBuilderTest {

    class FakeTriggerCondition(
        private val shouldTriggerResult: Boolean = true,
        private val delayMillis: Long = 0L
    ) : TriggerCondition {

        var triggered = false

        override suspend fun shouldTrigger(context: ReminderContext): Boolean {
            delay(delayMillis) // simulate latency if needed
            return shouldTriggerResult
        }

        override suspend fun onTriggered(context: ReminderContext) {
            triggered = true
        }
    }

    private val testCondition = FakeTriggerCondition()
    private val testContent: @Composable (dismiss: () -> Unit) -> Unit = { _ -> }

    @Test
    fun `build creates Reminder with valid fields`() {
        val reminderId = "test_id"

        val reminder = reminder {
            id(reminderId)
            priority(3)
            condition {
                and(testCondition)
            }
//            external("battery_low")
            content(testContent)
        }

        assertEquals(reminderId, reminder.id)
        assertEquals(1, reminder.conditions.size)
        assertEquals(testCondition, reminder.conditions[0])
        assertEquals(1, reminder.externalConditionKeys.size)
        assertEquals("battery_low", reminder.externalConditionKeys[0])
        assertEquals(3, reminder.priority)
        assertNotNull(reminder.content)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `build throws if no conditions are provided`() {
        reminder {
            id("no_conditions")
            priority(1)
            content(testContent)
        }
    }

    @Test
    fun `builder assigns default ID when not explicitly set`() {
        val reminder = reminder {
            priority(2)
            condition {
                and(testCondition)
            }
            content(testContent)
        }

        assertTrue(reminder.id.isNotBlank())
    }
}
