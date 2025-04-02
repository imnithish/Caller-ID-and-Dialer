package com.imn.whocalling

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.imn.whocalling.ui.navigation.Navigation
import com.imn.whocalling.ui.theme.WhoCallingTheme
import com.imn.whocalling.util.permission.calleridandspamroleapp.CallerIdAndSpamAppRoleRequester
import com.imn.whocalling.util.permission.systemalertwindow.SystemAlertWindowPermissionRequester
import com.imn.whocalling.util.permission.telephonyandcontacts.TelephonyAndContactsPermissionRequester
import dagger.hilt.android.AndroidEntryPoint


const val MockyName ="4ca5343d-256f-4325-8e46-04bbb493ae52"

//{
//    "numbers": [
//    {
//        "number": "+918907471155",
//        "name": "Nitheesh",
//        "isSpam": false,
//        "location": "Trivandrum"
//    },
//    {
//        "number": "+919876543210",
//        "name": "John Doe",
//        "isSpam": false,
//        "location": "Mumbai, India"
//    },
//    {
//        "number": "+441234567890",
//        "name": "Spam Caller",
//        "isSpam": true,
//        "location": "London, UK"
//    },
//    {
//        "number": "+15551234567",
//        "name": "Alice Smith",
//        "isSpam": false,
//        "location": "New York, USA"
//    }
//    ]
//}
//https://designer.mocky.io/

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var telephonyAndContactsPermissionRequester: TelephonyAndContactsPermissionRequester
    private lateinit var systemAlertWindowPermissionRequester: SystemAlertWindowPermissionRequester
    private lateinit var callerIdAndSpamAppRoleRequester: CallerIdAndSpamAppRoleRequester

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        telephonyAndContactsPermissionRequester = TelephonyAndContactsPermissionRequester(this)
        systemAlertWindowPermissionRequester = SystemAlertWindowPermissionRequester(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) callerIdAndSpamAppRoleRequester =
            CallerIdAndSpamAppRoleRequester(this)

        setContent {
            WhoCallingTheme {
                Navigation(
                    telephonyAndContactsPermissionRequester = telephonyAndContactsPermissionRequester,
                    systemAlertWindowPermissionRequester = systemAlertWindowPermissionRequester,
                    callerIdAndSpamAppRoleRequester = callerIdAndSpamAppRoleRequester,
                )
            }
        }
    }
}