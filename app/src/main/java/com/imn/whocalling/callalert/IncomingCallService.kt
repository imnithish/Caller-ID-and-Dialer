package com.imn.whocalling.callalert

import android.content.Intent
import android.os.Build
import android.telecom.Call
import android.telecom.CallScreeningService
import com.imn.whocalling.di.getApiService
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IncomingCallService : CallScreeningService() {

    private val incomingCallNotification = IncomingCallNotification()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        incomingCallNotification.closeWindow()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onScreenCall(callDetails: Call.Details) {

        val apiService = getApiService(applicationContext)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (callDetails.callDirection == Call.Details.DIRECTION_INCOMING) {
                val phoneNumber = callDetails.handle.schemeSpecificPart
                phoneNumber?.let {
                    incomingCallNotification.showWindow(this, it, apiService)
                }
            }
            respondToCall(callDetails, CallResponse.Builder().build())
        }
    }
}