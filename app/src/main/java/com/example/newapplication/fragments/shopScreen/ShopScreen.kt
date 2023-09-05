package com.example.newapplication.fragments.shopScreen

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.allViews
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.newapplication.R
import com.example.newapplication.fragments.menuScreen.MenuScreen
import com.example.newapplication.util.BalanceManager
import com.example.newapplication.util.ShopManager
import com.example.newapplication.widgets.setAppCompatButtonsColor
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class ShopScreen: Fragment() {
    private val viewModel: ViewModelForShopScreen by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.f_shop, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("Shop", "Shop opened!")

        fun setButtonsColor() {
            context?.let { context ->
                view.allViews.filter { it is AppCompatButton && it.id !in listOf(R.id.defaultButtonsColor, R.id.redButtonsColor, R.id.greenButtonsColor, R.id.blueButtonsColor) }.forEach {
                    it as AppCompatButton
                    it.setAppCompatButtonsColor(viewModel.shopManager.buttonsColorManager, context)
                }
            }
        }
        setButtonsColor()
        view.run {
            findViewById<AppCompatButton>(R.id.leaveShopButton).setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, MenuScreen())
                    .commit()
            }
            val userBalance = findViewById<TextView>(R.id.userBalance)
            viewModel.playNotEnoughMoneyCallback = {
                (userBalance.background as AnimationDrawable).run {
                    if(isRunning) {
                        stop()
                    }
                    start()
                }
            }
            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.balance.collect {
                        userBalance.text = getString(R.string.user_balance, it)
                    }
                }
            }
            val luckBoostsText = view.findViewById<TextView>(R.id.luckBoostText)
            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.luckBoosts.collect {
                        luckBoostsText.text = getString(R.string.luck_boost_text, it)
                    }
                }
            }
            val winBoostsText = view.findViewById<TextView>(R.id.winBoostText)
            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.winBoosts.collect {
                        winBoostsText.text = getString(R.string.win_boost_text, it)
                    }
                }
            }
            view.findViewById<AppCompatButton>(R.id.luckBoostButton).setOnClickListener {
                viewModel.buyLuckBoost()
            }
            view.findViewById<AppCompatButton>(R.id.winBoostButton).setOnClickListener {
                viewModel.buyWinBoost()
            }
            // Color part
            val defaultButtonsColor = findViewById<AppCompatButton>(R.id.defaultButtonsColor).apply {
                setOnClickListener {
                    viewModel.buyOrSelectButtonsColor(ShopManager.COLOR_DEFAULT)
                    setButtonsColor()
                }
            }
            val redButtonsColor = findViewById<AppCompatButton>(R.id.redButtonsColor).apply {
                setOnClickListener {
                    viewModel.buyOrSelectButtonsColor(ShopManager.COLOR_RED)
                    setButtonsColor()
                }
            }
            val greenButtonsColor = findViewById<AppCompatButton>(R.id.greenButtonsColor).apply {
                setOnClickListener {
                    viewModel.buyOrSelectButtonsColor(ShopManager.COLOR_GREEN)
                    setButtonsColor()
                }
            }
            val blueButtonsColor = findViewById<AppCompatButton>(R.id.blueButtonsColor).apply {
                setOnClickListener {
                    viewModel.buyOrSelectButtonsColor(ShopManager.COLOR_BLUE)
                    setButtonsColor()
                }
            }
            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.selectedButtonsColor.collect {
                        val selectedForeground = ResourcesCompat.getDrawable(resources, R.drawable.selected_buttons_color_fg, null)
                        val lockedForeground = ResourcesCompat.getDrawable(resources, R.drawable.round_lock_24, null)
                        fun getButtonsColorForeground(color: String) = when {
                            it == color -> selectedForeground
                            !viewModel.colorWasBought(color) -> lockedForeground
                            else -> null
                        }
                        defaultButtonsColor.foreground = getButtonsColorForeground(ShopManager.COLOR_DEFAULT)
                        redButtonsColor.foreground = getButtonsColorForeground(ShopManager.COLOR_RED)
                        greenButtonsColor.foreground = getButtonsColorForeground(ShopManager.COLOR_GREEN)
                        blueButtonsColor.foreground = getButtonsColorForeground(ShopManager.COLOR_BLUE)
                    }
                }
            }
        }
    }
}