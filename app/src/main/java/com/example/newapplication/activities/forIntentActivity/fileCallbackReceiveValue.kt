package com.example.newapplication.activities.forIntentActivity

import android.content.Intent
import android.net.Uri
import android.webkit.ValueCallback

fun ValueCallback<Array<Uri>>.receiveValue(resultCode: Int, data: Intent?, cUri: Uri?): Boolean {
    if (resultCode == -1) {
        val dataString = data?.dataString
        if (dataString != null) {
            val resultUri = Uri.parse(dataString)
            onReceiveValue(resultUri.asArray())
        } else {
            onReceiveValue(cUri?.asArray())
        }
    } else {
        onReceiveValue(null)
    }
    return true
}

fun Uri.asArray() = arrayOf(this)