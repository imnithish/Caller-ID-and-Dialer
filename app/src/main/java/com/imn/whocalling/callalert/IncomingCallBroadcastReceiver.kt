package com.imn.whocalling.callalert

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.TelephonyManager
import com.imn.whocalling.di.getApiService
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IncomingCallBroadcastReceiver : BroadcastReceiver() {

    private val incomingCallNotification = IncomingCallNotification()

    private val Intent.needToShowWindow: Boolean
        get() = getStringExtra(TelephonyManager.EXTRA_STATE) == TelephonyManager.EXTRA_STATE_RINGING

    private val Intent.needToCloseWindow: Boolean
        get() = getStringExtra(TelephonyManager.EXTRA_STATE) == TelephonyManager.EXTRA_STATE_IDLE ||
                getStringExtra(TelephonyManager.EXTRA_STATE) == TelephonyManager.EXTRA_STATE_OFFHOOK

    private val Intent.needToCloseWindowForApiN: Boolean
        get() = needToCloseWindow

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == TelephonyManager.ACTION_PHONE_STATE_CHANGED) {
            val phoneNumber = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
            } else {
                null
            }
            when {
                intent.needToCloseWindowForApiN -> context
                    .applicationContext
                    .startService(Intent(context, IncomingCallService::class.java))

                phoneNumber == null -> Unit
                intent.needToShowWindow -> {
                    val apiService = getApiService(context)
                    incomingCallNotification.showWindow(context, phoneNumber, apiService)
                }
                intent.needToCloseWindow -> incomingCallNotification.closeWindow()
            }
        }
    }
}
