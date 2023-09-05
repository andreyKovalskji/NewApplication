package com.example.newapplication.fragments.rulesScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.allViews
import androidx.fragment.app.Fragment
import com.example.newapplication.R
import com.example.newapplication.fragments.menuScreen.MenuScreen
import com.example.newapplication.util.ButtonsColorManager
import com.example.newapplication.widgets.setAppCompatButtonsColor
import org.koin.android.ext.android.inject

class RulesScreen: Fragment() {
    private val buttonsColorManager: ButtonsColorManager by inject()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.f_rules, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let { context ->
            view.allViews.filter { it is AppCompatButton }.forEach {
                it as AppCompatButton
                it.setAppCompatButtonsColor(buttonsColorManager, context)
            }
        }
        view.run {
            findViewById<AppCompatButton>(R.id.leaveRulesButton).setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, MenuScreen())
                    .commit()
            }
        }
    }
}