package com.imn.whocalling.ui.navigation

import android.os.Build
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.imn.whocalling.ui.screens.HomeScreen
import com.imn.whocalling.ui.screens.PermissionScreen
import com.imn.whocalling.util.permission.calleridandspamroleapp.CallerIdAndSpamAppRoleRequester
import com.imn.whocalling.util.permission.systemalertwindow.SystemAlertWindowPermissionRequester
import com.imn.whocalling.util.permission.telephonyandcontacts.TelephonyAndContactsPermissionRequester

@Composable
fun Navigation(
    systemAlertWindowPermissionRequester: SystemAlertWindowPermissionRequester,
    telephonyAndContactsPermissionRequester: TelephonyAndContactsPermissionRequester,
    callerIdAndSpamAppRoleRequester: CallerIdAndSpamAppRoleRequester,
    onSetupIncomingCallHandler: () -> Unit
) {
    val navController: NavHostController = rememberNavController()
    NavHost(
        modifier = Modifier.fillMaxSize(),
        navController = navController,
        startDestination = if (systemAlertWindowPermissionRequester.isPermissionGranted()
            && telephonyAndContactsPermissionRequester.areAllPermissionsGranted() &&
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                callerIdAndSpamAppRoleRequester.isRoleGranted()
            else true
        ) NavigationDestination.Home else NavigationDestination.Permission,
        enterTransition = { fadeIn() },
        exitTransition = { fadeOut() },
        popEnterTransition = { fadeIn() },
        popExitTransition = { fadeOut() },
    ) {

        composable<NavigationDestination.Permission> {
            PermissionScreen(
                systemAlertWindowPermissionRequester,
                telephonyAndContactsPermissionRequester,
                callerIdAndSpamAppRoleRequester
            ) {
                navController.navigate(NavigationDestination.Home)
            }
        }

        composable<NavigationDestination.Home> {
            HomeScreen(
                onSetupIncomingCallHandler = onSetupIncomingCallHandler
            )
        }

    }
}

