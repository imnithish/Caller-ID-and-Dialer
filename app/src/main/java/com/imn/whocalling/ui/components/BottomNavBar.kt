package com.imn.whocalling.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Text
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imn.whocalling.data.BottomNavItem

@Composable
fun BottomNavBar(
    modifier: Modifier = Modifier,
    items: List<BottomNavItem>,
    selectedIndex: Int = 0,
    onClick: (index: Int) -> Unit
) {
    NavigationBar(
        modifier = modifier.height(56.dp),
        containerColor = MaterialTheme.colorScheme.background
    ) {
        items.forEachIndexed { index, item ->
            BottomNavigationItem(
                selected = index == selectedIndex,
                onClick = {
                    onClick(index)
                },
                icon = {
                    Icon(
                        modifier = modifier.size(24.dp),
                        painter = painterResource(item.icon),
                        contentDescription = item.name,
                        tint = if (index == selectedIndex) Color.Black else Color.Gray
                    )
                },
                label = {
                    Text(
                        text = item.name,
                        fontSize = 10.sp,
                        color = if (index == selectedIndex) Color.Black else Color.Gray,
                    )
                }
            )
        }
    }

}