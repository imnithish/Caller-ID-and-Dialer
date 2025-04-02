package com.imn.whocalling.ui.screens

import android.os.Build
import androidx.activity.ComponentActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.imn.whocalling.util.openAppPermissionSettings
import com.imn.whocalling.util.permission.PermissionState
import com.imn.whocalling.util.permission.calleridandspamroleapp.CallerIdAndSpamAppRoleRequester
import com.imn.whocalling.util.permission.systemalertwindow.SystemAlertWindowPermissionRequester
import com.imn.whocalling.util.permission.telephonyandcontacts.TelephonyAndContactsPermissionRequester
import com.imn.whocalling.util.toast

@Composable
fun PermissionScreen(
    systemAlertWindowPermissionRequester: SystemAlertWindowPermissionRequester,
    telephonyAndContactsPermissionRequester: TelephonyAndContactsPermissionRequester,
    callerIdAndSpamAppRoleRequester: CallerIdAndSpamAppRoleRequester,
    navigateToHome: () -> Unit
) {
    val context = LocalContext.current
    val activity = context as ComponentActivity
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current

    var isSystemAlertWindowPermissionGranted = remember {
        mutableStateOf(
            systemAlertWindowPermissionRequester.isPermissionGranted()
        )
    }
    var isTelephonyAndContactsPermissionGranted = remember {
        mutableStateOf(
            telephonyAndContactsPermissionRequester.areAllPermissionsGranted()
        )
    }
    var isCallerIdAndSpamAppRoleGranted = remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                callerIdAndSpamAppRoleRequester.isRoleGranted()
            else true
        )
    }
    var isContinueButtonAvailable = remember {
        mutableStateOf(
            systemAlertWindowPermissionRequester.isPermissionGranted()
                    && telephonyAndContactsPermissionRequester.areAllPermissionsGranted() &&
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                        callerIdAndSpamAppRoleRequester.isRoleGranted()
                    else true

        )
    }

    DisposableEffect(lifecycleOwner) {
        val observer = object : DefaultLifecycleObserver {
            override fun onResume(owner: LifecycleOwner) {
                isTelephonyAndContactsPermissionGranted.value =
                    telephonyAndContactsPermissionRequester.areAllPermissionsGranted()
                isContinueButtonAvailable.value =
                    systemAlertWindowPermissionRequester.isPermissionGranted()
                            && telephonyAndContactsPermissionRequester.areAllPermissionsGranted() &&
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                                callerIdAndSpamAppRoleRequester.isRoleGranted()
                            else true
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {

            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(
                    text = "Gimme these permissions \uD83D\uDE3E",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(32.dp))

                PermissionItem(
                    title = "System alert window",
                    isPermissionGranted = isSystemAlertWindowPermissionGranted.value,
                ) {
                    systemAlertWindowPermissionRequester.requestPermission {
                        isSystemAlertWindowPermissionGranted.value =
                            it == PermissionState.GRANTED
                    }
                }

                Spacer(Modifier.height(8.dp))

                PermissionItem(
                    title = "Read phone state, Read contacts, Read call log",
                    isPermissionGranted = isTelephonyAndContactsPermissionGranted.value == true,
                ) {
                    telephonyAndContactsPermissionRequester.requestPermissions {
                        isTelephonyAndContactsPermissionGranted.value =
                            telephonyAndContactsPermissionRequester.areAllPermissionsGranted()
                        if (it.containsValue(PermissionState.PERMANENTLY_DENIED)) {
                            activity toast "Please grant permissions from settings"
                            activity.openAppPermissionSettings()
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))

                PermissionItem(
                    title = "Default caller id and spam app",
                    isPermissionGranted = isCallerIdAndSpamAppRoleGranted.value == true,
                ) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                        callerIdAndSpamAppRoleRequester.requestRole {
                            isCallerIdAndSpamAppRoleGranted.value = it == PermissionState.GRANTED
                            if (it != PermissionState.GRANTED) {
                                activity toast "Try again or set 'Who Calling' as the default caller ID & spam app."
                            }
                        }
                }

                AnimatedVisibility(
                    visible = isContinueButtonAvailable.value,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 32.dp),
                ) {
                    OutlinedButton(
                        onClick = navigateToHome
                    ) {
                        Text("Continue")
                    }
                }

            }
        }
    }
}

@Composable
fun PermissionItem(
    title: String,
    isPermissionGranted: Boolean,
    onButtonClick: () -> Unit,
) {

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            modifier = Modifier.weight(1f),
            text = title
        )

        OutlinedButton(
            enabled = !isPermissionGranted,
            onClick = onButtonClick
        ) {
            Text(if (isPermissionGranted) "Granted" else "Grant")
        }
    }

}