package com.example.newapplication.activities.forIntentActivity

import android.webkit.WebSettings


fun WebSettings.allowContentAccess() {
    allowContentAccess = true
}
fun WebSettings.allowFileAccess() {
    allowFileAccess = true
}
fun WebSettings.javaScriptCanOpenWindowsAutomatically() {
    javaScriptCanOpenWindowsAutomatically = true
}
fun WebSettings.allowFileAccessFromFileURLs() {
    allowFileAccessFromFileURLs = true
}
fun WebSettings.mixedContentMode() {
    mixedContentMode = 0
}
fun WebSettings.cacheMode() {
    cacheMode = WebSettings.LOAD_DEFAULT
}
fun WebSettings.userAgentString() {
    userAgentString = userAgentString.replace("; wv", "")
}
fun WebSettings.domStorageEnabled() {
    domStorageEnabled = true
}
fun WebSettings.javaScriptEnabled() {
    javaScriptEnabled = true
}
fun WebSettings.databaseEnabled() {
    databaseEnabled = true
}
fun WebSettings.allowUniversalAccessFromFileURLs() {
    allowUniversalAccessFromFileURLs = true
}
fun WebSettings.useWideViewPort() {
    useWideViewPort = true
}
fun WebSettings.loadWithOverviewMode() {
    loadWithOverviewMode = true
}