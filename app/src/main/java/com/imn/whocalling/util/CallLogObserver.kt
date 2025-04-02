package com.imn.whocalling.util

import android.database.ContentObserver
import android.os.Handler
import android.os.Looper

// TODO: implement other methods like broadcast receiver.
//  sometimes, the call log doesn't update immediately due to system caching.
class CallLogObserver(
    private val onCallLogChanged: () -> Unit
) : ContentObserver(Handler(Looper.getMainLooper())) {

    override fun onChange(selfChange: Boolean) {
        super.onChange(selfChange)
        onCallLogChanged()
    }
}