package com.imn.whocalling.ui.screens.dialer

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animation
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.imn.whocalling.util.toast

@Composable
fun DialerScreen(viewModel: DialerViewModel = hiltViewModel()) {

    val context = LocalContext.current
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:${viewModel.phoneNumber}"))
                context.startActivity(intent)
            } else {
                context.toast("Permission Denied")
            }
        }

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
                    fontSize = 30.sp,
                    modifier = Modifier.padding(16.dp)
                )
                AnimatedVisibility(
                    visible = viewModel.suggestion != null,
                    modifier = Modifier
                        .clickable {
                            if (ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.CALL_PHONE
                                ) == PackageManager.PERMISSION_GRANTED
                            ) {
                                val intent =
                                    Intent(
                                        Intent.ACTION_CALL,
                                        Uri.parse("tel:${viewModel.suggestion?.second}")
                                    )
                                context.startActivity(intent)
                            } else {
                                launcher.launch(Manifest.permission.CALL_PHONE)
                            }
                        }
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = viewModel.suggestion?.first ?: "",
                            fontSize = 22.sp,
                        )
                        Text(
                            text = viewModel.suggestion?.second ?: "",
                            fontSize = 18.sp,
                        )
                    }

                }
                Spacer(modifier = Modifier.height(16.dp))
                DialPad(viewModel, {
                    if (ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.CALL_PHONE
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        val intent =
                            Intent(Intent.ACTION_CALL, Uri.parse("tel:${viewModel.phoneNumber}"))
                        context.startActivity(intent)
                    } else {
                        launcher.launch(Manifest.permission.CALL_PHONE)
                    }
                })
            }
        }
    }
}

@Composable
fun DialPad(viewModel: DialerViewModel, onDial: () -> Unit) {
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

            Spacer(modifier = Modifier.width(8.dp))

            OutlinedButton(
                enabled = viewModel.phoneNumber != "",
                onClick = onDial
            ) {
                Text(text = "Call")
            }
        }
    }
}

@Composable
fun DialButton(number: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(88.dp)
            .padding(8.dp)
            .background(Color.Gray, shape = CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(text = number, fontSize = 24.sp, textAlign = TextAlign.Center, color = Color.White)
    }
}