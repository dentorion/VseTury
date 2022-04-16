package com.entin.core.util

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TravelCitiesStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    /**
     * Get cities
     */
    val travelCities: Flow<Pair<Int, Int>> = context.dataStore.data.map { preferences ->
        Pair(
            preferences[CITY_FROM] ?: DEFAULT_POSITION,
            preferences[CITY_TO] ?: DEFAULT_POSITION
        )
    }.flowOn(Dispatchers.IO)

    /**
     * Save date From
     */
    suspend fun setCityFromPosition(newPosition: Int) {
        context.dataStore.edit { settings ->
            settings[CITY_FROM] = newPosition
        }
    }

    /**
     * Save date To
     */
    suspend fun setCityToPosition(newPosition: Int) {
        context.dataStore.edit { settings ->
            settings[CITY_TO] = newPosition
        }
    }

    companion object {
        private val CITY_FROM = intPreferencesKey("city_from")
        private val CITY_TO = intPreferencesKey("city_to")
        private const val DEFAULT_POSITION = 1
    }
}