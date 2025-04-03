package com.imn.whocalling.ui.screens.dialer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun DialerScreen(viewModel: DialerViewModel = hiltViewModel()) {
    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = viewModel.phoneNumber,
                    fontSize = 32.sp,
                    modifier = Modifier.padding(16.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                DialPad(viewModel)
            }
        }
    }
}

@Composable
fun DialPad(viewModel: DialerViewModel) {
    val numbers = listOf(
        listOf("1", "2", "3"),
        listOf("4", "5", "6"),
        listOf("7", "8", "9"),
        listOf("*", "0", "#")
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        numbers.forEach { row ->
            Row {
                row.forEach { number ->
                    DialButton(number) { viewModel.onNumberClick(number) }
                }
            }
        }
        Spacer(modifier = Modifier.height(64.dp))

        Row {
            OutlinedButton(
                enabled = viewModel.phoneNumber != "",
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
                onClick = { viewModel.onDelete() }) {
                Text(text = "Delete")
            }

            Spacer(modifier = Modifier.width(16.dp))

            OutlinedButton(
                enabled = viewModel.phoneNumber != "",
                onClick = { viewModel.onDelete() }) {
                Text(text = "Call")
            }
        }
    }
}

@Composable
fun DialButton(number: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(96.dp)
            .padding(8.dp)
            .background(Color.Gray, shape = CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(text = number, fontSize = 24.sp, textAlign = TextAlign.Center, color = Color.White)
    }
}