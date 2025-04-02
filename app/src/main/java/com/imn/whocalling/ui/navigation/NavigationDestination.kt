package com.imn.whocalling.ui.navigation

import kotlinx.serialization.Serializable

sealed class NavigationDestination {
    @Serializable
    data object Permission : NavigationDestination()

    @Serializable
    data object Home : NavigationDestination()
}