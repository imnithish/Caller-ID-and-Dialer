package com.imn.whocalling.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imn.whocalling.R
import com.imn.whocalling.data.CallLogEntry

@Composable
fun CallLogItem(modifier: Modifier, log: CallLogEntry) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                AnimatedVisibility(log.whoCallingName != null || log.savedName != null) {
                    Text(
                        text = log.whoCallingName ?: log.savedName ?: "",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = log.number, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(text = log.type, color = Color.DarkGray)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = log.date, color = Color.DarkGray)
                    if (log.duration != "0s") {
                        Spacer(modifier = Modifier.width(4.dp))
                        Dot()
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = log.duration, color = Color.DarkGray)
                    }
                }
                AnimatedVisibility(
                    modifier = Modifier.padding(top = 2.dp),
                    visible = log.location != null
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(R.drawable.location_sign_svgrepo_com),
                            contentDescription = "Location",
                            modifier = Modifier
                                .size(18.dp),
                            tint = Color.DarkGray
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = log.location ?: "",
                            color = Color.DarkGray
                        )
                    }

                }
            }

            AnimatedVisibility(modifier = Modifier.padding(start = 16.dp), visible = log.isSpam) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(R.drawable.warning_svgrepo_com),
                        contentDescription = "Spam",
                        modifier = Modifier
                            .size(36.dp),
                        tint = Color.DarkGray
                    )
                    Text(text = "Likely a spam", color = Color.DarkGray)

                }
            }
        }

        Spacer(Modifier.height(8.dp))
        Divider(color = Color.LightGray)
    }
}