package com.imn.whocalling.ui.screens.dialer

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.imn.whocalling.util.getAllContacts
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DialerViewModel @Inject constructor(
    private val application: Application,
) : ViewModel() {

    var phoneNumber by mutableStateOf("")
        private set

    var suggestion: Pair<String, String>? by mutableStateOf(null)
        private set

    var allContacts = mutableStateListOf<Pair<String, String>>()

    init {
        getAllContacts(application).forEach {
            allContacts.add(it)
        }
    }

    fun onNumberClick(number: String) {
        if (phoneNumber.length < 10) {
            phoneNumber += number
            searchContactAndSet()
        }
    }

    fun onDelete() {
        if (phoneNumber.isNotEmpty()) {
            phoneNumber = phoneNumber.dropLast(1)
            searchContactAndSet()
        }
    }

    fun searchContactAndSet(
    ) {
        val matches = allContacts.filter { it.second.contains(phoneNumber, ignoreCase = true) }
        suggestion = matches.firstOrNull()
        if (phoneNumber.isEmpty())
            suggestion = null
    }

}