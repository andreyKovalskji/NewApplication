package com.example.newapplication.util

import android.app.Activity
import android.content.SharedPreferences
import android.util.Log

class ButtonsColorManager(private val sharedPreferencesMethod: (String, Int) -> SharedPreferences) {
    private var colorChanged = false
    private var _sharedPreferences = getSharedPreferences(false)

    private fun getSharedPreferences(needCheck: Boolean = true): SharedPreferences {
        val sharedPreferences = if(!needCheck || colorChanged) sharedPreferencesMethod(
            SHARED_PREFERENCES_FILE_NAME, Activity.MODE_PRIVATE) else _sharedPreferences
        return if(!needCheck) {
            sharedPreferences
        }
        else if (colorChanged) sharedPreferences else _sharedPreferences

    }

    val selectedButtonsColor
        get() = getSharedPreferences().getString(BUTTONS_COLOR, COLOR_DEFAULT)



    companion object {
        private const val SHARED_PREFERENCES_FILE_NAME = "game"
        private const val BUTTONS_COLOR = "buttons_color"
        const val COLOR_DEFAULT = ""
        const val COLOR_RED = "red"
        const val COLOR_GREEN = "green"
        const val COLOR_BLUE = "blue"
    }
}