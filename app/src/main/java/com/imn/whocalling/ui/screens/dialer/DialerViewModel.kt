package com.imn.whocalling.ui.screens.dialer

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DialerViewModel @Inject constructor(
    private val application: Application,
) : ViewModel() {

    var phoneNumber by mutableStateOf("")
        private set

    fun onNumberClick(number: String) {
        if (phoneNumber.length < 10) {
            phoneNumber += number
        }
    }

    fun onDelete() {
        if (phoneNumber.isNotEmpty()) {
            phoneNumber = phoneNumber.dropLast(1)
        }
    }

}