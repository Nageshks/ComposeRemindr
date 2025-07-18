package com.zenhealth.remindr.fake

import com.zenhealth.remindr.core.ExternalCondition
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class FakeExternalCondition(
    override val key: String,
    private var satisfied: Boolean = true
) : ExternalCondition {

    private val _changes = MutableSharedFlow<Boolean>(replay = 1)

    override suspend fun isSatisfied(): Boolean = satisfied

    override fun observeChanges(): Flow<Boolean> = _changes.asSharedFlow()

    fun emitChange(newValue: Boolean) {
        satisfied = newValue
        _changes.tryEmit(newValue)
    }
}
