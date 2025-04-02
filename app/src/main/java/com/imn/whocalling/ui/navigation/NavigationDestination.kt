package com.imn.whocalling.ui.navigation

import com.imn.whocalling.ui.navigation.NavigationDestination.CallLog
import com.imn.whocalling.ui.navigation.NavigationDestination.Dialer
import kotlinx.serialization.Serializable

sealed class NavigationDestination {
    @Serializable
    data object Permission : NavigationDestination()

    @Serializable
    data object Home : NavigationDestination()

    @Serializable
    data object CallLog : NavigationDestination()

    @Serializable
    data object Dialer : NavigationDestination()
}

val HomeRoutes =
    arrayOf(
        CallLog,
        Dialer,
    )