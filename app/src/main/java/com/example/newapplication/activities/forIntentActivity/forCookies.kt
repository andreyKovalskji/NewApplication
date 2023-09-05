package com.example.newapplication.activities.forIntentActivity

import android.webkit.CookieManager
import android.webkit.WebView

fun CookieManager.setDefaultParameters(w: WebView) {
    setAcceptCookie(true)
    setAcceptThirdPartyCookies(w, true)
}