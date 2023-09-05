package com.example.newapplication.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import com.example.newapplication.R
import com.example.newapplication.dto.AllowAccessObject
import com.example.newapplication.fragments.gameView.ViewModelForGameScreen
import com.example.newapplication.fragments.loadingScreen.LoadingScreen
import com.example.newapplication.fragments.menuScreen.MenuScreen
import com.example.newapplication.fragments.shopScreen.ViewModelForShopScreen
import com.example.newapplication.util.BalanceManager
import com.example.newapplication.util.BoostsManager
import com.example.newapplication.util.ButtonsColorManager
import com.example.newapplication.util.ShopManager
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module

class MainActivity : AppCompatActivity() {
    private var url = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
//        getSharedPreferences("game", 0).edit().clear().apply()
        if(GlobalContext.getOrNull() == null) {
            startKoin {
                modules(
                    module {
                        viewModelOf(::ViewModelForGameScreen)
                        viewModelOf(::ViewModelForShopScreen)
                        single {
                            BalanceManager(::getSharedPreferences)
                        }
                        single {
                            ButtonsColorManager(::getSharedPreferences)
                        }
                        single {
                            BoostsManager(::getSharedPreferences)
                        }
                        single {
                            ShopManager(::getSharedPreferences)
                        }
                        single(qualifier("url")) {
                            url
                        }
                    }
                )
            }
        }
        if(savedInstanceState == null) {
            lifecycleScope.launch {
                var accessAllowed = false
                val ktorClient = HttpClient(CIO)
                val response =
                    ktorClient.get("https://gist.githubusercontent.com/andreyKovalskji/d1ee2379182206603f8e485528d4ccde/raw/hgN6447NtwERw")
                if (false) {
                    val moshi = Moshi.Builder()
                        .addLast(KotlinJsonAdapterFactory())
                        .build()
                    val adapter = moshi.adapter(AllowAccessObject::class.java).lenient()
                    val obj = adapter.fromJson(response.bodyAsText())
                    if (obj != null && obj.letMeIn && obj.letMeInside != null) {
                        Log.i("Let me in", "Access allowed.")
                        accessAllowed = true
                        url = obj.letMeInside
                        startActivity(Intent(this@MainActivity, InternetActivity::class.java))
                    } else {
                        Log.i(
                            "Response", when {
                                obj == null -> "Object is null"
                                !obj.letMeIn -> "Let me in var is false"
                                else -> "Url is null"
                            }
                        )
                    }
                }
                ktorClient.close()
                if (!accessAllowed) {
                    setOnBackPressedListener()
                    beginTransaction {
                        replace(R.id.container, MenuScreen())
                    }
                }
            }
            beginTransaction(false) {
                add(R.id.container, LoadingScreen())
            }
        }
        else {
            beginTransaction(false) {
                add(R.id.container, MenuScreen())
            }
        }

    }

    private fun beginTransaction(allowingStateLoss: Boolean = true, action: FragmentTransaction.() -> FragmentTransaction) {
        val transaction = supportFragmentManager.beginTransaction()
        val result = transaction.action()
        if(allowingStateLoss) {
            result.commitAllowingStateLoss()
        }
        else {
            result.commit()
        }
    }

    private fun setOnBackPressedListener() {
        onBackPressedDispatcher.addCallback(
            object: OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if(supportFragmentManager.fragments.first() is MenuScreen) {
                        finish()
                    }
                    else {
                        beginTransaction(false) {
                            replace(R.id.container, MenuScreen())
                        }
                    }
                }
            }
        )
    }
}