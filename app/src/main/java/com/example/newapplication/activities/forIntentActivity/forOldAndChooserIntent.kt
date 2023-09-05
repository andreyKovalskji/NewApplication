package com.example.newapplication.activities.forIntentActivity

import android.content.Intent

fun Intent.setOldIntent(): Intent {
    type = "*/*"
    addCategory(Intent.CATEGORY_OPENABLE)
    return this
}

fun Intent.setChooserIntent(intentArray: Array<Intent>): Intent {
    putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray)
    putExtra(Intent.EXTRA_INTENT, Intent(Intent.ACTION_GET_CONTENT).setOldIntent())
    return this
}