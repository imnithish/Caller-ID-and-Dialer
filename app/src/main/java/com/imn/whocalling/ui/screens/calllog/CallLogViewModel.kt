package com.imn.whocalling.ui.screens.calllog

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.CallLog
import android.provider.ContactsContract
import android.view.View
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imn.whocalling.data.CallLogEntry
import com.imn.whocalling.ui.screens.calllog.data.CallLogRepo
import com.imn.whocalling.util.CallLogObserver
import com.imn.whocalling.util.CallType
import com.imn.whocalling.util.ResultWrapper
import com.imn.whocalling.util.formatDate
import com.imn.whocalling.util.formatDuration
import com.imn.whocalling.util.logd
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class CallLogViewModel @Inject constructor(
    private val application: Application,
    private val callLogRepo: CallLogRepo
) : ViewModel() {

    private val _callLogs = mutableStateListOf<CallLogEntry>()
    val callLogs: List<CallLogEntry> get() = _callLogs

    private val contentResolver = getApplication(application).contentResolver
    private val callLogObserver = CallLogObserver {
        fetchCallLogs()
    }

    init {
        fetchCallLogs()
        contentResolver.registerContentObserver(
            CallLog.Calls.CONTENT_URI, true, callLogObserver
        )
    }

    fun getContactName(context: Context, phoneNumber: String): String? {
        val uri = Uri.withAppendedPath(
            ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
            Uri.encode(phoneNumber)
        )
        val projection = arrayOf(ContactsContract.PhoneLookup.DISPLAY_NAME)

        val cursor: Cursor? = context.contentResolver.query(uri, projection, null, null, null)

        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME)
                return it.getString(nameIndex)
            }
        }
        cursor?.close()
        return null
    }

    @SuppressLint("MissingPermission")
    fun fetchCallLogs() {
        "fetchCallLogs".logd()
        val context = getApplication(application).applicationContext
        val projection = arrayOf(
            CallLog.Calls.NUMBER,
            CallLog.Calls.TYPE,
            CallLog.Calls.DATE,
            CallLog.Calls.DURATION
        )

        val cursor: Cursor? = context.contentResolver.query(
            CallLog.Calls.CONTENT_URI,
            projection,
            null, null,
            "${CallLog.Calls.DATE} DESC"
        )

        cursor?.use {
            _callLogs.clear()
            val numberIndex = it.getColumnIndex(CallLog.Calls.NUMBER)
            val typeIndex = it.getColumnIndex(CallLog.Calls.TYPE)
            val dateIndex = it.getColumnIndex(CallLog.Calls.DATE)
            val durationIndex = it.getColumnIndex(CallLog.Calls.DURATION)
            while (it.moveToNext()) {
                val number = it.getString(numberIndex)
                val name = getContactName(application, number)
                val duration = it.getInt(durationIndex)

                val type = when (it.getInt(typeIndex)) {
                    CallLog.Calls.INCOMING_TYPE -> CallType.Incoming.value
                    CallLog.Calls.OUTGOING_TYPE -> if (duration == 0) CallType.OutgoingNotAccepted.value else CallType.Outgoing.value
                    CallLog.Calls.MISSED_TYPE -> CallType.Missed.value
                    CallLog.Calls.REJECTED_TYPE -> CallType.Rejected.value
                    else -> CallType.Unknown.value
                }
                val date = Date(it.getLong(dateIndex)).formatDate()

                _callLogs.add(
                    CallLogEntry(
                        number = number,
                        type = type,
                        date = date ?: "",
                        duration = duration.formatDuration(),
                        savedName = name,
                        whoCallingName = null,
                        isSpam = false,
                    )
                )
            }
        }
        cursor?.close()
        fetchMockyDataAndUpdateCallLogs()
    }

    private fun fetchMockyDataAndUpdateCallLogs() {
        viewModelScope.launch {
            when (val res = callLogRepo.getMockyData()) {
                is ResultWrapper.Error -> {
                    "${res.exception}".logd()
                }

                is ResultWrapper.Success -> {
                    res.value.body()?.numbers?.run {
                        _callLogs.replaceAll { callLog ->
                            val matchingNumber = this.find { it.number == callLog.number }
                            if (matchingNumber != null) {
                                callLog.copy(
                                    whoCallingName = matchingNumber.name,
                                    isSpam = matchingNumber.isSpam,
                                    location = matchingNumber.location
                                )
                            } else {
                                callLog
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        contentResolver.unregisterContentObserver(callLogObserver)
    }
}