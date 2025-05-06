package com.ravikantsharma.dashboard.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.ravikantsharma.dashboard.presentation.dashboard.DashboardScreenRoot
import com.ravikantsharma.ui.AppNavRoute
import com.ravikantsharma.ui.NavigationRequestHandler
import com.ravikantsharma.ui.navigation.AllTransactionsScreenRoute
import com.ravikantsharma.ui.navigation.DashboardBaseRoute
import com.ravikantsharma.ui.navigation.DashboardScreenRoute
import com.ravikantsharma.ui.navigation.navigateToAllTransactionsScreenRoute

fun NavGraphBuilder.dashboardNavGraph(
    navController: NavController,
    onNavigateToSettings: () -> Unit,
    navigationRequestHandler: NavigationRequestHandler
) {
    navigation<DashboardBaseRoute>(
        startDestination = DashboardScreenRoute
    ) {
        composable<DashboardScreenRoute> {
            DashboardScreenRoot(
                onNavigateToSettings = onNavigateToSettings,
                onNavigateToAllTransactions = {
                    navigationRequestHandler.navigateWithAuthCheck(
                        AppNavRoute(
                            pendingRoute = AllTransactionsScreenRoute
                        )
                    )
                }
            )
        }
    }
}