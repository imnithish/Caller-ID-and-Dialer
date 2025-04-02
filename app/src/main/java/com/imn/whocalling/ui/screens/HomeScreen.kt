package com.imn.whocalling.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
fun HomeScreen(
    onSetupIncomingCallHandler: () -> Unit
) {

    LaunchedEffect(Unit) {
        onSetupIncomingCallHandler()
    }
}