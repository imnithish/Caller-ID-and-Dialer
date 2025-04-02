package com.imn.whocalling.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Dot(color: Color = Color.LightGray, size: Dp = 4.dp) {
    Canvas(modifier = Modifier.size(size)) {
        drawCircle(color = color, radius = size.toPx() / 2)
    }
}