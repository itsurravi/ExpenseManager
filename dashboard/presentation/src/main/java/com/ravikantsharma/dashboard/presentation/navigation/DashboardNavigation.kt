package com.ravikantsharma.dashboard.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.ravikantsharma.dashboard.presentation.all_transactions.AllTransactionsScreenRoot
import com.ravikantsharma.dashboard.presentation.dashboard.DashboardScreenRoot
import com.ravikantsharma.ui.AppNavRoute
import com.ravikantsharma.ui.LocalAuthNavigationHandler
import com.ravikantsharma.ui.navigation.AllTransactionsScreenRoute
import com.ravikantsharma.ui.navigation.DashboardBaseRoute
import com.ravikantsharma.ui.navigation.DashboardScreenRoute

fun NavGraphBuilder.dashboardNavGraph(
    navController: NavController,
    isLaunchedFromWidget: Boolean,
    onNavigateToSettings: () -> Unit
) {
    navigation<DashboardBaseRoute>(
        startDestination = DashboardScreenRoute(isLaunchedFromWidget)
    ) {
        composable<DashboardScreenRoute> {
            val authNavigationHandler = LocalAuthNavigationHandler.current
            DashboardScreenRoot(
                onNavigateToSettings = onNavigateToSettings,
                onNavigateToAllTransactions = {
                    authNavigationHandler?.invoke(
                        AppNavRoute(
                            pendingRoute = AllTransactionsScreenRoute
                        )
                    )
                }
            )
        }

        composable<AllTransactionsScreenRoute> {
            AllTransactionsScreenRoot(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}