package com.example.newapplication.activities.forIntentActivity

import android.Manifest
import android.net.Uri
import android.util.Log
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.activity.result.ActivityResultLauncher

fun WebView.webChromeClient(requestPermissionLauncher: ActivityResultLauncher<String>, fileCallbackSetter: (ValueCallback<Array<Uri>>) -> Boolean) {
    webChromeClient = object : WebChromeClient() {
        override fun onShowFileChooser(
            webView: WebView,
            newFileCallback: ValueCallback<Array<Uri>>,
            fileChooserParams: FileChooserParams
        ): Boolean {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            if(fileCallbackSetter(newFileCallback)) {
                Log.i("File callback setter", "Was set!")
            }
            else {
                Log.i("File callback setter", "Was not set...")
            }
            return true
        }
    }
}