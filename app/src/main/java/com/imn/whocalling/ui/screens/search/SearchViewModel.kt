package com.imn.whocalling.ui.screens.search

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
class SearchViewModel @Inject constructor(
    application: Application,
) : ViewModel() {

    var query by mutableStateOf("")

    var allContacts = mutableStateListOf<Pair<String, String>>()
    var searchResult = mutableStateListOf<Pair<String, String>>()

    init {
        getAllContacts(application).forEach {
            allContacts.add(it)
        }
    }

    fun onQueryChange(newValue: String) {
        query = newValue
        searchAndSet()
    }

    private fun searchAndSet() {
        searchResult.clear()

        val matches = if (query.isNotEmpty()) {
            allContacts.filter {
                it.first.contains(query, ignoreCase = true) ||
                        it.second.contains(query, ignoreCase = true)
            }
        } else {
            emptyList()
        }

        matches.forEach {
            searchResult.add(it)
        }
    }

}