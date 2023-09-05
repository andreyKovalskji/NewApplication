package com.example.newapplication.fragments.gameView

import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newapplication.util.BoostsManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject
import kotlin.math.abs
import kotlin.random.Random

class ViewModelForGameScreen: ViewModel() {
    private val _userInput: MutableStateFlow<String> = MutableStateFlow("")
    private val boostsManager: BoostsManager by inject(BoostsManager::class.java)
    val userInput
        get() = _userInput.asStateFlow()

    var localeText: Map<String, String> = mapOf()
        get() = field
        set(value) {
            field = value
            val newGameText = field[GAME_TEXT_WRITE]
            if(newGameText != null) {
                _gameText.value = newGameText
            }
        }

    private val _gameText = MutableStateFlow("")
    val gameText
        get() = _gameText.asStateFlow()

    private val _balance = MutableStateFlow(0)
    val balance
        get() = _balance.asStateFlow()

    private val _canInputSomething = MutableStateFlow(true)
    val canInputSomething
        get() = _canInputSomething.asStateFlow()

    fun changeUserInput(newInput: String = "", clearOne: Boolean = false, clearAll: Boolean = false) {
        if(clearOne) {
            if(_userInput.value.isNotEmpty()) {
                _userInput.update{it.substring(0, _userInput.value.length - 1)}
            }
            return
        }
        if(clearAll) {
            _userInput.update {""}
            return
        }
        if(newInput.isEmpty()) return
        if(newInput.length + _userInput.value.length > 3) return
        if(_userInput.value.isNotEmpty()) {
            if ((_userInput.value  + newInput).toInt() > 100) return
        }
        _userInput.update { it + newInput }
    }

    fun setBalance(value: Int) {
        _balance.value = value
    }

    suspend fun tryToGuess() {
        if(_balance.value < 100) return
        _gameText.value = localeText.getOrDefault(GAME_TEXT_WAIT, "Processing your data...")
        _balance.value -= 100
        delay(1000)
        val rnd = Random.nextInt(0, 101)
        val curNum = _userInput.value.toInt()
        var cash: Int = 0
        val luckBoost = if(boostsManager.luckBoosts > 0) {
            boostsManager.luckBoosts -= 1
            1
        }
        else {
            0
        }
        val winBoost = if(boostsManager.winBoosts > 0) {
            boostsManager.winBoosts -= 1
            1
        }
        else {
            0
        }
        _gameText.value =  localeText.getOrDefault( when {
            rnd == curNum -> {
                cash = 100000
                cash += winBoost * cash
                TRY_TO_GUESS_EXACT
            }
            abs(rnd - curNum) <= 3 + (luckBoost * 3) -> {
                cash = 10000
                cash += winBoost * cash
                TRY_TO_GUESS_CLOSE
            }
            abs(rnd - curNum) <= 5 + (luckBoost * 5) -> {
                cash = 1000
                cash += winBoost * cash
                TRY_TO_GUESS_NEAR
            }
            abs(rnd - curNum) <= 15 + (luckBoost * 15) -> {
                cash = 100
                cash += winBoost * cash
                TRY_TO_GUESS_BARELY
            }
            else -> TRY_TO_GUESS_NOT
        }, "Your: %d. Guessed: %d. Your won: %d").format(curNum, rnd, cash)
        _balance.update { it + cash }
        delay(2000)
        _userInput.value = ""
        _gameText.value = localeText.getOrDefault(GAME_TEXT_WRITE, "Write number from 0 to 100.")

    }

    fun setCanInputSomething(value: Boolean) {
        _canInputSomething.value = value
    }


    companion object {
        const val TRY_TO_GUESS_EXACT = "exact"
        const val TRY_TO_GUESS_CLOSE = "close"
        const val TRY_TO_GUESS_NEAR = "near"
        const val TRY_TO_GUESS_BARELY = "barely"
        const val TRY_TO_GUESS_NOT = "not"
        const val GAME_TEXT_WRITE = "write"
        const val GAME_TEXT_WAIT = "wait"

    }
}