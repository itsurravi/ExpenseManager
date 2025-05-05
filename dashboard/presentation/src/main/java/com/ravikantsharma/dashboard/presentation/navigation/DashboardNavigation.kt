package com.ravikantsharma.dashboard.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.ravikantsharma.dashboard.presentation.dashboard.DashboardScreenRoot

fun NavGraphBuilder.dashboardNavGraph(
    navController: NavController,
    onNavigateToSettings: () -> Unit
) {
    navigation<DashboardBaseRoute>(
        startDestination = DashboardScreenRoute
    ) {
        composable<DashboardScreenRoute> {
            DashboardScreenRoot(
                onNavigateToSettings = onNavigateToSettings
            )
        }
    }
}