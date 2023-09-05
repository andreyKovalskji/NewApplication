package com.example.newapplication.fragments.gameView

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.allViews
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.newapplication.R
import com.example.newapplication.fragments.menuScreen.MenuScreen
import com.example.newapplication.util.BalanceManager
import com.example.newapplication.util.ButtonsColorManager
import com.example.newapplication.widgets.setAppCompatButtonsColor
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class GameScreen: Fragment() {
    private val viewModel: ViewModelForGameScreen by inject()
    private lateinit var buttons: List<AppCompatButton>
    private val balanceManager: BalanceManager by inject()
    private val buttonsColorManager: ButtonsColorManager by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.f_game, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let { context ->
            view.allViews.filter { it is AppCompatButton }.forEach {
                it as AppCompatButton
                it.setAppCompatButtonsColor(buttonsColorManager, context)
            }
        }
        viewModel.localeText = mapOf(
            ViewModelForGameScreen.GAME_TEXT_WRITE to getString(R.string.game_text_write),
            ViewModelForGameScreen.GAME_TEXT_WAIT to getString(R.string.game_text_wait),
            ViewModelForGameScreen.TRY_TO_GUESS_EXACT to getString(R.string.try_to_guess_exact),
            ViewModelForGameScreen.TRY_TO_GUESS_CLOSE to getString(R.string.try_to_guess_close),
            ViewModelForGameScreen.TRY_TO_GUESS_NEAR to getString(R.string.try_to_guess_near),
            ViewModelForGameScreen.TRY_TO_GUESS_BARELY to getString(R.string.try_to_guess_barely),
            ViewModelForGameScreen.TRY_TO_GUESS_NOT to getString(R.string.try_to_guess_not),
        )
        viewModel.setBalance(balanceManager.balance)
        view.run {
            buttons = listOf(
                findViewById(R.id.clearOneButton), findViewById(R.id.clearAllButton), findViewById(R.id.tryButton),
                findViewById(R.id.numberButton0), findViewById(R.id.numberButton1), findViewById(R.id.numberButton2),
                findViewById(R.id.numberButton3), findViewById(R.id.numberButton4), findViewById(R.id.numberButton5),
                findViewById(R.id.numberButton6), findViewById(R.id.numberButton7), findViewById(R.id.numberButton8),
                findViewById(R.id.numberButton9),
            )
            buttons[0].setOnClickListener {
                viewModel.changeUserInput(clearOne = true)
            }
            buttons[1].setOnClickListener {
                viewModel.changeUserInput(clearAll = true)
            }
            buttons[2].setOnClickListener {
                viewLifecycleOwner.lifecycleScope.launch {
                    if(viewModel.userInput.value == "") return@launch
                    viewModel.setCanInputSomething(false)
                    viewModel.tryToGuess()
                    balanceManager.balance = viewModel.balance.value
                    viewModel.setCanInputSomething(true)
                }
            }
            val numberButtons: List<AppCompatButton> = buttons.subList(3, buttons.size)
            for((ind, numberButton) in numberButtons.withIndex()) {
                numberButton.setOnClickListener {
                    viewModel.changeUserInput(ind .toString())
                }
            }
            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.userInput.collect {
                        findViewById<TextView>(R.id.userInput).text = it
                    }
                }
            }
            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.balance.collect {
                        findViewById<TextView>(R.id.userBalance).text = getString(R.string.user_balance, it)
                    }
                }
            }
            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.gameText.collect {
                        findViewById<TextView>(R.id.gameText).text = it
                    }
                }
            }
            findViewById<AppCompatButton>(R.id.leaveGameButton).setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, MenuScreen())
                    .commit()
            }
            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.canInputSomething.collect {
                        for(button in buttons) {
                            button.isEnabled = it
                        }
                    }
                }
            }
            findViewById<AppCompatButton>(R.id.getSomeCoins).setOnClickListener {
                viewModel.setBalance(viewModel.balance.value + 100)
            }
        }
    }
}