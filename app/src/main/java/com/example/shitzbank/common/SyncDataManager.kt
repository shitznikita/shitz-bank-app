package com.example.shitzbank.common

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SyncDataManager @Inject constructor(
    @ApplicationContext context: Context
) {

    private val PREFS_NAME = "shitzbank_sync_prefs"
    private val KEY_LAST_SYNC_TIME = "last_sync_time"

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    fun saveLastSyncTime(dateTime: LocalDateTime) {
        sharedPreferences.edit()
            .putString(KEY_LAST_SYNC_TIME, dateTime.format(formatter))
            .apply()
    }

    fun getLastSyncTime(): LocalDateTime {
        val timeString = sharedPreferences.getString(KEY_LAST_SYNC_TIME, null)
        return timeString?.let {
            LocalDateTime.parse(it, formatter)
        } ?: LocalDateTime.now()
    }

}