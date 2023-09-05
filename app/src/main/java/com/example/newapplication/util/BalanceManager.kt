package com.example.newapplication.util

import android.app.Activity
import android.content.SharedPreferences

class BalanceManager (private val sharedPreferences: (String, Int) -> SharedPreferences) {

    private fun getSharedPreferences(): SharedPreferences {
        return sharedPreferences(SHARED_PREFERENCES_FILE_NAME, Activity.MODE_PRIVATE)
    }

    var balance
        get() = getSharedPreferences().getInt(BALANCE, 2000)
        set(value) = getSharedPreferences().edit().putInt(BALANCE, value).apply()

    companion object {
        private const val SHARED_PREFERENCES_FILE_NAME = "game"
        private const val BALANCE = "balance"
    }
}