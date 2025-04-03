package com.imn.whocalling.util

import android.database.ContentObserver
import android.os.Handler
import android.os.Looper

class CallLogObserver(
    private val onCallLogChanged: () -> Unit
) : ContentObserver(Handler(Looper.getMainLooper())) {
    // try using HandlerThread instead?

    override fun onChange(selfChange: Boolean) {
        super.onChange(selfChange)
        onCallLogChanged()
    }
}