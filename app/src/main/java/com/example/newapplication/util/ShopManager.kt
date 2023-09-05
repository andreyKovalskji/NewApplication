package com.example.newapplication.util

import android.app.Activity
import android.content.SharedPreferences
import android.util.Log

class ShopManager(private val sharedPreferencesMethod: (String, Int) -> SharedPreferences) {

    private val buffsManager = BoostsManager(sharedPreferencesMethod)
    val buttonsColorManager = ButtonsColorManager(sharedPreferencesMethod)

    private var sharedPreferences = sharedPreferencesMethod(SHARED_PREFERENCES_FILE_NAME, Activity.MODE_PRIVATE)
    private var changedData = false

    private fun getSharedPreferences(): SharedPreferences {
        if(changedData) {
            sharedPreferences = sharedPreferencesMethod(SHARED_PREFERENCES_FILE_NAME, Activity.MODE_PRIVATE)
            changedData = false
        }
        return sharedPreferences
    }

    private fun editSharedPreferences(editAction: SharedPreferences.Editor.() -> SharedPreferences.Editor) {
        sharedPreferences.edit().editAction().apply()
        changedData = true
    }
    var selectedButtonColor
        get() = buttonsColorManager.selectedButtonsColor
        set(value) {
            if(value == COLOR_DEFAULT || value == COLOR_RED || value == COLOR_GREEN || value == COLOR_BLUE) {
                editSharedPreferences {
                    putString(BUTTONS_COLOR, value)
                }
            }
            else {
                Log.i("Selected button color", "Selected button color is unsuitable ($value).")
            }
        }

    var redWasBought
        get() = getSharedPreferences().getBoolean(RED_WAS_BOUGHT, false)
        set(value) {
            if(value) {
                editSharedPreferences {
                    putBoolean(RED_WAS_BOUGHT, true)
                }
            }
        }
    var greenWasBought
        get() = getSharedPreferences().getBoolean(GREEN_WAS_BOUGHT, false)
        set(value) {
            if(value) {
                editSharedPreferences {
                    putBoolean(GREEN_WAS_BOUGHT, true)
                }
            }
        }
    var blueWasBought
        get() = getSharedPreferences().getBoolean(BLUE_WAS_BOUGHT, false)
        set(value) {
            if(value) {
                editSharedPreferences {
                    putBoolean(BLUE_WAS_BOUGHT, true)
                }
            }
        }

    var luckBoosts
        get() = buffsManager.luckBoosts
        set(value) {
            buffsManager.luckBoosts = value
        }

    var winBoosts
        get() = buffsManager.winBoosts
        set(value) {
            buffsManager.winBoosts = value
        }

    companion object {
        private const val SHARED_PREFERENCES_FILE_NAME = "game"
        private const val BUTTONS_COLOR = "buttons_color"
        const val COLOR_DEFAULT = ""
        const val COLOR_RED = "red"
        const val COLOR_GREEN = "green"
        const val COLOR_BLUE = "blue"
        private const val LUCK_BOOSTS = "luck_boosts"
        private const val WIN_BOOSTS = "win_boosts"
        private const val RED_WAS_BOUGHT = "red_was_bought"
        private const val GREEN_WAS_BOUGHT = "green_was_bought"
        private const val BLUE_WAS_BOUGHT = "blue_was_bought"
    }
}