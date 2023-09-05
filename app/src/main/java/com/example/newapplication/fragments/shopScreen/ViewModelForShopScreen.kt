package com.example.newapplication.fragments.shopScreen

import androidx.lifecycle.ViewModel
import com.example.newapplication.util.BalanceManager
import com.example.newapplication.util.ShopManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.koin.java.KoinJavaComponent.inject

class ViewModelForShopScreen: ViewModel() {
    val shopManager: ShopManager by inject(ShopManager::class.java)
    private val balanceManager: BalanceManager by inject(BalanceManager::class.java)

    private val _balance = MutableStateFlow(balanceManager.balance)
    val balance
        get() = _balance.asStateFlow()

    private val _selectedButtonsColor = MutableStateFlow(shopManager.selectedButtonColor)
    val selectedButtonsColor
        get() = _selectedButtonsColor.asStateFlow()

    private val redWasBought = MutableStateFlow(shopManager.redWasBought)
    private val greenWasBought = MutableStateFlow(shopManager.greenWasBought)
    private val blueWasBought = MutableStateFlow(shopManager.blueWasBought)

    private val _luckBoosts = MutableStateFlow(shopManager.luckBoosts)
    private val _winBoosts = MutableStateFlow(shopManager.winBoosts)

    private val _defaultButtonState = MutableStateFlow(getButtonsColorState(ShopManager.COLOR_DEFAULT))
    private val _redButtonState = MutableStateFlow(getButtonsColorState( ShopManager.COLOR_RED))
    private val _greenButtonState = MutableStateFlow(getButtonsColorState(ShopManager.COLOR_GREEN))
    private val _blueButtonState = MutableStateFlow(getButtonsColorState(ShopManager.COLOR_BLUE))

    val luckBoosts
        get() = _luckBoosts.asStateFlow()

    val winBoosts
        get() = _winBoosts.asStateFlow()

    lateinit var playNotEnoughMoneyCallback: () -> Unit

    private fun getButtonsColorState(color: String) = when {
        getSuitableWasBought(color)?.value == false -> ButtonsColorState.LOCKED
        _selectedButtonsColor.value == color -> ButtonsColorState.SELECTED
        else -> ButtonsColorState.UNLOCKED
    }
    private fun getSuitableWasBought(color: String): MutableStateFlow<Boolean>? = when(color) {
        ShopManager.COLOR_DEFAULT -> null
        ShopManager.COLOR_RED -> redWasBought
        ShopManager.COLOR_GREEN -> greenWasBought
        ShopManager.COLOR_BLUE -> blueWasBought
        else -> throw IllegalArgumentException("Unknown color (\"$color\").")
    }

    fun colorWasBought(color: String): Boolean = getButtonsColorState(color) != ButtonsColorState.LOCKED

    enum class ButtonsColorState {
        LOCKED,
        UNLOCKED,
        SELECTED
    }

    fun buyOrSelectButtonsColor(color: String) {
        val msf = getSuitableWasBought(color)
        if(msf?.value == false) {
            if(_balance.value >= 1000) {
                changeBalance(_balance.value - 1000)
                msf.value = true
                shopManager.selectedButtonColor = color
                _selectedButtonsColor.value = color
                when(color) {
                    ShopManager.COLOR_RED -> {
                        shopManager.redWasBought = true
                        _redButtonState.value = getButtonsColorState(color)
                    }
                    ShopManager.COLOR_GREEN -> {
                        shopManager.greenWasBought = true
                        _greenButtonState.value = getButtonsColorState(color)
                    }
                    ShopManager.COLOR_BLUE -> {
                        shopManager.blueWasBought = true
                        _blueButtonState.value = getButtonsColorState(color)
                    }
                }
            }
            else {
                playNotEnoughMoneyCallback()
            }
        }
        else {
            shopManager.selectedButtonColor = color
            _selectedButtonsColor.value = color
        }
    }

    private fun changeBalance(value: Int) {
        _balance.value = value
        balanceManager.balance = _balance.value
    }

    fun buyLuckBoost() {
        if(_balance.value >= 100 && _luckBoosts.value < 100) {
            changeBalance(_balance.value - 100)
            _luckBoosts.update { it + 1 }
            shopManager.luckBoosts = _luckBoosts.value
        }
    }

    fun buyWinBoost() {
        if(_balance.value >= 100 && _winBoosts.value < 100) {
            changeBalance(_balance.value - 100)
            _winBoosts.update { it + 1 }
            shopManager.winBoosts = _winBoosts.value
        }
    }
}