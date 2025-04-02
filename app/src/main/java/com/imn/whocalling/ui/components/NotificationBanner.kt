package com.imn.whocalling.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NotificationBanner(number: String, onClose:()-> Unit) {

    Card(modifier = Modifier.fillMaxWidth()) {
        Column {
            Text(number)
            Spacer(Modifier.height(8.dp))
            OutlinedButton(onClick = onClose) {
                Text("Close")
            }
        }
    }

}