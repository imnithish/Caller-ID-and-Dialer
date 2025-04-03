package com.imn.whocalling.ui.screens.search

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.text.input.ImeAction
import androidx.core.content.ContextCompat
import com.imn.whocalling.util.toast

@Composable
fun SearchScreen(viewModel: SearchViewModel = hiltViewModel()) {

    var phoneNumber by remember {
        mutableStateOf("")
    }
    val kc =
        LocalSoftwareKeyboardController.current

    val context = LocalContext.current
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:${phoneNumber}"))
                context.startActivity(intent)
            } else {
                context.toast("Permission Denied")
            }
        }

    Scaffold(
        topBar = {
            OutlinedTextField(
                value = viewModel.query,
                onValueChange = {
                    viewModel.onQueryChange(it)
                },
                label = { Text("Search Contact") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        kc?.hide()
                    }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier.padding(it),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            items(viewModel.searchResult) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),

                    onClick = {
                        phoneNumber = it.second
                        if (ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.CALL_PHONE
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            val intent =
                                Intent(
                                    Intent.ACTION_CALL,
                                    Uri.parse("tel:${phoneNumber}")
                                )
                            context.startActivity(intent)
                        } else {
                            launcher.launch(Manifest.permission.CALL_PHONE)
                        }
                    }
                ) {
                    Column(Modifier.padding(8.dp)) {
                        Text(text = it.first)
                        Text(text = it.second)
                    }

                }
            }

        }
    }
}