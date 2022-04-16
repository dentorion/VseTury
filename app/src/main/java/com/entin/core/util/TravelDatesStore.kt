package com.entin.core.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "store")

@Singleton
class TravelDatesStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    /**
     * Get dates
     */
    val travelDates: Flow<Pair<Long, Long>> = context.dataStore.data.map { preferences ->
        Pair(
            preferences[DATE_FROM] ?: Calendar.getInstance().also {
                it.set(
                    Calendar.getInstance().get(Calendar.YEAR),
                    Calendar.getInstance().get(Calendar.MONTH),
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
                    0, 0, 0
                )
            }.time.time,

            preferences[DATE_TO] ?: Calendar.getInstance().also {
                it.set(
                    Calendar.getInstance().get(Calendar.YEAR),
                    Calendar.getInstance().get(Calendar.MONTH),
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
                    HOURS_23, 0, 0
                )
                it.add(Calendar.DATE, DAYS_7)
            }.time.time
        )
    }.flowOn(Dispatchers.IO)

    /**
     * Save date From
     */
    suspend fun setDateFrom(newDate: Long) {
        context.dataStore.edit { settings ->
            settings[DATE_FROM] = newDate
        }
    }

    /**
     * Save date To
     */
    suspend fun setDateTo(newDate: Long) {
        context.dataStore.edit { settings ->
            settings[DATE_TO] = newDate
        }
    }

    companion object {
        private val DATE_FROM = longPreferencesKey("date_from")
        private val DATE_TO = longPreferencesKey("date_to")
        private const val DAYS_7 = 7
        private const val HOURS_23 = 23
    }
}