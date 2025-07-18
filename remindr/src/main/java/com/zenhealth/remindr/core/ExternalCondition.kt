package com.zenhealth.remindr.core

import kotlinx.coroutines.flow.Flow

interface ExternalCondition {
    val key: String
    suspend fun isSatisfied(): Boolean
    fun observeChanges(): Flow<Boolean>
}
