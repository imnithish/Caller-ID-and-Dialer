package com.imn.whocalling.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.imn.whocalling.R
import com.imn.whocalling.data.BottomNavItem
import com.imn.whocalling.ui.components.BottomNavBar
import com.imn.whocalling.ui.navigation.HomeRoutes
import com.imn.whocalling.ui.navigation.NavigationDestination
import com.imn.whocalling.ui.navigation.NavigationDestination.CallLog
import com.imn.whocalling.ui.navigation.NavigationDestination.Dialer
import com.imn.whocalling.ui.screens.calllog.CallLogScreen
import com.imn.whocalling.ui.screens.dialer.DialerScreen
import com.imn.whocalling.ui.screens.search.SearchScreen

@Composable
fun HomeScreen(
) {
    val navController = rememberNavController()
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }

    Scaffold(modifier = Modifier.navigationBarsPadding(), bottomBar = {
        BottomNavBar(
            modifier = Modifier.fillMaxWidth(),
            items = remember {
                listOf(
                    BottomNavItem(
                        icon = R.drawable.baseline_access_time_24,
                        name = "Call log"
                    ),
                    BottomNavItem(
                        icon = R.drawable.baseline_dialpad_24,
                        name = "Dialer"
                    ),
                    BottomNavItem(
                        icon = R.drawable.search_24dp_1f1f1f,
                        name = "Search"
                    )
                )
            },
            selectedIndex = selectedTab
        ) {
            selectedTab = it
            navController.run controller@{
                navigate(
                    HomeRoutes[it],
                    navOptions {
                        popUpTo(this@controller.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    },
                )
            }
        }
    }) { innerPadding ->

        NavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            startDestination = CallLog,
        ) {

            composable<CallLog> {
                CallLogScreen()
            }

            composable<Dialer> {
                DialerScreen()
            }

            composable<NavigationDestination.Search> {
                SearchScreen()
            }

        }

    }

}