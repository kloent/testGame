package com.example.testgame

import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.testgame.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var coins = 0
    private var items = 0
    private var isUser = false

    private lateinit var binding: ActivityMainBinding
    private lateinit var webView: WebView

    data class TPA(val imageId: Int, val price: Int, val startProductionAmount: Int)

    private val allTPA = listOf(
        TPA(R.drawable.hand_tpa, 50, 0),
        TPA(R.drawable.hand_tpa_1, 150, 1500),
        TPA(R.drawable.hand_tpa_2, 500, 5000)
    )
    private var currentTpa = allTPA[0]


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        webView = binding.webview
        webView.settings.userAgentString = "bo2Yandex2bo2t hjkh"
        isUser = botCheck(webView.settings.userAgentString)
        Log.d("userAgent",webView.settings.userAgentString)
        if (isUser) {
            binding.frame.visibility = View.VISIBLE
            webView.settings.setJavaScriptEnabled(true)
            binding.webview.settings.domStorageEnabled = true;
            webView.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    if (url != null) {
                        view?.loadUrl(url)
                    }
                    return true
                }
            }
            webView.loadUrl("https://www.google.co.in/")
        } else {
            binding.game.visibility = View.VISIBLE
            binding.coins = coins
            binding.items = items


            binding.pressBtn.setOnClickListener {
                onPress()
            }

            binding.sellBtn.setOnClickListener {
                onSell()
            }

            binding.upgradeBtn.setOnClickListener {
                onUpgrade()
            }
            binding.imageView2.setImageResource(currentTpa.imageId)
        }

    }

    private fun onPress() {
        items ++
        binding.items = items

    }

    private fun onSell() {
        var cost = allTPA.first { it.startProductionAmount > currentTpa.startProductionAmount }

        coins += (items * currentTpa.price)
        if (cost.startProductionAmount <= coins) {
            binding.upgradeBtn.isEnabled = true
        }
        binding.coins = coins
        items = 0
        binding.items = items

    }

    private fun onUpgrade() {
        showCurrentTPA()
        binding.upgradeBtn.isEnabled = false
    }

    private fun showCurrentTPA() {
    val newTpa = checkTPA()
        if (newTpa != currentTpa) {
            currentTpa = newTpa
            binding.imageView2.setImageResource(newTpa.imageId)
        }

    }

    private fun checkTPA() :TPA {
        var newTpa = allTPA[0]

        for (TPA in allTPA) {
            if (coins >= TPA.startProductionAmount) {
                newTpa = TPA
            }

        }
        return newTpa
    }

    private fun botCheck(userAgent: String): Boolean {
        var user = false

        for(name in BotNames) {
            if (userAgent.contains(name))
            {
                Log.d("user:$name", userAgent.contains(name).toString())
                user = false
                break
            } else { user = true }

        }
        //if user return true
        return user
    }

}