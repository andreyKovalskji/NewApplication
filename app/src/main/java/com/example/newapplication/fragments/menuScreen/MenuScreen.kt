package com.example.newapplication.fragments.menuScreen

import android.content.res.Resources.Theme
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.allViews
import androidx.fragment.app.Fragment
import com.example.newapplication.R
import com.example.newapplication.fragments.gameView.GameScreen
import com.example.newapplication.fragments.rulesScreen.RulesScreen
import com.example.newapplication.fragments.shopScreen.ShopScreen
import com.example.newapplication.util.ButtonsColorManager
import com.example.newapplication.util.ShopManager
import com.example.newapplication.widgets.setAppCompatButtonsColor
import org.koin.android.ext.android.inject

class MenuScreen: Fragment() {

    private val buttonsColorManager: ButtonsColorManager by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.f_menu, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let { context ->
            view.allViews.filter { it is AppCompatButton }.forEach {
                it as AppCompatButton
                it.setAppCompatButtonsColor(buttonsColorManager, context)
            }
        }
        view.run {
            findViewById<AppCompatButton>(R.id.playButton).setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, GameScreen())
                    .commit()
            }
            findViewById<AppCompatButton>(R.id.shopButton).setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, ShopScreen())
                    .commit()
            }
            findViewById<AppCompatButton>(R.id.rulesButton).setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, RulesScreen())
                    .commit()
            }
        }
    }
}