package com.example.newapplication.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.KeyEvent
import android.webkit.CookieManager
import android.webkit.ValueCallback
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.newapplication.R
import com.example.newapplication.activities.forIntentActivity.receiveValue
import org.koin.android.ext.android.inject
import com.example.newapplication.activities.forIntentActivity.*
import org.koin.core.qualifier.qualifier
import java.io.File
import java.io.IOException

class InternetActivity: AppCompatActivity() {
    lateinit var webV: WebView
    var fileCallback: ValueCallback<Array<Uri>>? = null
    var cUri: Uri? = null
    private val mainUrl: String by inject(qualifier = qualifier("url"))

    private val requestPermissionLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean? ->
        when(isGranted) {
            true -> Log.w("Request permission launcher", "Is granted.")
            false -> Log.w("Request permission launcher", "Is not granted.")
            else -> Log.w("Request permission launcher", "Is null.")
        }
        requestPermissionLauncherAction()
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.internet)
        webV = WebView(this).apply {
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        }
        findViewById<LinearLayout>(R.id.webViewContainer).addView(webV)
        Log.i("Url, received by InternetActivity", "mainUrl: $mainUrl")
        setSettings()
        CookieManager.getInstance().setDefaultParameters(webV)
        webV.webChromeClient(requestPermissionLauncher) {
            try {
                fileCallback = it
                true
            }
            catch (e: Exception) {
                Log.e("Web chrome client", e.stackTraceToString())
                false
            }
        }
        webV.webViewClient = Client()
        webV.loadUrl(mainUrl)
    }

    private fun setSettings() {
        val settings = webV.settings
        settings.javaScriptEnabled()
        settings.allowUniversalAccessFromFileURLs()
        settings.useWideViewPort()
        settings.userAgentString()
        settings.allowFileAccess()
        settings.javaScriptCanOpenWindowsAutomatically()
        settings.domStorageEnabled()
        settings.mixedContentMode()
        settings.databaseEnabled()
        settings.loadWithOverviewMode()
        settings.allowFileAccessFromFileURLs()
        settings.cacheMode()
        settings.allowContentAccess()
    }

    inner class Client : WebViewClient() {

        fun reallyContains(uri: String): Boolean {
            val c1 = uri.contains("http")
            val c2 = uri.indexOf("http") != -1
            val c3 = uri.contains("/")
            return c1 == c2 == c3
        }

        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
            val uriAsString = request.url.toString()
            return if (uriAsString.contains("/")) {
                if (uriAsString.contains("http")) {
                    Log.i("Uri", uriAsString)
                    !reallyContains(uriAsString)
                } else {
                    Log.i("Uri", "Uri $uriAsString not contains \"http\". Start activity")
                    val newIntent = Intent(Intent.ACTION_VIEW, Uri.parse(uriAsString))
                    startActivity(newIntent)
                    true
                }
            } else true
        }

        override fun onReceivedLoginRequest(
            view: WebView, realm: String, account: String?, args: String
        ) {
            super.onReceivedLoginRequest(view, realm, account, args)
        }
    }

    private fun requestPermissionLauncherAction() {
        val photoFile = try {
            File.createTempFile(
                "photo_file",
                ".jpg",
                getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            )
        } catch (exception: IOException) {
            Log.e("Photo file", "Error handled...", exception)
            null
        }
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile))
        }
        cUri = Uri.fromFile(photoFile)
        val intentArray = arrayOf(takePictureIntent)
        val chooserIntent = Intent(Intent.ACTION_CHOOSER).setChooserIntent(intentArray)
        startActivityForResult(chooserIntent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(fileCallback?.receiveValue(resultCode, data, cUri) == false) {
            fileCallback = null
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        webV.saveState(outState)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (webV.canGoBack()) {
            webV.goBack()
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        webV.restoreState(savedInstanceState)
    }
}