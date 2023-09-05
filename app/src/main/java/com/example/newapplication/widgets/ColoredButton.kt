package com.example.newapplication.widgets

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Color
import androidx.appcompat.widget.AppCompatButton
import com.example.newapplication.util.ButtonsColorManager

fun AppCompatButton.setAppCompatButtonsColor(buttonsColorManager: ButtonsColorManager, context: Context) {
    val isDarkTheme = context.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
    val selectedColor = buttonsColorManager.selectedButtonsColor
    val textColor = when(selectedColor) {
        ButtonsColorManager.COLOR_RED -> (0xFFFFFFFF).toInt()
        ButtonsColorManager.COLOR_GREEN -> (0xFF000000).toInt()
        ButtonsColorManager.COLOR_BLUE -> (0xFFFFFFFF).toInt()
        else -> if(isDarkTheme) Color.WHITE else (0xFFCCCCCC).toInt()
    }
    backgroundTintList = ColorStateList.valueOf(when(selectedColor) {
        ButtonsColorManager.COLOR_RED -> (0xFFFF0000).toInt()
        ButtonsColorManager.COLOR_GREEN -> (0xFF00FF00).toInt()
        ButtonsColorManager.COLOR_BLUE -> (0xFF0000FF).toInt()
        else -> if(isDarkTheme) (0xFF5a595b).toInt() else (0xFFd7d7d7).toInt()
    })
    setTextColor(textColor)
}