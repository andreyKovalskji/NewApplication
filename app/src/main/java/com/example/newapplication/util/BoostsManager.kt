package com.example.newapplication.util

import android.app.Activity
import android.content.SharedPreferences

class BoostsManager(private val sharedPreferencesMethod: (String, Int) -> SharedPreferences) {
    private var boostsChanged = false
    private var _sharedPreferences = getSharedPreferences(false)

    private fun getSharedPreferences(needCheck: Boolean = true): SharedPreferences {
        val sharedPreferences = if(!needCheck || boostsChanged) sharedPreferencesMethod(
            SHARED_PREFERENCES_FILE_NAME, Activity.MODE_PRIVATE) else _sharedPreferences
        return if(!needCheck) {
            sharedPreferences
        }
        else if (boostsChanged) sharedPreferences else _sharedPreferences
    }

    private fun editSharedPreferences(editAction: SharedPreferences.Editor.() -> SharedPreferences.Editor) {
        getSharedPreferences().edit().editAction().apply()
        boostsChanged = true
    }

    var luckBoosts
        get() = getSharedPreferences().getInt(LUCK_BOOSTS, 0)
        set(value) {
            editSharedPreferences {
                putInt(LUCK_BOOSTS, value)
            }
        }

    var winBoosts
        get() = getSharedPreferences().getInt(WIN_BOOSTS, 0)
        set(value) {
            editSharedPreferences {
                putInt(WIN_BOOSTS, value)
            }
        }

    companion object {
        private const val SHARED_PREFERENCES_FILE_NAME = "game"
        private const val LUCK_BOOSTS = "luck_boosts"
        private const val WIN_BOOSTS = "win_boosts"
    }
}